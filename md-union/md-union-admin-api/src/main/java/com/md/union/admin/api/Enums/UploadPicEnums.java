package com.md.union.admin.api.Enums;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;

/**
 * 商标维权
 */
public enum UploadPicEnums {

    ID_FRONT(1,"身份证正面"),
    ID_BACK(2,"身份证背面"),
    BUSINESS_LICENSE(3,"营业执照"),
    APPLY(4,"注册资料"),

            ;

    UploadPicEnums(int type, String title) {
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

    public static UploadPicEnums valueType(int id) {
        for (UploadPicEnums item : UploadPicEnums.values()) {
            if (item.type == id) {
                return item;
            }
        }
        throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "主键id不存在");
    }

}
