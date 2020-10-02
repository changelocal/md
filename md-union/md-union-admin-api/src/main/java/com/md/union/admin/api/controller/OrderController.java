package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.admin.api.Enums.OrderStatusEnums;
import com.md.union.admin.api.Enums.OrderTypeEnums;
import com.md.union.admin.api.Enums.SaleTypeEnums;
import com.md.union.admin.api.Enums.UploadPicEnums;
import com.md.union.admin.api.vo.Order;
import com.md.union.admin.api.vo.Ref;
import com.md.union.front.client.dto.*;
import com.md.union.front.client.feign.FrontClient;
import com.md.union.front.client.feign.OrderClient;
import com.md.union.front.client.feign.OrderRefClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/order")
@Api(tags = {"管理人员"})
public class OrderController {
    @Autowired
    private FrontClient frontClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private OrderRefClient orderRefClient;

    public static Date dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        try {
            oldDate= oldDate.replace("Z", " UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        return df2.format(date1);
        return date1;
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Order.ListRes query(@RequestBody Order.SearchReq request) {
        Order.ListRes ret = new Order.ListRes();
        OrderDTO.BrandOrderVO param = new OrderDTO.BrandOrderVO();
        BeanUtils.copyProperties(request, param);

        if(request.getDateRange().length>0
                && !Strings.isNullOrEmpty(request.getDateRange()[0])
                && !Strings.isNullOrEmpty(request.getDateRange()[1])){
            param.setCreateTimeBegin(dealDateFormat(request.getDateRange()[0]));
            param.setCreateTimeEnd(dealDateFormat(request.getDateRange()[1]));
        }else{
            param.setCreateTimeBegin(null);
            param.setCreateTimeEnd(null);
        }
        if(!Strings.isNullOrEmpty(request.getUserMobile())){
            WxUserDTO.WxUser adminUserPara = new WxUserDTO.WxUser();
            adminUserPara.setPageIndex(1);
            adminUserPara.setPageSize(20);
            adminUserPara.setMobile(request.getUserMobile());
            BaseResponse<WxUserDTO.QueryResp> queryWx = frontClient.query(adminUserPara);
            if (!queryWx.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                throw new ServiceException(queryWx.getStatus(), queryWx.getMessage());
            }
            if(!CollectionUtils.isEmpty(queryWx.getResult().getItems())){
                param.setUserId(queryWx.getResult().getItems().get(0).getId());
            }
        }

        //买家map
        WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
        adminUser.setPageIndex(1);
        adminUser.setPageSize(20);
        BaseResponse<WxUserDTO.QueryResp> queryWx = frontClient.query(adminUser);
        if (!queryWx.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(queryWx.getStatus(), queryWx.getMessage());
        }
        Map<Long, String> collect = queryWx.getResult().getItems().stream().filter(p->p.getMobile()!=null).collect(Collectors.toMap(WxUserDTO.WxUser::getId, WxUserDTO.WxUser::getMobile));
        //销售map
        AdminUserDTO.AdminUser admin = new AdminUserDTO.AdminUser();
        admin.setPageSize(20);
        admin.setPageIndex(1);
        BaseResponse<AdminUserDTO.QueryResp> queryAdmin = frontClient.query(admin);
        if (!queryAdmin.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(queryAdmin.getStatus(), queryAdmin.getMessage());
        }
        Map<Integer, String> collectAdmin = queryAdmin.getResult().getAdminUsers().stream().filter(p->p.getNickname()!=null).collect(Collectors.toMap(AdminUserDTO.AdminUser::getId, AdminUserDTO.AdminUser::getNickname));


        BaseResponse<OrderDTO.QueryResp> query = orderClient.query(param);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getItems())){
            List<Order.OrderRes> infos = new ArrayList<>();
            query.getResult().getItems().forEach(p->{
                Order.OrderRes info = new Order.OrderRes();
                BeanUtils.copyProperties(p, info);
                info.setStatusName(OrderStatusEnums.valueType(info.getStatus()).getTitle());
                info.setOrderTypeName(OrderTypeEnums.valueType(info.getOrderType()).getTitle());
                info.setUserMobile(collect.get(p.getUserId()));
                info.setOpUserName(collectAdmin.get(p.getOpUserId()));
                infos.add(info);
            });

            ret.setList(infos);
            ret.setTotal(query.getResult().getTotalCount());
        }else{
            ret.setTotal(0);
        }
        return ret;
    }
    @ApiOperation("更新订单")
    @PostMapping("/update")
    public void update(@RequestBody Order.OrderRes request) {
        OrderDTO.BrandOrderVO adminUser = new OrderDTO.BrandOrderVO();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<OrderDTO.Resp> query = orderClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        //如果是商标订单无效，释放商标
        if(adminUser.getStatus() == OrderStatusEnums.DISABLE.getType()
        && adminUser.getOrderType() == OrderTypeEnums.BRAND_BUY.getType()){
            //更新商标状态
            TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
            req.setPageSize(10);
            req.setPageIndex(1);
            req.setBrandId(adminUser.getProductNo());
            BaseResponse<TrademarkDTO.QueryResp> query2 = frontClient.search(req);
            if (!query2.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                throw new ServiceException(query2.getStatus(), query2.getMessage());
            }

            TrademarkDTO.MdBrand adminUser2 = new TrademarkDTO.MdBrand();
            adminUser2.setId(query2.getResult().getMdBrands().get(0).getId());
            adminUser2.setIsSale(SaleTypeEnums.not_sale.getType());
            BaseResponse<TrademarkDTO.Resp> query1 = frontClient.update(adminUser2);
            if (!query1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                throw new ServiceException(query1.getStatus(), query1.getMessage());
            }
        }
    }
    @ApiOperation("获得订单相关图片")
    @PostMapping("/ref")
    public Ref.QueryResp ref(@RequestBody String  orderId) {
        Ref.QueryResp ret = new Ref.QueryResp();
        RefDTO.OrderRefFile orderRefFile = new RefDTO.OrderRefFile();
        orderRefFile.setOrderNo(orderId);
        orderRefFile.setDel(0);
        BaseResponse<List<RefDTO.OrderRefFile>> byCondition = orderRefClient.findByCondition(orderRefFile);
        if (!byCondition.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(byCondition.getStatus(), byCondition.getMessage());
        }
        if(!CollectionUtils.isEmpty(byCondition.getResult())){
            List<Ref.OrderRefFile> orderRefFiles = new ArrayList<>();
            byCondition.getResult().forEach(p->{
                Ref.OrderRefFile ref = new Ref.OrderRefFile();
                BeanUtils.copyProperties(p, ref);
                ref.setFileTypeName(UploadPicEnums.valueType(ref.getFileType()).name());
                orderRefFiles.add(ref);
            });
            ret.setOrderRefFiles(orderRefFiles);
            ret.setTotal(byCondition.getResult().size());
        }else{
            ret.setTotal(0);
            ret.setOrderRefFiles(new ArrayList<>());
        }
        return ret;
    }

    @ApiOperation("推送服务订单")
    @PostMapping("/service/push")
    public void createServiceOrder(@RequestBody Order.SubmitServiceOrder serviceOrder) {
        OrderDTO.BrandOrderVO order = convert(serviceOrder);
        BaseResponse response = orderClient.add(order);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(response.getStatus())) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
    }
    private OrderDTO.BrandOrderVO convert(Order.SubmitServiceOrder serviceOrder) {
        OrderDTO.BrandOrderVO result = new OrderDTO.BrandOrderVO();
        result.setOrderNo("" + System.currentTimeMillis());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setPrePay(serviceOrder.getTotalPay());
        result.setRestPay(0);
        result.setTotalPay(serviceOrder.getTotalPay());
        result.setUserId(serviceOrder.getUserId());
        result.setOpUserId(1);//todo
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());
        result.setOrderType(serviceOrder.getOrderType());
        result.setProductNo(serviceOrder.getProductNo());
        result.setMinPrice(serviceOrder.getTotalPay());
        result.setMaxPrice(serviceOrder.getTotalPay());
        if (Strings.isNullOrEmpty(serviceOrder.getProductNo())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标服务主键不能为空");
        }
        BaseResponse<ServiceDTO.Service> serviceResp = frontClient.getService(serviceOrder.getProductNo());
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(serviceResp.getStatus())) {
            throw new ServiceException(serviceResp.getStatus(), serviceResp.getMessage());
        }
        result.setProductName(serviceResp.getResult().getServiceName());
        result.setCategoryName(serviceResp.getResult().getServiceName());
        result.setImg(serviceResp.getResult().getImageUrl());

        return result;
    }

    @ApiOperation("推送订单")
    @PostMapping("/push")
    public void createOrder(@RequestBody Order.SubmitOrder serviceOrder) {
        OrderDTO.BrandOrderVO order = convertOrder(serviceOrder);
        BaseResponse response = orderClient.add(order);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(response.getStatus())) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        //更新商标状态
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setPageSize(10);
        req.setPageIndex(1);
        req.setBrandId(serviceOrder.getProductNo());
        BaseResponse<TrademarkDTO.QueryResp> query = frontClient.search(req);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

        TrademarkDTO.MdBrand adminUser = new TrademarkDTO.MdBrand();
        adminUser.setId(query.getResult().getMdBrands().get(0).getId());
        adminUser.setIsSale(SaleTypeEnums.locked.getType());
        BaseResponse<TrademarkDTO.Resp> query1 = frontClient.update(adminUser);
        if (!query1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query1.getStatus(), query1.getMessage());
        }
    }
    private OrderDTO.BrandOrderVO convertOrder(Order.SubmitOrder serviceOrder) {
        OrderDTO.BrandOrderVO result = new OrderDTO.BrandOrderVO();
        WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
        adminUser.setPageSize(10);
        adminUser.setPageIndex(1);
        adminUser.setMinId(serviceOrder.getUserId());
        BaseResponse<WxUserDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(CollectionUtils.isEmpty(query.getResult().getItems())){
            throw new ServiceException(query.getStatus(), "客户不存在");
        }

        result.setOrderNo("" + System.currentTimeMillis());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setPrePay(serviceOrder.getPrePay());
        result.setRestPay(serviceOrder.getTotalPay()-serviceOrder.getPrePay());
        result.setTotalPay(serviceOrder.getTotalPay());
        result.setUserId(query.getResult().getItems().get(0).getId());
        result.setOpUserId(1);//todo
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());
        result.setOrderType(OrderTypeEnums.BRAND_REGISTER.getType());
        result.setProductNo(serviceOrder.getProductNo());
        result.setMinPrice(serviceOrder.getTotalPay());
        result.setMaxPrice(serviceOrder.getTotalPay());
        if (Strings.isNullOrEmpty(serviceOrder.getProductNo())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商品主键不能为空");
        }

        result.setProductName(serviceOrder.getProductName());
        result.setProductNo(serviceOrder.getProductNo());
        result.setCategoryName(serviceOrder.getCategoryName());
        result.setCategory(serviceOrder.getCategory());
        result.setImg(serviceOrder.getImg());
        return result;
    }




}