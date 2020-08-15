package com.md.union.front.api.controller;

import com.arc.util.MD5.MD5Util;
import com.arc.util.http.HttpRequest;
import com.md.union.front.api.config.MinProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

@RestController
@EnableConfigurationProperties(MinProperties.class)
@RequestMapping("/front/pay")
@Api(tags = {"支付"})
@Slf4j
public class PayController {

    private final MinProperties properties;

    public PayController(MinProperties properties) {
        this.properties = properties;
    }

    /**
     * 支付

     * @return
     */
    @PostMapping("/make")
    public String appletPay(@RequestBody String orderId) {
        String mapStr = "";
        String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
        //**通过订单id可以拿到订单信息**
            //获得openid调用微信统一下单接口
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("appid", properties.getMinAppId());
            //公众账号ID
            dataMap.put("mch_id", properties.getMchId());
            //商户号
            dataMap.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
            //随机字符串，长度要求在32位以内。
            dataMap.put("body", "商标");
            //商品描述,通过订单id获得
            dataMap.put("out_trade_no", "1231239892747972347");
            //商品订单号,用户下订单后台生成
            dataMap.put("total_fee", "0.1");
            //商品金,通过订单id获得
            dataMap.put("spbill_create_ip", "");
            //客户端ip
            //通知地址(需要是外网可以访问的)
            dataMap.put("notify_url", "https://pay.mdlogo.cn/front/pay/notifyUrl");
            dataMap.put("trade_type", "JSAPI");
            //交易类型
//            dataMap.put("openid", openid);
            dataMap.put("openid", "");
            //商户号
            //生成签名
//            String signature = WXPayUtil.generateSignature(dataMap, weixinKey);
            dataMap.put("sign", createSign(dataMap,properties.getMinSecret()));
            //签名
            //将类型为map的参数转换为xml
            String requestXml = mapToXml(dataMap);
            //发送参数,调用微信统一下单接口,返回xml
            String responseXml = HttpRequest.post(unifiedorderUrl).body( requestXml);
            Map<String, String> responseMap = getMapFromXML(responseXml);
            if ("FAIL".equals(responseMap.get("return_code"))) {
                mapStr = responseMap.get("return_msg");
                log.error(mapStr);
                return "";
            }
            if ("FAIL".equals(responseMap.get("result_code"))) {
                mapStr = responseMap.get("err_code_des");
                log.error(mapStr);
                return "";
            }
            if ("".equals(responseMap.get("prepay_id")) || responseMap.get("prepay_id") ==
                    null) {
                log.error("prepay_id 为空");
                return "";
            }
            //成功之后,提取prepay_id,重点就是这个
            HashMap<String, String> params = new HashMap<>();
            params.put("appId", properties.getMinAppId());
            params.put("nonceStr",UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
            params.put("package", responseMap.get("prepay_id"));
            params.put("signType", "MD5");
            params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
            //重新签名
//            String paySign = WXPayUtil.generateSignature(params, weixinKey);
            params.put("paySign", createSign(params,properties.getMinSecret()));
            //传给前端页面
            //在微信浏览器里面打开H5网页中执行JS调起支付。接口输入输出数据格式为JSON。
            mapStr = params.toString();
            //前端接受参数调用wx.requestPayment(OBJECT)发起微信支付
            //返回requestPayment:ok,支付成功
        } catch (Exception e) {

        }
        return mapStr;
    }
    /**
     * 创建签名Sign
     *
     * @param key
     * @param parameters
     * @return
     */
    public static String createSign(HashMap<String,String> parameters, String key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator<?> it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            if(entry.getValue() != null || !"".equals(entry.getValue())) {
                String v = String.valueOf(entry.getValue());
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
        sb.append("key=" + key);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * Map转换为 Xml
     *
     * @param map
     * @return Xml
     * @throws Exception
     */
    public static String mapToXml(HashMap<String, String> map) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //防止XXE攻击
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: map.keySet()) {
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
        }
        catch (Exception ex) {
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





        /**
         * 异步回调(必须有,得发布到外网)
         *
         * @param request
         * @return
         */
    @RequestMapping("/notifyUrl")
    public void xcxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream =  request.getInputStream();
        //获取请求输入流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        outputStream.close();
        inputStream.close();
        Map<String,String> map = getMapFromXML(new String(outputStream.toByteArray(),"utf-8"));
        log.info("【小程序支付回调】 回调数据： \n"+map);
        String resXml = "";
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equalsIgnoreCase(returnCode)) {
            String returnmsg = (String) map.get("result_code");
            if("SUCCESS".equals(returnmsg)){
                //更新数据
//                int result = paymentService.xcxNotify(map);
//                if(result > 0){
                    //支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>"+"</xml>";
//                }
            }else{
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                log.info("支付失败:"+resXml);
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
            log.info("【订单支付失败】");
        }

        log.info("【小程序支付回调响应】 响应内容：\n"+resXml);
        response.getWriter().print(resXml);
    }


}