package com.arc.util.tmkoo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arc.util.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TmkooCommon {
    public static final String apiPassword = "P8G3m5dR6Dj";
    public static final String apiKey = "QIJIAN_1819072015";

   /* public static void main(String[] args) {
//        try {
//            TmkooCommon.search("卓霸");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        TmkooCommon.info("12816966", 25);
    }*/

    /**
     * @param regNo 注册号
     * @param cls   国际分类
     */
    public static Tmkoo.Flow info(String host, String regNo, int cls) {
        Tmkoo.Flow flow = new Tmkoo.Flow();
        String url = host + "?apiKey=" + apiKey + "&apiPassword=" + apiPassword + "&regNo=" + regNo + "&intCls=" + cls;
        System.out.println(url);
        String resp = HttpRequest.get(url).body();
        System.out.println(resp);
        log.info(resp);
        JSONObject data = JSONObject.parseObject(resp);
        if (data != null) {
            String ret = data.getString("ret");
            if("0".equals(ret)){
                List<Tmkoo.FlowInfo> flowInfos = new ArrayList<>();
                JSONArray results = JSONObject.parseArray(data.getString("flow"));
                for(int i =0;i< results.size();i++){
                    Tmkoo.FlowInfo info = new Tmkoo.FlowInfo();
                    info.setDate(results.getJSONObject(i).getString("flowDate"));
                    info.setName(results.getJSONObject(i).getString("flowName"));
                    flowInfos.add(info);
                }
                flow.setFlowInfos(flowInfos);
            }
        }
        return flow;
    }

    public static Tmkoo.Result search(String host, String keyword) throws UnsupportedEncodingException {
        Tmkoo.Result res = new Tmkoo.Result();
        List<Tmkoo.RegisterInfo> result = new ArrayList<>();
        List<String> regNoes = new ArrayList<>();
        String encode = URLEncoder.encode(keyword, "UTF-8");
        String url = host + "/search.php?keyword=" + encode + "&apiKey=" + apiKey +
                "&apiPassword=" + apiPassword + "&pageSize=50&pageNo=1&searchType=1";
//        System.out.println(url);
        String resp = HttpRequest.get(url).body();
//        System.out.println(resp);
        log.info("", resp);
        JSONObject data = JSONObject.parseObject(resp);
        if (data != null) {
            String ret = data.getString("ret");
            if ("0".equals(ret)) {
                JSONArray results = JSONObject.parseArray(data.getString("results"));
                for (int i = 0; i < results.size(); i++) {
                    //和搜索的内容一致的
                    if (keyword.equals(results.getJSONObject(i).getString("tmName"))) {
                        Tmkoo.RegisterInfo info = new Tmkoo.RegisterInfo();
                        if ("商标已注册".equals(results.getJSONObject(i).getString("currentStatus"))) {
                            info.setCate(Integer.parseInt(results.getJSONObject(i).getString("intCls")));
                            info.setRegister(true);
                        } else if ("商标无效".equals(results.getJSONObject(i).getString("currentStatus"))) {
//                            info.setCate(Integer.parseInt(results.getJSONObject(i).getString("intCls")));
//                            info.setRegister(false);
                            continue;
                        } else {
                            info.setCate(Integer.parseInt(results.getJSONObject(i).getString("intCls")));
                            info.setRegister(true);
                        }
                        regNoes.add(results.getJSONObject(i).getString("regNo"));
                        result.add(info);
                    }
                }
            }
        }
        log.info("", result);
        System.out.println(result);
        res.setRegisters(result);
        return res;
    }

}