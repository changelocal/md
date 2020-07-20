package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.CAMEL)
public class AdminUser {
    private String id;
    private String mobile;
    private String qqAccount;
    private String wxAccount;
    private String nickName;
    private int isEnable;
}
