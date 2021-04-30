package net.trustly.github.domain;

import java.util.List;

public class ApiError extends ApiResponse{

    private List<String> errors;

    private String message;

    public ApiError(int status, String message, List<String> errors, String path) {
        super(status,path);
        this.message = message;
        this.errors = errors;
    }

    public ApiError(int status, String message, String error, String path) {
        super(status,path);
        this.message = message;
        this.errors = List.of(error);
    }

    public String getMessage() {
        return message;
    }
    public List<String> getErrors() {
        return errors;
    }


}
