package com.arc.util.tmkoo;

import lombok.Data;
import java.util.List;

/**
 * Created by sage on 2019/5/8.
 */

public final class Tmkoo {
    @Data
    public static class Result{
        //相关商标的注册数
        private int otherCnt;
        //商标名
        private String brandName;
        //注册了的注册号
        private List<String> regNos;
        private List<RegisterInfo> registers;
    }
    @Data
    public static class RegisterInfo{
        private int cate;
        private boolean register;
    }
    @Data
    public static class Flow{
        private List<FlowInfo> flowInfos;
    }
    @Data
    public static class FlowInfo{
        private String date;
        private String code;
        private String name;
    }
}