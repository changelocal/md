package com.md.union.front.client.dto;

import lombok.Data;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("OSS返回文件名字")
public class TrademarkDTO {
    @Data
    public static class Hot{

    }
    @Data
    public static class HotResp{
        private List<HotTrademarkCate> hotTrademarkCates;
    }

    public static class HotTrademarkCate{
        @ApiModelProperty("分类代号")
        private String number;
        @ApiModelProperty("分类名字")
        private String name;
        @ApiModelProperty("分类下的热门商标")
        private List<HotTrademark> hotTrademarks;
    }

    public static class HotTrademark{
        private String id;
        private String name;
        private String pic;
        private String price;
    }

    @Data
    public static class Test{
        private String id;
        private String name;
    }

    @Data
    public static class TestResp{
        private String id;
        private String name;
    }

}
