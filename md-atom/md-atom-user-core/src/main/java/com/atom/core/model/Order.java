package com.atom.core.model;

import com.arc.db.jsd.NameStyle;
import com.arc.db.jsd.annotation.JsdTable;
import lombok.Data;

@Data
@JsdTable(nameStyle = NameStyle.LOWER)
public class Order {
    private int id;
    private String name;
}
