package com.arc.common;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errCode;

    public ServiceException(String errorCode, String message) {
        super(message);
        this.errCode = errorCode;
    }

    public ServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errorCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}
