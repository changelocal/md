package com.md.union.admin.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

// 1 商标注册订单 2 商标维权订单 3 商标信息变更订单
public enum OrderTypeEnums {

    BRAND_REGISTER(1, "商标注册订单"),
    BRAND_DEAL(2, "商标维权订单"),
    BRAND_CHANGE(3, "商标信息变更订单"),
    ;

    OrderTypeEnums(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public static OrderTypeEnums valueType(int id) {
        for (OrderTypeEnums item : OrderTypeEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "主键id不存在");
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
