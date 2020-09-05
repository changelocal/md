package com.md.union.web.api.Enums;

//订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
public enum OrderStatusEnums {

    PRE_PAY(1, "待支付定金"),
    PRE_SUB(2, "待提交资料"),
    TRUST(3, "委托受理"),
    END_PAY(4, "待支付尾款"),
    END(5, "已完成"),
    ;

    OrderStatusEnums(int type, String title) {
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
