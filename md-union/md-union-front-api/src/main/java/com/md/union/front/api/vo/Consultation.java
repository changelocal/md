package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    }
}
