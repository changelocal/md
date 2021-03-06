package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.front.api.Enums.OrderStatusEnums;
import com.md.union.front.api.Enums.OrderTypeEnums;
import com.md.union.front.api.Enums.SaleTypeEnums;
import com.md.union.front.api.config.MinProperties;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.Order;
import com.md.union.front.api.vo.PayInfo;
import com.md.union.front.api.vo.WxMss;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.dto.WxUserDTO;
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
import java.time.LocalDate;
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
        /*if (request.getBrandNo() != null) {
            createOrder(request.getBrandNo());
        }*/
        PayInfo.Order result = new PayInfo.Order();
        log.info("prePay param:{}", JSON.toJSONString(request));
        Map<String, String> ret = minCommon.appletPay(request.getOrderNo());
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
                String orderNo = (String) map.get("out_trade_no");
                OrderDTO.BrandOrderVO brandOrderVO1 = new OrderDTO.BrandOrderVO();
                brandOrderVO1.setOrderNo(orderNo);
                brandOrderVO1.setPageIndex(1);
                brandOrderVO1.setPageSize(10);
                //查询订单
                BaseResponse<OrderDTO.QueryResp> query = orderClient.query(brandOrderVO1);
                int key = 0;
                long userid = 0;
                String productName = "";
                if(!CollectionUtils.isEmpty(query.getResult().getItems())){
                    key = query.getResult().getItems().get(0).getId();
                    userid = query.getResult().getItems().get(0).getUserId();
                    productName = query.getResult().getItems().get(0).getProductName();
                }
                if(query.getResult().getItems().get(0).getStatus()==OrderStatusEnums.PRE_PAY.getType()){
                    OrderDTO.BrandOrderVO brandOrderVO = new OrderDTO.BrandOrderVO();
                    brandOrderVO.setId(key);
                    brandOrderVO.setOrderNo(orderNo);
                    brandOrderVO.setStatus(OrderStatusEnums.PRE_SUB.getType());
                    brandOrderVO.setPreTime(new Date());
                    brandOrderVO.setOverTime(new Date());
                    //更新订单
                    BaseResponse update = orderClient.update(brandOrderVO);
                    if (!update.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                        throw new ServiceException(update.getStatus(), update.getMessage());
                    }
                    //更新商标状态

                    TrademarkDTO.MdBrand mdBrand = new TrademarkDTO.MdBrand();
                    mdBrand.setId(query.getResult().getItems().get(0).getProductNo());
                    mdBrand.setIsSale(SaleTypeEnums.saled.getType());
                    BaseResponse<TrademarkDTO.Resp> update1 = frontClient.update(mdBrand);
                    if (!update1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                        throw new ServiceException(update1.getStatus(), update1.getMessage());
                    }

                    //获得用户openid，推送通知
                    WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
                    adminUser.setId((int)userid);
                    BaseResponse<WxUserDTO.QueryResp> queryRespBaseResponse = frontClient.query(adminUser);
                    WxMss.MakeOrder makeOrder = new WxMss.MakeOrder();
                    makeOrder.setOpenid(queryRespBaseResponse.getResult().getItems().get(0).getMinId());
                    makeOrder.setOrderNo(orderNo);
                    makeOrder.setOrderStatus(OrderStatusEnums.PRE_SUB.getTitle());
                    makeOrder.setName(productName);
                    makeOrder.setOrderTime(LocalDate.now().toString());
                    makeOrder.setNote("请上传办理资料，确保办理流程顺利进行。");
                    minCommon.pushMakeOrder(makeOrder);
                }
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