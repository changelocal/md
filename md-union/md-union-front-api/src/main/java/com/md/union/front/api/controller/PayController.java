package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.front.api.Enums.OrderStatusEnums;
import com.md.union.front.api.Enums.OrderTypeEnums;
import com.md.union.front.api.config.MinProperties;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.Order;
import com.md.union.front.api.vo.PayInfo;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import com.md.union.front.client.feign.OrderClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@RestController
@EnableConfigurationProperties(MinProperties.class)
@RequestMapping("/front/pay")
@Api(tags = {"支付"})
@Slf4j
public class PayController {

    @Autowired
    private MinCommon minCommon;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private FrontClient frontClient;

    /**
     * 支付
     *
     * @return
     */
    @PostMapping("/order")
    public PayInfo.Order payOrder(@RequestBody Order.PayParam request) {
        if (request.getBrandNo() != null) {
            createOrder(request.getBrandNo());
        }
        PayInfo.Order result = new PayInfo.Order();
        log.info("prePay param:{}", JSON.toJSONString(request));
        Map<String, String> ret = minCommon.appletPay();
        for (String key : ret.keySet()) {
            if ("appId".equals(key)) {
                result.setAppId(ret.get(key));
            } else if ("nonceStr".equals(key)) {
                result.setNonceStr(ret.get(key));
            } else if ("package".equals(key)) {
                result.setPrePayId(ret.get(key));
            } else if ("signType".equals(key)) {
                result.setSignType(ret.get(key));
            } else if ("timeStamp".equals(key)) {
                result.setTimeStamp(ret.get(key));
            } else if ("paySign".equals(key)) {
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
        log.info("notifyUrl：" + request);
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

    private void createOrder(String brandNo) {
        OrderDTO.BrandOrderVO order = convert(brandNo);
        BaseResponse response = orderClient.add(order);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(response.getStatus())) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
    }

    private OrderDTO.BrandOrderVO convert(String brandNo) {
        OrderDTO.BrandOrderVO result = new OrderDTO.BrandOrderVO();
        result.setOrderNo("" + System.currentTimeMillis());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setPrePay(1000);
        result.setRestPay(1000);
        result.setTotalPay(2000);
        result.setUserId(AppUserPrincipal.getPrincipal().getId());
        result.setOpUserId(1);
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());


        result.setOrderType(OrderTypeEnums.BRAND_REGISTER.getType());
        result.setProductNo(brandNo);
        result.setMinPrice(10000);
        result.setMinPrice(20000);
        if (Strings.isNullOrEmpty(brandNo)) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标编号不能为空");
        }
        Map<String, TrademarkDTO.MdBrand> brandMap = getBrandByBrandIds(Arrays.asList(brandNo));
        if (!brandMap.containsKey(brandNo))
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标编号不存在");
        TrademarkDTO.MdBrand brand = brandMap.get(brandNo);
        result.setProductName(brand.getBrandName());
        result.setCategory(brand.getCategory());
        result.setCategoryName(brand.getCategoryName());
        result.setImg(brand.getImageUrl());
        return result;
    }

    private Map<String, TrademarkDTO.MdBrand> getBrandByBrandIds(List<String> brandIds) {
        Map<String, TrademarkDTO.MdBrand> result = new HashMap<>();
        if (CollectionUtils.isEmpty(brandIds))
            return result;
        TrademarkDTO.MdBrand findBrand = new TrademarkDTO.MdBrand();
        findBrand.setBrandIds(brandIds);
        BaseResponse<TrademarkDTO.QueryResp> brandResp = frontClient.find(findBrand);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(brandResp.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "批量查询品牌信息失败");
        }
        Map<Integer, TrademarkDTO.Cate> cateMap = getCategoryMap();
        brandResp.getResult().getMdBrands().forEach(p -> {
            if (cateMap.containsKey(p.getCategory())) {
                p.setCategoryName(cateMap.get(p.getCategory()).getCategoryName());
            }
            result.put(p.getBrandId(), p);
        });
        return result;
    }

    private Map<Integer, TrademarkDTO.Cate> getCategoryMap() {
        Map<Integer, TrademarkDTO.Cate> result = new HashMap<>();
        BaseResponse<TrademarkDTO.RootBrandResp> brandCategoryResp = frontClient.root();
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(brandCategoryResp.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "查询45大分类失败");
        }
        brandCategoryResp.getResult().getCates().forEach(p -> result.put(p.getCode(), p));
        return result;
    }


}