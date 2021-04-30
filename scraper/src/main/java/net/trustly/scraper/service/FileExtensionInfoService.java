package net.trustly.scraper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.trustly.scraper.domain.FileExtensionInfo;
import net.trustly.scraper.domain.PageItem;
import net.trustly.scraper.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileExtensionInfoService {
    @Value("${cache.time}")
    private long CACHE_TIME;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RequestService requestService;

    public List<FileExtensionInfo> getFilesExtensionInfoListInCache(String url) {
        return (List<FileExtensionInfo>)this.redisTemplate.opsForValue().get(url);
    }

    public List<FileExtensionInfo> getFilesExtensionInfoList(String url) {
        List<FileExtensionInfo> result = null;
        String htmlPage = this.requestService.getHtmlPage(url);
        List<PageItem> rootPageItems = getPageItems(htmlPage);
        Map<String, FileExtensionInfo> fileExtensionInfoMap = new HashMap<>();
        buildFileExtensionInfoMap(fileExtensionInfoMap, rootPageItems);
        result = (List<FileExtensionInfo>)fileExtensionInfoMap.values().stream().collect(Collectors.toList());
        this.redisTemplate.opsForValue().set(url.trim(), result, this.CACHE_TIME, TimeUnit.SECONDS);
        return result;
    }

    private void buildFileExtensionInfoMap(Map<String, FileExtensionInfo> fileExtensionInfoMap, List<PageItem> pageItems) {
        for (PageItem pageItem : pageItems) {
            String htmlPage = this.requestService.getHtmlPageWithoutPrefix(pageItem.getUri());
            List<PageItem> children = getPageItems(htmlPage);
            if (children.isEmpty()) {
                FileExtensionInfo fileExtensionInfoExtracted = getFileExtensionInfo(htmlPage, pageItem);
                if (fileExtensionInfoMap.containsKey(pageItem.getExtension())) {
                    FileExtensionInfo fileExtensionInfoRegistered = fileExtensionInfoMap.get(pageItem.getExtension());
                    fileExtensionInfoRegistered.merge(fileExtensionInfoExtracted);
                    continue;
                }
                fileExtensionInfoMap.put(pageItem.getExtension(), fileExtensionInfoExtracted);
                continue;
            }
            buildFileExtensionInfoMap(fileExtensionInfoMap, children);
        }
    }

    private FileExtensionInfo getFileExtensionInfo(String htmlPage, PageItem pageItem) {
        Matcher m = getMatcherInputWithRegularExpression(htmlPage, "<div class=\"text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1\">(.*?)</div>");
        if (m.find()) {
            String fileInfo = m.group(1).trim();
            String[] parts = fileInfo.split(" ");
            Integer numberOfLines = getNumberOfLinesInText(parts);
            Long sizeOfFileInBytes = getSizeBytesInText(parts);
            FileExtensionInfo fileExtensionInfo = new FileExtensionInfo(pageItem.getExtension(), numberOfLines, sizeOfFileInBytes);
            return fileExtensionInfo;
        }
        throw new RuntimeException("Error to get info file");
    }

    private Integer getNumberOfLinesInText(String[] parts) {
        Integer numberOfLines = Integer.valueOf(0);
        int index = List.<String>of(parts).indexOf("lines");
        if (index != -1)
            numberOfLines = Integer.valueOf(parts[index - 1]);
        return numberOfLines;
    }

    private Long getSizeBytesInText(String[] parts) {
        Long sizeInBytes = Long.valueOf(0L);
        String[] types = { "Bytes", "KB", "MB", "GB" };
        for (int i = 0; i < types.length; i++) {
            int index = List.<String>of(parts).indexOf(types[i]);
            if (index != -1)
                sizeInBytes = Long.valueOf(Double.valueOf(Math.ceil(Double.valueOf(parts[index - 1]).doubleValue() * Math.pow(1000.0D, i))).longValue());
        }
        return sizeInBytes;
    }

    private List<PageItem> getPageItems(String htmlPage) {
        List<PageItem> pageItems = new ArrayList<>();
        Matcher m = getMatcherInputWithRegularExpression(htmlPage, "<a class=\"js-navigation-open Link--primary\"(.*?)</a>");
        while (m.find()) {
            String hyperLinkTag = m.group();
            String url = getMatchedElement(hyperLinkTag, "href=\"(.*?)\"");
            String title = getMatchedElement(hyperLinkTag, "title=\"(.*?)\"");
            PageItem pageItem = new PageItem(title, url);
            pageItems.add(pageItem);
        }
        return pageItems;
    }

    private Matcher getMatcherInputWithRegularExpression(String input, String regex) {
        input = input.replace("\n", "");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m;
    }

    private String getMatchedElement(String input, String regex) {
        Matcher m = getMatcherInputWithRegularExpression(input, regex);
        String element = m.find() ? m.group(1) : null;
        return element;
    }
}
