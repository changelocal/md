package com.md.union.front.api.facade;

import com.alibaba.fastjson.JSONObject;
import com.arc.common.ServiceException;
import com.arc.util.MD5.MD5Util;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.http.BaseResponse;
import com.arc.util.http.HttpRequest;
import com.arc.util.lang.EncryptUtil;
import com.arc.util.lang.FaultException;
import com.md.union.front.api.config.MinProperties;
import com.md.union.front.api.vo.MinUser;
import com.md.union.front.api.vo.TemplateData;
import com.md.union.front.api.vo.WxMss;
import com.md.union.front.api.vo.WxMssVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

@Configuration
@EnableConfigurationProperties(MinProperties.class)
@Slf4j
public class MinCommon {

    private final MinProperties properties;

    public MinCommon(MinProperties properties) {
        this.properties = properties;
    }


    /**
     * 微信小程序登录
     *
     * @param code 小程序客户端获取到的动态code
     * @return
     */
    public MinUser minLogin(String code) {
        String url = properties.getRouteHost() + "/sns/jscode2session?appid=" + properties.getMinAppId() +
                "&secret=" + properties.getMinSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        log.info("minLogin req url :{}", url);
        HttpRequest httpRequest = HttpRequest.get(url);
        String result = httpRequest.body();
        log.info("minLogin result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "登录失败" + resp.getString("errmsg"));
        }
        MinUser minUser = new MinUser();
        minUser.setSessionKey(resp.getString("session_key"));
        minUser.setUnionId(resp.getString("unionid"));
        minUser.setMinId(resp.getString("openid"));
        minUser.setType(2);
        minUser.setSessionId(EncryptUtil.md5("wxlogin" + new Date().getTime() + minUser.getMinId() + minUser.getSessionKey()));

        log.info("minLogin return===>{}", minUser);
        return minUser;
    }

    public void pushToPay(WxMss.ToPay toPay){
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(toPay.getOpenid());//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id("a-Vu8j5AgGljf1hAmvoKIuI5WeL1VwrZtcnDBXC9RqE");//订阅消息模板id
        wxMssVo.setPage("pages/index/index");

        Map<String, TemplateData> m = new HashMap<>(4);
        m.put("thing5", new TemplateData(toPay.getName()));
        m.put("amount2", new TemplateData(toPay.getPayment()));
        m.put("character_string3", new TemplateData(toPay.getOrderNo()));
        m.put("thing4", new TemplateData(toPay.getNote()));
        wxMssVo.setData(m);

        sendMinTip(null);
    }
    public void pushMakeOrder(WxMss.MakeOrder makeOrder){
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(makeOrder.getOpenid());//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id("Q-Ry38-lawOPAnu36powW9R6mySsWMfNf8CKwtL0rQ0");//订阅消息模板id
        wxMssVo.setPage("pages/index/index");

        Map<String, TemplateData> m = new HashMap<>(4);
        m.put("number1", new TemplateData(makeOrder.getOrderNo()));
        m.put("date2", new TemplateData(makeOrder.getOrderTime()));
        m.put("thing3", new TemplateData(makeOrder.getOrderStatus()));
        m.put("thing5", new TemplateData(makeOrder.getName()));
        m.put("thing4", new TemplateData(makeOrder.getNote()));
        wxMssVo.setData(m);

        sendMinTip(null);
    }
    public void pushDelivery(WxMss.Delivery delivery){
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(delivery.getOpenid());//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id("EvFuY2-cvHzY78b2FhdZYNeURAl4ocuT_FzUwUs22Og");//订阅消息模板id
        wxMssVo.setPage("pages/index/index");

        Map<String, TemplateData> m = new HashMap<>(4);
        m.put("thing3", new TemplateData(delivery.getName()));
        m.put("thing1", new TemplateData(delivery.getOrderType()));
        m.put("thing2", new TemplateData(delivery.getTradeType()));
        wxMssVo.setData(m);

        sendMinTip(null);
    }

    public void pushOrderProgress(WxMss.OrderProgress orderProgress){
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(orderProgress.getOpenid());//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id("707M70HS7OtcHjjadGLyRjg-u1FkUE6S05JOjjM8K_4");//订阅消息模板id
        wxMssVo.setPage("pages/index/index");

        Map<String, TemplateData> m = new HashMap<>(4);
        m.put("thing6", new TemplateData(orderProgress.getName()));
        m.put("character_string1", new TemplateData(orderProgress.getOrderId()));
        m.put("phrase2", new TemplateData(orderProgress.getStatus()));
        m.put("date3", new TemplateData(orderProgress.getOperateTime()));
        m.put("thing10", new TemplateData(orderProgress.getNote()));
        wxMssVo.setData(m);

//        JSONObject data = new JSONObject();
//        data.put("touser", "openid");
//        data.put("template_id", ConfigTemplate.To_Pay_TmpId);
//        data.put("page", "pages/insuranceDetail/insuranceDetail?insuranceId=" + "id");
//        data.put("form_id", "formid");
//        JSONObject content = new JSONObject();
//        content.put("keyword1", new BrandController.Template("国内快件丢失保险（快递保）"));
//        content.put("keyword2", new BrandController.Template("iddd"));
//        content.put("keyword3", new BrandController.Template("idddd"));
//        content.put("keyword4", new BrandController.Template("您可登录燕赵财险官网查询理赔进度，服务电话：4000-000-123"));
//        data.put("data", content);

        sendMinTip(null);
    }
    @Data
    public class Template {
        Template(String value) {
            this.value = value;
        }

        private String value;
    }
    /**
     * 发送保单消息
     *
     * @param request
     */
    public void sendMinTip(Object request) {
        String accessToken = getMinAccessToken();
        String url = properties.getRouteHost() + "/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        log.info("sendMinTip url:{} data:{}", url, request);
        String result = HttpRequest.post(url).header("Content-Type", HttpRequest.CONTENT_TYPE_JSON)
                .send(request.toString()).body();
        log.info("sendMinTip result {}", result);
    }

    /**
     * 微信公众号获取token
     *
     * @return 返回token
     */
    public String getMinAccessToken() {
//        String tokenCache = "WXP_TEMPLATE_" + properties.getMinAppId();
//        if (redisClient.exists(tokenCache)) {
//            return redisClient.get(tokenCache);
//        }
        String accessTokenUrl = properties.getRouteHost() + "/cgi-bin/token?grant_type=client_credential&appid=" + properties.getMinAppId() + "&secret=" + properties.getMinSecret();
        log.info("getMinAccessToken req url {}", accessTokenUrl);
        String result = HttpRequest.get(accessTokenUrl).body();
        log.info("common getMinAccessToken result:{}", result);
        JSONObject resp = JSONObject.parseObject(result);
        if (resp.containsKey("errcode") && resp.getIntValue("errcode") != 0) {
            throw new FaultException("获取minAccessToken失败" + resp.getString("errmsg"));
        }
        String accessToken = resp.getString("access_token");
//        redisClient.set(tokenCache, accessToken, (resp.getIntValue("expires_in") - 10));
        return accessToken;
    }


    /**
     * 支付
     *
     * @return
     */
    public Map<String, String> appletPay() {
        String OrderId = "P" + System.currentTimeMillis();
        String openId = AppUserPrincipal.getPrincipal().getMinId();
        String mapStr = "";
        //String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String unifiedorderUrl = properties.getRouteHost() + "/pay/unifiedorder";
        SortedMap<String, String> params = new TreeMap<>();
        try {
            //**通过订单id可以拿到订单信息**
            //获得openid调用微信统一下单接口
            SortedMap<String, String> dataMap = new TreeMap<>();
            dataMap.put("appid", properties.getMinAppId());
            //公众账号ID
            dataMap.put("mch_id", properties.getMchId());
            //商户号
            dataMap.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
            //随机字符串，长度要求在32位以内。
            dataMap.put("body", "商标");
            //商品描述,通过订单id获得
            dataMap.put("out_trade_no", OrderId);
            //商品订单号,用户下订单后台生成
            dataMap.put("total_fee", "1");
            //商品金,通过订单id获得
            dataMap.put("spbill_create_ip", "47.92.65.35");
            //客户端ip
            //通知地址(需要是外网可以访问的)
            dataMap.put("notify_url", "https://pay.mdlogo.cn/front/pay/notifyUrl");
            dataMap.put("trade_type", "JSAPI");
            //交易类型
            dataMap.put("openid", openId);
            //商户号
            //生成签名
            //String signature = WXPayUtil.generateSignature(dataMap, weixinKey);
            dataMap.put("sign", createSign(dataMap, properties.getApiKey()));
            //签名
            //将类型为map的参数转换为xml
            String requestXml = mapToXml(dataMap);
            //发送参数,调用微信统一下单接口,返回xml
            log.info("requestXml :{}", requestXml);
            String responseXml = HttpRequest.post(unifiedorderUrl).send(requestXml.getBytes("UTF-8")).body();
            log.info("pay result :{}", responseXml);
            Map<String, String> responseMap = getMapFromXML(responseXml);
            if ("FAIL".equals(responseMap.get("return_code"))) {
                mapStr = responseMap.get("return_msg");
                log.error(mapStr);
                return null;
            }
            if ("FAIL".equals(responseMap.get("result_code"))) {
                mapStr = responseMap.get("err_code_des");
                log.error(mapStr);
                return null;
            }
            if ("".equals(responseMap.get("prepay_id")) || responseMap.get("prepay_id") ==
                    null) {
                log.error("prepay_id 为空");
                return null;
            }
            //成功之后,提取prepay_id,重点就是这个

            params.put("appId", properties.getMinAppId());
            params.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
            params.put("package","prepay_id="+responseMap.get("prepay_id"));
            params.put("signType", "MD5");
//            params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            params.put("timeStamp", Long.toString(System.currentTimeMillis() / 1000));
            //重新签名
//            String paySign = WXPayUtil.generateSignature(params, weixinKey);
            params.put("paySign", createSign(params, properties.getApiKey()));
            //传给前端页面
            //在微信浏览器里面打开H5网页中执行JS调起支付。接口输入输出数据格式为JSON。
            mapStr = params.toString();
            //前端接受参数调用wx.requestPayment(OBJECT)发起微信支付
            //返回requestPayment:ok,支付成功
        } catch (Exception e) {
            log.info("pay error {}", e);
        }
        return params;
    }

    /**
     * 创建签名Sign
     *
     * @param key
     * @param parameters
     * @return
     */
    public static String createSign(SortedMap<String, String> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            if (entry.getValue() != null || !"".equals(entry.getValue())) {
                String v = String.valueOf(entry.getValue());
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
//        sb.append("key=" + "mtsb20201234567890mtsbmtsbmtsbmt");
        sb.append("key=" + key);
        String text = sb.toString();
        log.info("sb result:{}", text);
        String sign = MD5Util.MD5Encode(text, "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * Map转换为 Xml
     *
     * @param map
     * @return Xml
     * @throws Exception
     */
    public static String mapToXml(SortedMap<String, String> map) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //防止XXE攻击
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * XML转换为Map
     *
     * @param strXML
     * @return Map
     * @throws Exception
     */
    public static Map<String, String> getMapFromXML(String strXML) throws Exception {
        log.info("getMapFromXML:", strXML);
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //防止XXE攻击
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setExpandEntityReferences(false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

}
