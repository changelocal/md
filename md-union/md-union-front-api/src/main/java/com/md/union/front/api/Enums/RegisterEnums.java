package com.md.union.front.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标服务
 */
public enum RegisterEnums {

    BRAND_NOMAL_RGISTER(1, "c443-4249-928b-31a36e19629d-a543c6820726-d41d8", "商标普通注册", "专家预审，通过率高，不成功退全款", 580, "https://pay.mdlogo.cn/file/brand-register/1.png"),
    BRAND_FAST_RGISTER(2, "bdda-4c25-9609-e1947bcc915b-1d4c872533ca-d41d8", "商标加急注册", "不成功退全款，免费再注册一次", 680, "https://pay.mdlogo.cn/file/brand-register/2.png"),
    BRAND_GUARANT_RGISTER(3, "6bc3-4699-9c95-9505bb002124-a543c6820726-d41d8", "商标担保注册", "59分钟急速注册，省心担保，注册不成功退全款", 1180, "https://pay.mdlogo.cn/file/brand-register/3.png"),
    BRAND_SHANRE_RGISTER(4, "f6fb-4d2a-b323-4d342dd3b4e7-1d4c872533ca-d41d8", "商标设计+注册双享套餐", "包含LOGO设计，商标加急注册，设计满意为止", 1580, "https://pay.mdlogo.cn/file/brand-register/4.png"),
    BRAND_PERSON_RGISTER(5, "0dbd-4104-a574-833130dc39c7-1d4c872533ca-d41d8", "个体营业执照+商标注册", "代办个体营业执照只需要身份证", 980, "https://pay.mdlogo.cn/file/brand-register/5.png"),
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
