package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商标起名")
public class Name {

    @Data
    @ApiModel("商标起名详情展示")
    public static class Info{
        @ApiModelProperty("商标编号")
        private int id;
        @ApiModelProperty("商标名称")
        private String brand;
        @ApiModelProperty("成功率")
        private String successRate;
        @ApiModelProperty("购买/注册")
        private int buyOrRegist;
    }

    @Data
    @ApiModel("商标起名搜索")
    public static class SearchReq{
        @ApiModelProperty("行业分类")
        private int category;
        @ApiModelProperty("输入")
        private String input;
        @ApiModelProperty("状态")
        private int status;
        @ApiModelProperty("字数")
        private int wordCnt;
        @ApiModelProperty("当前页")
        public int pageIndex;
        @ApiModelProperty("每页显示条数")
        public int pageSize;
    }

    @Data
    @ApiModel("商标起名查询结果")
    public static class SearchRes{
        @ApiModelProperty("分类列表")
        private List<Info> list;
        @ApiModelProperty("总条数")
        private int count;
    }


}
