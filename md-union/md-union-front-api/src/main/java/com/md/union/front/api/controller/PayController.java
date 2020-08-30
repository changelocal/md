package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.md.union.front.api.config.MinProperties;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.Order;
import com.md.union.front.api.vo.PayInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@RestController
@EnableConfigurationProperties(MinProperties.class)
@RequestMapping("/front/pay")
@Api(tags = {"支付"})
@Slf4j
public class PayController {

    @Autowired
    private MinCommon minCommon;
    /**
     * 支付
     *
     * @return
     */
    @PostMapping("/order")
    public PayInfo.Order payOrder(@RequestBody Order.PayParam request) {
        PayInfo.Order result = new PayInfo.Order();
        log.info("prePay param:{}", JSON.toJSONString(request));
        Map<String,String> ret = minCommon.appletPay();
        for(String key : ret.keySet()){
            if("".equals(key)){
                result.setAppId(ret.get(key));
            }
            else if("nonceStr".equals(key)){
                result.setNonceStr(ret.get(key));
            }
            else if("prePayId".equals(key)){
                result.setPrePayId(ret.get(key));
            }
            else if("signType".equals(key)){
                result.setSignType(ret.get(key));
            }
            else if("timeStamp".equals(key)){
                result.setTimeStamp(ret.get(key));
            }
            else {
                result.setPaySign(ret.get(key));
            }
        }
        log.info("prePay result:{}", result);
        return result;
    }


    /**
     * 异步回调(必须有,得发布到外网)
     *
     * @param request
     * @return
     */
    @RequestMapping("/notifyUrl")
    public void xcxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = request.getInputStream();
        //获取请求输入流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        Map<String, String> map = minCommon.getMapFromXML(new String(outputStream.toByteArray(), "utf-8"));
        log.info("【小程序支付回调】 回调数据： \n" + map);
        String resXml = "";
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equalsIgnoreCase(returnCode)) {
            String returnmsg = (String) map.get("result_code");
            if ("SUCCESS".equals(returnmsg)) {
                //更新数据
//                int result = paymentService.xcxNotify(map);
//                if(result > 0){
                //支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
//                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
                log.info("支付失败:" + resXml);
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]></return_msg>" + "</xml>";
            log.info("【订单支付失败】");
        }

        log.info("【小程序支付回调响应】 响应内容：\n" + resXml);
        response.getWriter().print(resXml);
    }


}