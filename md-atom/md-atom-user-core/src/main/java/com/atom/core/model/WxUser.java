package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

import java.util.Date;

@Data
@JsdTable(nameStyle = NameStyle.CAMEL)
public class WxUser {
    private String id;
    private String mobile;
    private String openId;
    private String appId;
    private String unionId;
    private String nickName;
    private int isEnable;
    private int followStatus;
    private Date createTime;
}
