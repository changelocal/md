package com.md.union.admin.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("资料")
public class Ref {
    @Data
    public static class OrderRefFile {
        /**
         * 主键id
         */
        private Long id;

        /**
         * 文件编号oss id
         */
        private String fileId;

        /**
         * 文件所属1公司2个人
         */
        private int fileSource;

        /**
         * 文件类型
         */
        private int fileType;
        private String fileTypeName;

        /**
         * 创建时间
         */
        private Date createTime;

    }

    @Data
    public static class QueryResp{
        private List<OrderRefFile> orderRefFiles;
        private int total;
    }
    @Data
    public static class Resp{
        private int code;
    }



}