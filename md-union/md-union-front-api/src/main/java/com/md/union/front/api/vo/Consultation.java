package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("咨询")
public class Consultation {

    @Data
    @ApiModel("发起咨询")
    public static class ConsultationReq{
        @ApiModelProperty("商标ID")
        private String id;
    }
    @Data
    @ApiModel("咨询对象信息")
    public static class ConsultationResp{
        @ApiModelProperty("姓名")
        private String name;
        @ApiModelProperty("头衔")
        private String title;
        @ApiModelProperty("电话")
        private String tel;
        @ApiModelProperty("qq")
        private String qq;
        @ApiModelProperty("头像")
        private String avatar;
    }

    @Data
    @ApiModel("45大分类全部")
    public static class BrandClassResp{
        @ApiModelProperty("45大分类")
        private List<RootBrandClass> rootBrandClasses;
    }
    @Data
    @ApiModel("45大分类")
    public static class RootBrandClass{
        @ApiModelProperty("代号")
        private int code;
        @ApiModelProperty("名字")
        private String name;
    }
    @Data
    @ApiModel("热门搜索")
    public static class HomeHotResp{
        @ApiModelProperty("搜索数量")
        private String searchCount;
        @ApiModelProperty("热门搜索")
        private List<String> hotSearch;

    }
    @Data
    @ApiModel("45大分类详情")
    public static class BrandClassDetailsResp{
        @ApiModelProperty("45大分类")
        private List<BrandClass> brandClasses;
    }
    @Data
    @ApiModel("45大分类")
    public static class BrandClass{
        @ApiModelProperty("名字")
        private String name;
        @ApiModelProperty("详情")
        private String desc;
    }
}
