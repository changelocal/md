package com.md.union.front.client.enums;

public enum OrderStatus {
    PAY_SUCCESS(1,"支付成功");

    private int code;
    private String desc;
    OrderStatus(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
