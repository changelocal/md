package com.md.union.front.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标维权
 */
public enum BrandTypeEnums {

    chinese(1,"中文"),
    alphabet(2,"字母"),
    chinese_alphabet(3,"中文+字母"),
    image(4,"图形"),
    chinese_image(5,"中文+图形"),
    alphabet_image(6,"字母+图形"),
    chinese_alphabet_image(7,"中文+字母+图形"),
    number(8,"数字"),
    alphabet_number(9,"字母+数字"),

            ;

    BrandTypeEnums(int type, String title) {
        this.type = type;
        this.title = title;

    }

    public int getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }

    //类型
    private int type;
    //标题
    private String title;

    public static BrandTypeEnums valueType(int id) {
        for (BrandTypeEnums item : BrandTypeEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "主键id不存在");
    }

}
