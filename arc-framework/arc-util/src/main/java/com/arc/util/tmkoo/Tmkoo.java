package com.arc.util.tmkoo;

import lombok.Data;
import java.util.List;

/**
 * Created by sage on 2019/5/8.
 */

public final class Tmkoo {
    @Data
    public static class Result{
        private int otherCnt;
        private String brandName;
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
        private String name;
    }
}