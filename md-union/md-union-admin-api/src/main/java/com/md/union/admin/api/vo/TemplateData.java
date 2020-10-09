package com.md.union.admin.api.vo;

import lombok.Data;

@Data
public class TemplateData {

    private String value;

    public TemplateData(String value) {
        this.value = value;
    }
}