package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.CAMEL)
public class AdminUser {
    private int id;
    private String mobile;
    private String qqAccount;
    private String nickName;
}
