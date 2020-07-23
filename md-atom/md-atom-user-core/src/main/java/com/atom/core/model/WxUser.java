package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.CAMEL)
public class WxUser {
    private String id;
    private String mobile;
    private String open_id;
    private String union_id;
    private String nickName;
    private int isEnable;
}
