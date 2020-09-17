package com.arc.util.express;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arc.util.http.HttpRequest;
import com.arc.util.lang.EncryptUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class ExpressKd100Common {

    public static final String customer = "5CDFBF42EE998FBA68497F1CBEED3A2C";
    public static final String key = "jaQVZjMN9380";
//    public static final String url = AppConfig.getDefault().getCustom().getString("route.host") + "/kuaidi100" + "/autonumber/auto?";
    public static final String url = "http://www.kuaidi100.com/autonumber/auto?";
//    public static final String traceUrl = AppConfig.getDefault().getCustom().getString("route.host") + "/kuaidi100query" + "/poll/query.do";
    public static final String traceUrl = "https://poll.kuaidi100.com/poll/query.do";


    public static void main(String[] args) {

        List<Express.CompanyInfo> e = ExpressKd100Common.getExpressByCode("1000960921839");
        System.out.println(e);

        Express.CompanyInfo trace = ExpressKd100Common.getTrace(getPostData4Trace("1000960921839",e.get(0)));
        System.out.println(trace);
    }
    /**
     * 根据运单号查询快递公司信息
     *
     * @param logisticCode
     * @return
     */
    public static List<Express.CompanyInfo> getExpressByCode(String logisticCode) {
        List<Express.CompanyInfo> result = new ArrayList<>();
        log.info("url:{}", url);
        String postUrl = url+ "num="+logisticCode +"&key="+key;
        String resp = HttpRequest.post(postUrl).body();
        System.out.println(resp);
        log.info(resp);
        JSONArray data = JSONObject.parseArray(resp);
        if (data != null) {
            if (!CollectionUtils.isEmpty(data)) {
                Express.CompanyInfo item = new Express.CompanyInfo();
                String comCode =  data.getJSONObject(0).getString("comCode");
                if (!Strings.isNullOrEmpty(comCode)) {
                    item.setCompanyName(comCode);
                    result.add(item);
                }
            }
        }
        return result;
    }

    /**
     * 物流轨迹查询参数准备
     *
     * @param logisticCode
     * @return
     * @throws UnsupportedEncodingException
     */
    private static Express.Kd100Query getPostData(String logisticCode, Express.CompanyInfo info) {
        Express.Kd100Query query= new Express.Kd100Query();
        query.setCustomer(customer);
        Express.Param para = new Express.Param();
        para.setCom(info.getCompanyName());
        para.setNum(logisticCode);
        query.setParam(para);
        String sign = EncryptUtil.md5(para.toString() + key + customer);
        query.setSign(sign);
        return query;
    }
    private static String getPostData4Trace(String logisticCode,Express.CompanyInfo info) {
        StringBuilder sb = new StringBuilder();
        String data = "{'com':'" + info.getCompanyName() + "'";
        data += ",'num':'" + logisticCode + "'";
        data += ",'phone':'" + "" + "'";
        data += ",'from':'" + "" + "'";
        data += ",'to':'" + "" + "'";
        data += ",'resultv2':'" + "0" + "'}";
        String sign = EncryptUtil.md5(data  + key + customer);

        sb.append("customer=" +customer)
          .append("&sign=" + sign.toUpperCase())
          .append("&param=" + data);

        return sb.toString();
    }
    //物流轨迹查询
    public static Express.CompanyInfo getTrace(String query) {
        Express.CompanyInfo info = new Express.CompanyInfo();
//        JSONObject.toJSONString(query);
        log.info("{},{}", traceUrl, query);
        String resp = HttpRequest.post(traceUrl).send(query)
                .body();
        log.info("物流轨迹信息===>{}", resp);
        JSONObject result = JSONObject.parseObject(resp);

        if(!"ok".equals(result.getString("message"))
                || !"0".equals(result.getString("state"))
        || !"200".equals(result.getString("status"))){
            return null;
        }

        info.setWaybillNo(result.getString("nu"));

        JSONArray data = result.getJSONArray("data");
        List<Express.TraceInfo> traceInfos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            for(int i=0; i< data.size();i++){
                Express.TraceInfo traceInfo = new Express.TraceInfo();
                traceInfo.setAcceptStation(data.getJSONObject(i).getString("context"));
                traceInfo.setAcceptTime(data.getJSONObject(i).getString("time"));
                traceInfos.add(traceInfo);
            }
        }
        info.setTraceList(traceInfos);
        return info;
    }

}