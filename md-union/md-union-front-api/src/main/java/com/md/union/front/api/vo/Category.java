package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商标分类")
public class Category {

    @Data
    @ApiModel("商标分类详情展示")
    public static class Info{
        @ApiModelProperty("商标分类编号")
        private String categoryNo;
        @ApiModelProperty("商标分类名称")
        private String categoryName;
        @ApiModelProperty("icon图片")
        private String icon;
        @ApiModelProperty("分类详细介绍")
        private String desc;
        @ApiModelProperty("分类是否注册")
        private boolean registerStatus;
    }

    @Data
    @ApiModel("商标分类详情购买展示")
    public static class SearchReq{
        @ApiModelProperty("是否注册")
        private Boolean registerSatus;
        @ApiModelProperty("商标id")
        private String brandId;
    }

    @Data
    @ApiModel("商标分类查询结果")
    public static class SearchRes{
        @ApiModelProperty("分类列表")
        private List<Info> list;
        @ApiModelProperty("总条数")
        private int count;
    }


}
