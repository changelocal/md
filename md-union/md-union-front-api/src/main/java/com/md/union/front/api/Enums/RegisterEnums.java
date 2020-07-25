package com.md.union.front.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标服务
 */
public enum RegisterEnums {

    BRAND_NOMAL_RGISTER(1, "商标普通注册", "专家预审，通过率高，不成功退全款", 580,""),
    BRAND_FAST_RGISTER(2, "商标加急注册", "不成功退全款，免费再注册一次", 680,""),
    BRAND_GUARANT_RGISTER(3, "商标担保注册", "59分钟急速注册，省心担保，注册不成功退全款", 1180,""),
    BRAND_SHANRE_RGISTER(4, "商标设计+注册双享套餐", "包含LOGO设计，商标加急注册，设计满意为止", 1580,""),
    BRAND_PERSON_RGISTER(5, "个体营业执照+商标注册", "代办个体营业执照只需要身份证", 980,""),
    ;

    RegisterEnums(int type, String title, String brief, int price,String img) {
        this.type = type;
        this.title = title;
        this.brief = brief;
        this.price = price;
        this.img = img;
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

    public String getImg() {
        return img==null?def_img:img;
    }


    private String def_img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595638504208&di=754606fdcb88e6b4ee9ea1476f4c2a5f&imgtype=0&src=http%3A%2F%2Fimage-ali.bianjiyi.com%2F1%2F2018%2F0710%2F14%2F15312043093853.jpg";

    //维权类型
    private int type;
    //标题
    private String title;
    //简介
    private String brief;
    //价格
    private int price;
    //图片
    private String img;

    public static RegisterEnums valueType(int id) {
        for (RegisterEnums item : RegisterEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标注册主键id不存在");
    }

}
