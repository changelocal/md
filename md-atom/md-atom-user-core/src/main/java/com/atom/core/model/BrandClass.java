package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.LOWER)
public class BrandClass {
    private String id;
    private String name;
    private int code;
    //private String desc;
    private int pcode;
    private int isHot;
}
