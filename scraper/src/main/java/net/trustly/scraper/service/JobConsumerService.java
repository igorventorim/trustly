package net.trustly.scraper.service;

import com.google.gson.Gson;
import java.util.List;
import java.util.Objects;
import net.trustly.scraper.domain.FileExtensionInfo;
import net.trustly.scraper.domain.Job;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class JobConsumerService {

    @Autowired
    private FileExtensionInfoService fileExtensionInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RequestService requestService;

    private Logger logger = LoggerFactory.getLogger(net.trustly.scraper.service.JobConsumerService.class);

    @Async
    @KafkaListener(topics = {"${job.topic}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(ConsumerRecord consumerRecord) {
        String url = (String)consumerRecord.value();
        Long jobId = Long.valueOf((String)consumerRecord.key());
        String jobJson = (String)this.redisTemplate.opsForValue().get(jobId);
        Gson gson = new Gson();
        Job job = (Job)gson.fromJson(jobJson, Job.class);
        this.logger.info("Consumer start job_id:" + String.valueOf(jobId));
        try {
            List<FileExtensionInfo> listFileExtensionInfo = this.fileExtensionInfoService.getFilesExtensionInfoListInCache(url);
            if (Objects.isNull(listFileExtensionInfo)) {
                if (!this.requestService.isUrlRequestSuccessful(url)) {
                    job.setJobStatus(Job.JobStatus.ERROR);
                    job.setResponse("The URL '" + url + "' is not a valid repository.");
                    jobJson = gson.toJson(job);
                    this.redisTemplate.opsForValue().set(jobId, jobJson);
                    this.logger.info("Consumer finish job_id:" + String.valueOf(jobId));
                    return;
                }
                listFileExtensionInfo = this.fileExtensionInfoService.getFilesExtensionInfoList(url);
            }
            job.setResponse(listFileExtensionInfo);
            job.setJobStatus(Job.JobStatus.DONE);
        } catch (Exception e) {
            e.printStackTrace();
            job.setJobStatus(Job.JobStatus.ERROR);
            job.setResponse("Sorry, there was an error processing, please try again later.");
        }
        jobJson = gson.toJson(job);
        this.redisTemplate.opsForValue().set(jobId, jobJson);
        this.logger.info("Consumer finish job_id:" + String.valueOf(jobId));
    }
}
