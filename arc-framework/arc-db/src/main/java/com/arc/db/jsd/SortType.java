package com.arc.db.jsd;


public enum SortType {
    ASC("ASC"), DESC("DESC");

    String value;

    SortType(String value) {
        this.value = value;
    }
}
