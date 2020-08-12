package com.md.union.front.client.enums;

public enum  ResultCode {
    SUCCESS(1,"SUCCESS"),FAIL(2,"FAIL") ;

    private int code;
    private String desc;
    ResultCode(int code,String desc){
        this.code=code;
        this.desc=desc;
    }
    public int value() {
        return this.code;
    }

}