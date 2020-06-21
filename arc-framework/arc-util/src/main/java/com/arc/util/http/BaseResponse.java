package com.arc.util.http;

public class BaseResponse<T>  {
    public static final String STATUS_HANDLE_SUCCESS = "000000";
    public static final String STATUS_SYSTEM_FAILURE = "999999";

    private String status;
    private T result;
    private String message;

    public BaseResponse() {
        this(STATUS_HANDLE_SUCCESS, null, null);
    }

    public BaseResponse(T result) {
        this(result, null);
    }

    public BaseResponse(T result, String message) {
        this(STATUS_HANDLE_SUCCESS, result, message);
    }

    public BaseResponse(String status, T result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public BaseResponse(String status, String message) {
        this(status, null, message);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
