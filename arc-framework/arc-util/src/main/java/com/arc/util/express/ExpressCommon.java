package com.arc.util.express;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arc.util.http.HttpRequest;
import com.arc.util.lang.EncryptUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class ExpressCommon {

    public static final String EBusinessID = "1529357";
    public static final String apiKey = "aab630ce-8815-4e84-87be-de34fe12c835";
//    public static final String url = AppConfig.getDefault().getCustom().getString("route.host") + "/kuaidiniao" + "/Ebusiness/EbusinessOrderHandle.aspx";
    public static final String url = "";


    /**
     * 根据运单号查询快递公司信息
     *
     * @param logisticCode
     * @return
     */
    public static List<Express.CompanyInfo> getExpressByCode(String logisticCode, String companyNo) {
        List<Express.CompanyInfo> result = new ArrayList<>();
        try {
            log.info("{},{}", url, getPostData(logisticCode));
            String resp = HttpRequest.post(url).send(getPostData(logisticCode))
                                     .body();
            log.info(resp);
            JSONObject data = JSONObject.parseObject(resp);
            if (data.getBoolean("Success")) {
                JSONArray list = data.getJSONArray("Shippers");
                if (list == null) return result;
                for (int i = 0; i < list.size(); i++) {
                    Express.CompanyInfo item = new Express.CompanyInfo();
                    String shiperCode = Strings.isNullOrEmpty(companyNo) ? list.getJSONObject(list.size() - 1).getString("ShipperCode") : companyNo;
                    JSONObject traceInfo = getTrace(logisticCode, shiperCode);
                    if (traceInfo.containsKey("Success") && traceInfo.getBoolean("Success")) {
                        item.setDelivered(traceInfo.getIntValue("State"));
//                        item.setDeliveredDesc(DeliveryStatus.valueOf(item.getDelivered()).displayName());
                        if (traceInfo.containsKey("Traces") && !traceInfo.getJSONArray("Traces").isEmpty()) {
                            List<Express.TraceInfo> traceList = new ArrayList<>();
                            for (int j = 0; j < traceInfo.getJSONArray("Traces").size(); j++) {
                                Express.TraceInfo trace = new Express.TraceInfo();
                                trace.setAcceptStation(traceInfo.getJSONArray("Traces").getJSONObject(j).getString("AcceptStation"));
                                trace.setAcceptTime(traceInfo.getJSONArray("Traces").getJSONObject(j).getString("AcceptTime"));
                                traceList.add(trace);
                            }
                            item.setTraceList(traceList);
                        }
                    } else {
                        item.setDelivered(4);
                        item.setDeliveredDesc("异常快件");
                    }
                    item.setWaybillNo(logisticCode);
                    item.setCompanyNo(list.getJSONObject(i).getString("ShipperCode"));
                    item.setCompanyName(list.getJSONObject(i).getString("ShipperName"));
                    result.add(item);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据单号获得快递公司信息
     *
     * @param logisticCode
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getPostData(String logisticCode) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();

        String data = "{'LogisticCode':'" + logisticCode + "'}";

        String sign = EncryptUtil.base64Encode((EncryptUtil.md5(data + apiKey)).getBytes("UTF-8"));

        sb.append("RequestData=" + URLEncoder.encode(data, "UTF-8"))
                .append("&EBusinessID=" + EBusinessID)
                .append("&RequestType=2002")
                .append("&DataSign=" + URLEncoder.encode(sign, "UTF-8"))
                .append("&DataType=2");

        return sb.toString();
    }

    //物流轨迹查询
    private static JSONObject getTrace(String logisticCode, String shipperCode) {
        JSONObject result = new JSONObject();
        try {
            log.info("{},{}", url, getPostData4Trace(logisticCode, shipperCode));
            String resp = HttpRequest.post(url).send(getPostData4Trace(logisticCode, shipperCode))
                    .body();
            log.info("物流轨迹信息===>{}", resp);
            result = JSONObject.parseObject(resp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得快递物流信息
     *
     * @param logisticCode
     * @param shipperCode
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getPostData4Trace(String logisticCode, String shipperCode) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String data = "{'LogisticCode':'" + logisticCode + "'";
        data += ",'ShipperCode':'" + shipperCode + "'}";
        String sign = EncryptUtil.base64Encode((EncryptUtil.md5(data + apiKey)).getBytes("UTF-8"));

        sb.append("RequestData=" + URLEncoder.encode(data, "UTF-8"))
                .append("&EBusinessID=" + EBusinessID)
                .append("&RequestType=1002")
                .append("&DataSign=" + URLEncoder.encode(sign, "UTF-8"))
                .append("&DataType=2");

        return sb.toString();
    }
}