package net.trustly.scraper.domain;

import java.io.Serializable;
import java.util.Objects;

public class FileExtensionInfo implements Serializable {
    public String extension;

    public int count;

    public int lines;

    public long bytes;

    public FileExtensionInfo(String extension, Integer lines, Long bytes) {
        this.extension = extension;
        this.lines = Objects.nonNull(lines) ? lines.intValue() : 0;
        this.bytes = Objects.nonNull(bytes) ? bytes.longValue() : 0L;
        this.count = 1;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getCount() {
        return this.count;
    }

    public int getLines() {
        return this.lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public long getBytes() {
        return this.bytes;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void merge(net.trustly.scraper.domain.FileExtensionInfo fileExtensionInfo) {
        if (!this.extension.equals(fileExtensionInfo.getExtension()))
            throw new RuntimeException("Merging is only allowed to the same extension");
        this.count += fileExtensionInfo.getCount();
        this.bytes += fileExtensionInfo.getBytes();
        this.lines += fileExtensionInfo.getLines();
    }
}

