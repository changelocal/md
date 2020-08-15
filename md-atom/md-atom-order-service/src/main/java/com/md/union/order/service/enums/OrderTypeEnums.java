package com.md.union.order.service.enums;

// 1 商标注册订单 2 商标维权订单 3 商标信息变更订单
public enum OrderTypeEnums {

    SERVER_ORDER(1, "服务订单"),
    BRAND_ORDER(1, "商标订单"),
    ;

    OrderTypeEnums(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    private int type;
    private String title;

}
