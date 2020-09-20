package com.md.union.front.api.Enums;

// 1 商标注册订单 2 商标维权订单 3 商标信息变更订单
public enum OrderTypeEnums {

    BRAND_REGISTER(1, "商标注册订单"),
    BRAND_DEAL(2, "商标维权订单"),
    BRAND_CHANGE(3, "商标信息变更订单"),
    BRAND_BUY(4, "普通商标订单"),
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
