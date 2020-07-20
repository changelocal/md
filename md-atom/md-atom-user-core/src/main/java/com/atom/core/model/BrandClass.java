package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.CAMEL)
public class BrandClass {
    private String id;
    private String name;
    private String code;
    private String desc;
    private String pcode;
    private String isHot;
}
