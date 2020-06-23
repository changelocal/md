package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("OSS返回文件名字")
public class OssFileInfo {
    @ApiModelProperty(value = "OSS返回文件名字")
    private String name;

}