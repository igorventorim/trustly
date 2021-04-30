package net.trustly.github.domain;

import java.util.Calendar;

public class ApiResponse {

    private Calendar timestamp;

    private int status;

    private String path;

    public ApiResponse(int status, String path) {
        this.status = status;
        this.path = path;
        this.timestamp = Calendar.getInstance();
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

}
