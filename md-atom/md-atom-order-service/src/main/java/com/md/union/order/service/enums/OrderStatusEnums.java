package com.md.union.order.service.enums;

//订单状态 1待支付定金 2待提交资料 3委托受理 4待支付尾款 5已完成
public enum OrderStatusEnums {

    PRE_PAY(1, "待支付定金"),
    PRE_SUB(1, "待提交资料"),
    TRUST(1, "委托受理"),
    END_PAY(1, "待支付尾款"),
    END(1, "已完成"),
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
