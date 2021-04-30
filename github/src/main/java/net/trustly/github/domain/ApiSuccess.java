package net.trustly.github.domain;

public class ApiSuccess extends ApiResponse{

    private Object result;

    public ApiSuccess(int status, String path, Object response) {
        super(status, path);
        this.result = response;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}
