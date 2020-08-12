package com.md.union.front.api.controller;

import com.arc.util.wechat.WechatProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(WechatProperties.class)
@RequestMapping("/front/pay")
@Api(tags = {"支付"})
@Slf4j
public class PayController {

//    private final WechatProperties properties = wechatProperties;
//
//    /**
//     * 支付
//     * @param httpServletRequest
//     * @param httpServletResponse
//     * @param openid
//     * @param orderId   订单id(可以在这个接口里下订单,不用另写接口,这里是另写的)
//     * @param writer
//     * @param model
//     * @return
//     */
//    public String appletPay(HttpServletRequest httpServletRequest, HttpServletResponse
//            httpServletResponse, String openid, String orderId, Writer writer, Model
//                                    model) {
//        String mapStr = "";
//        try {
//        //**通过订单id可以拿到订单信息**
//            //获得openid调用微信统一下单接口
//            HashMap<String, String> dataMap = new HashMap<>();
//            dataMap.put("appid", wxAppletAppId); //公众账号ID
//            dataMap.put("mch_id", mchId); //商户号
//            dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串，长度要求在32位以内。
//            dataMap.put("body", "手机"); //商品描述,通过订单id获得
//            dataMap.put("out_trade_no", orderId); //商品订单号,用户下订单后台生成
//            dataMap.put("total_fee", "1"); //商品金,通过订单id获得
//            dataMap.put("spbill_create_ip", HttpUtil.getIpAddress(httpServletRequest)); //客户端ip
//            //通知地址(需要是外网可以访问的)
//            dataMap.put("notify_url", "https://127.0.0.1:8080/weixinAppletpay/notifyUrl");
//            dataMap.put("trade_type", "JSAPI"); //交易类型
//            dataMap.put("openid", openid); //商户号
//            //生成签名
//            String signature = WXPayUtil.generateSignature(dataMap, weixinKey);
//            dataMap.put("sign", signature);//签名
//            //将类型为map的参数转换为xml
//            String requestXml = WXPayUtil.mapToXml(dataMap);
//            //发送参数,调用微信统一下单接口,返回xml
//            String responseXml = HttpUtil.doPost(unifiedorderUrl, requestXml);
//            Map<String, String> responseMap = WXPayUtil.xmlToMap(responseXml);
//            if ("FAIL".equals(responseMap.get("return_code"))) {
//                mapStr = responseMap.get("return_msg");
//                writer.write(mapStr);
//                return "";
//            }
//            if ("FAIL".equals(responseMap.get("result_code"))) {
//                mapStr = responseMap.get("err_code_des");
//                writer.write(mapStr);
//                return "";
//            }
//            if ("".equals(responseMap.get("prepay_id")) || responseMap.get("prepay_id") ==
//                    null) {
//                writer.write("prepay_id 为空");
//                return "";
//            }
//            //成功之后,提取prepay_id,重点就是这个
//            HashMap<String, String> params = new HashMap<>();
//            params.put("appId", wxAppletAppId);
//            params.put("nonceStr", WXPayUtil.generateNonceStr());
//            params.put("package", responseMap.get("prepay_id"));
//            params.put("signType", "MD5");
//            params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
//            //重新签名
//            String paySign = WXPayUtil.generateSignature(params, weixinKey);
//            params.put("paySign", paySign);
//            //传给前端页面
//            //在微信浏览器里面打开H5网页中执行JS调起支付。接口输入输出数据格式为JSON。
//            mapStr = params.toString();
//            //前端接受参数调用wx.requestPayment(OBJECT)发起微信支付
//            //返回requestPayment:ok,支付成功
//        } catch (Exception e) {
//
//        }
//        return mapStr;
//    }
//
//    /**
//     * 异步回调(必须有,得发布到外网)
//     *
//     * @param unifiedorderUrl
//     * @param requestXml
//     * @return
//     */
//    @RequestMapping("/notifyUrl")
//    public String notifyUrl(String unifiedorderUrl, String requestXml) {
//        System.out.print("进入支付h5回调=====================");
//
//        //判断接受到的result_code是不是SUCCESS,如果是,则返回成功,具体业务具体分析,修改订单状态
//
//        // 通知微信.异步确认成功.必写.不然会一直通知后台.
//        String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" +
//                "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//        return resXml; //或者 return "success";
//    }


}