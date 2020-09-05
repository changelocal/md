package com.md.union.web.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标服务
 */
public enum RegisterEnums {

    BRAND_NOMAL_RGISTER(1, "R0001", "商标普通注册", "专家预审，通过率高，不成功退全款", 580, "http://47.92.65.35:8082/file/brand-register/1.png"),
    BRAND_FAST_RGISTER(2, "R0001", "商标加急注册", "不成功退全款，免费再注册一次", 680, "http://47.92.65.35:8082/file/brand-register/2.png"),
    BRAND_GUARANT_RGISTER(3, "R0001", "商标担保注册", "59分钟急速注册，省心担保，注册不成功退全款", 1180, "http://47.92.65.35:8082/file/brand-register/3.png"),
    BRAND_SHANRE_RGISTER(4, "R0001", "商标设计+注册双享套餐", "包含LOGO设计，商标加急注册，设计满意为止", 1580, "http://47.92.65.35:8082/file/brand-register/4.png"),
    BRAND_PERSON_RGISTER(5, "R0001", "个体营业执照+商标注册", "代办个体营业执照只需要身份证", 980, "http://47.92.65.35:8082/file/brand-register/5.png"),
    ;

    RegisterEnums(int type, String no, String title, String brief, int price, String icon) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.icon = icon;
        this.no = no;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public int getPrice() {
        return price;
    }

    public String getIcon() {
        return icon;
    }

    public String getNo() {
        return no;
    }


    //维权类型
    private int type;
    //标题
    private String title;
    //简介
    private String brief;
    //价格
    private int price;
    //图片
    private String icon;
    //产品编号
    private String no;

    public static RegisterEnums valueType(int id) {
        for (RegisterEnums item : RegisterEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标注册主键id不存在");
    }

    public static RegisterEnums valueNo(String no) {
        for (RegisterEnums item : RegisterEnums.values()) {
            if (item.no.equals(no)) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标注册主键id不存在");
    }

}
