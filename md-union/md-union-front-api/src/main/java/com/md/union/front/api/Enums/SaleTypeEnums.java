package com.md.union.front.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标维权
 */
public enum SaleTypeEnums {

    not_sale(1,"未售"),
    saled(2,"已售"),
    locked(3,"被锁定"),
            ;

    SaleTypeEnums(int type, String title) {
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

    public static SaleTypeEnums valueType(int id) {
        for (SaleTypeEnums item : SaleTypeEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "主键id不存在");
    }

}
