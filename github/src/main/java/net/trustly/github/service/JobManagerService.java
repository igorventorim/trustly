package net.trustly.github.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kotlin.jvm.Synchronized;
import net.trustly.github.domain.FileExtensionInfo;
import net.trustly.github.domain.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobManagerService {

    @Value("${job.topic}")
    private String jobTopic;

    private final KafkaTemplate kafkaTemplate;

    private final RedisTemplate redisTemplate;

    public JobManagerService(final KafkaTemplate kafkaTemplate, RedisTemplate redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;

        if(!redisTemplate.hasKey("job_id")) {
            redisTemplate.opsForValue().set("job_id",Long.valueOf(0));
        }
    }

    public Map<String, Long> send(final String url) {
        Job job = createJob(url);
        kafkaTemplate.send(jobTopic, String.valueOf(job.getJobId()), url);
        return createResponseJob(job.getJobId());
    }

    public Job createJob(String url) {
        Long key = getNextIdJob();
        Job job = new Job();
        job.setJobId(key);
        job.setJobStatus(Job.JobStatus.IN_PROCESS);
        job.setUrl(url);
        String jobJson = new Gson().toJson(job);
        redisTemplate.opsForValue().set(key,jobJson);
        return job;
    }

    @Synchronized
    private Long getNextIdJob() {
        Long key = (Long)redisTemplate.opsForValue().get("job_id");
        redisTemplate.opsForValue().set("job_id",++key);
        return key;
    }

    private Map<String, Long> createResponseJob(Long key) {
        Map<String, Long> result = new HashMap<>();
        result.put("jobId",key);
        return result;
    }

    public Job getResultJobById(Long id) {

        if(!redisTemplate.hasKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String jobJson = (String)redisTemplate.opsForValue().get(id);
        Gson gson = new Gson();
        Job jobResult = gson.fromJson(jobJson, Job.class);
        //CONVERT RESPONSE TO TYPE FILEEXTENSIONINFO
        if(jobResult.getResponse() instanceof ArrayList) {
            jobResult.setResponse(
                    gson.fromJson(gson.toJson(jobResult.getResponse()),
                            new TypeToken<List<FileExtensionInfo>>(){}.getType()));
        }
        return jobResult;
    }



}
