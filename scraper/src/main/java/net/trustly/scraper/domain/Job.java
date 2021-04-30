package net.trustly.scraper.domain;

import java.io.Serializable;

public class Job implements Serializable {

    private Long jobId;

    private String url;

    private JobStatus jobStatus;

    private Object response;

    public enum JobStatus {
        IN_PROCESS,
        DONE,
        ERROR;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}