package com.arc.util.express;

import lombok.Data;

import java.util.List;

/**
 * Created by sage on 2019/5/8.
 */
//("快递公司相关信息")
public final class Express {

    //("物流公司检索")
    @Data
    public static class CompanyIndex {
        //("分组字母 A B C")
        private String groupName;
        //("物流公司列表")
        private List<CompanyGroupInfo> list;
    }

    //("物流公司检索详情")
    @Data
    public static class CompanyGroupInfo{
        //("物流公司名称")
        private String name;
        //("物流公司logo")
        private String logo;
        //("物流公司电话")
        private String mobile;

    }


    //("快递公司详情")
    @Data
    public static class CompanyInfo {
        //("运单号")
        private String waybillNo;
        //("快递公司编号")
        private String companyNo;
        //("快递公司名称")
        private String companyName;
        //("是否投递完成 2-在途中,3-签收,4-问题件")
        private int delivered;
        //("物流状态描述")
        private String deliveredDesc;
        //("是否可以投保")
        private boolean buyInsurance;
        //("物流轨迹列表")
        private List<TraceInfo> traceList;
    }

    //("物流轨迹信息")
    @Data
    public static class TraceInfo {
        //("节点信息")
        private String acceptStation;
        //("节点时间")
        private String acceptTime;
    }

    @Data
    //("按快递单号查询快递公司信息")
    public static class CompanyQuery {
        //("快递单号")
        private String waybillNo;
        //("物流公司编号")
        private String companyNo;
    }

    @Data
    //("快递100物流轨迹信息")
    public static class Kd100Query {
        //("我方分配给贵司的的公司编号")
        private String customer;
        //("签名， 用于验证身份")
        private String sign;
        //("其他参数组合成的json对象")
        private Param param;
    }

    //("其他参数组合成的json对象")
    @Data
    public static class Param {
        //("查询的快递公司的编码，一律用小写字母")
        private String com;
        //("查询的快递单号， 单号的最大长度是32个字符")
        private String num;
        //("收件人或寄件人的手机号或固话")
        private String phone;
        //("出发地城市，省-市-区")
        private String from;
        //("目的地城市，省-市-区")
        private String to;
        //("添加此字段表示开通行政区域解析功能")
        private String resultv2;
    }
}