package com.md.union.front.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("通用，上传图片，提交资料")
public class Common {

    @Data
    @ApiModel("提交资料")
    public static class CommitReq{
        @ApiModelProperty("订单id")
        private String id;
        @ApiModelProperty("公司、个人")
        private int model;
        @ApiModelProperty("资料list")
        private List<Ref> refs;

    }
    @Data
    @ApiModel("提交资料")
    public static class Ref{
        @ApiModelProperty("oss id")
        private String refId;
        @ApiModelProperty("文件类型")
        private int type;
    }
    @Data
    @ApiModel("提交资料结果")
    public static class CommitRes{
        @ApiModelProperty("结果")
        private String ret;
    }
    @Data
    @ApiModel("微信授权手机")
    public static class MinGetPhone{

        private String encryptedData;
        private String session_key;
        private String iv;
    }

}
