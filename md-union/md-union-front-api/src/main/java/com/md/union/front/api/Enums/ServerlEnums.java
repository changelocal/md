package com.md.union.front.api.Enums;

public enum DealEnums {

    BRAND_NOMAL_RGISTER(1, "商标普通注册", "专家预审，通过率高，不成功退全款", 580),
    BRAND_FAST_RGISTER(1, "商标加急注册", "不成功退全款，免费再注册一次", 680),
    BRAND_GUARANT_RGISTER(1, "商标担保注册", "59分钟急速注册，省心担保，注册不成功退全款", 1180),
    BRAND_SHANRE_RGISTER(1, "商标设计+注册双享套餐", "包含LOGO设计，商标加急注册，设计满意为止", 1580),
    BRAND_PERSON_RGISTER(1, "个体营业执照+商标注册", "代办个体营业执照只需要身份证", 980),
    ;

    DealEnums(int type, String title, String brief, int price) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
    }

    //维权类型
    private int type;
    //标题
    private String title;
    //简介
    private String brief;
    //价格
    private int price;

}
