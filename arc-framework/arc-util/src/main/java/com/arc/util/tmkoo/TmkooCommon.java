package com.arc.util.tmkoo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arc.util.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@Slf4j
public class TmkooCommon {
    public static final String apiPassword = "P8G3m5dR6Dj";
    public static final String apiKey = "QIJIAN_1819072015";

    public static void main(String[] args) {


//        try {
//            TmkooCommon.search("好利来");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        TmkooCommon.info("9999212", 11);
    }

    /**
     *
     * @param regNo 注册号
     * @param cls  国际分类
     */
    public static void info(String regNo, int cls) {
        String url="http://api.tmkoo.com/info.php?apiKey="+apiKey+"&apiPassword="+apiPassword+"&regNo="+regNo+"&intCls="+cls;
        System.out.println(url);
        String resp = HttpRequest.get(url).body();
        System.out.println(resp);
        log.info(resp);
        JSONObject data = JSONObject.parseObject(resp);
        if (data != null) {
            String msg = data.getString("msg");
            System.out.println(msg);
        }
    }
    public static void search(String keyword) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(keyword, "UTF-8");
        String url="http://api.tmkoo.com/search.php?keyword="+encode+"&apiKey="+apiKey +
                "&apiPassword="+apiPassword+"&pageSize=50&pageNo=1&searchType=4";
        System.out.println(url);
        String resp = HttpRequest.get(url).body();
        System.out.println(resp);
        log.info(resp);
        JSONArray data = JSONObject.parseArray(resp);
        if (data != null) {
            if (!CollectionUtils.isEmpty(data)) {

            }
        }
    }

}