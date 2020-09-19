package com.md.union.order.service.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.data.PageResult;
import com.arc.util.http.BaseResponse;
import com.atom.core.dao.BrandOrderDao;
import com.atom.core.model.BrandOrder;
import com.atom.core.model.BrandOrderParam;
import com.google.common.base.Strings;
import com.md.union.order.service.enums.OrderStatusEnums;
import com.md.union.order.service.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand/order")
@Api(tags = {"用户订单管理"})
public class BrandOrderController {

    @Autowired
    private BrandOrderDao brandOrderDao;

    @ApiOperation(value = "查询所有用户订单", notes = "查询所有用户订单")
    @PostMapping("/query")
    public OrderVO.QueryResp query(@RequestBody OrderVO.BrandOrderVO request) {
        OrderVO.QueryResp result = new OrderVO.QueryResp();
        List<OrderVO.BrandOrderVO> list = new ArrayList<>();
        PageResult<BrandOrder> pageResult = brandOrderDao.query(convertQuery(request));
        if (!CollectionUtils.isEmpty(pageResult.getItems())) {
            pageResult.getItems().forEach(p -> {
                list.add(convertResp(p));
            });
        }
        result.setTotalCount(pageResult.getTotalCount());
        result.setItems(list);
        return result;
    }

    @PostMapping("/add")
    public int add(@RequestBody OrderVO.BrandOrderVO request) {
        log.info("BrandOrderController.add param:{}", JSON.toJSONString(request));
        checkAddOrder(request);
        int id = brandOrderDao.add(convertAdd(request));
        log.info("BrandOrderController.add success orderNo:{}", request.getOrderNo());
        return id;
    }

    @PostMapping("/update")
    public void update(@RequestBody OrderVO.BrandOrderVO request) {
        brandOrderDao.update(convertUpdate(request));
    }

    @PostMapping("/get/by/condition")
    public OrderVO.BrandOrderVO getByCondition(@RequestBody OrderVO.BrandOrderVO request) {
        BrandOrderParam param = new BrandOrderParam();
        param.setId(request.getId());
        param.setStatus(request.getStatus());
        param.setUserId(request.getUserId());
        param.setOpUserId(request.getOpUserId());
        param.setProductNo(request.getProductNo());
        param.setOpUserId(request.getOpUserId());
        param.setId(request.getId());
        log.info("brandOrderDao.find param:{}", JSON.toJSONString(param));
        List<BrandOrder> list = brandOrderDao.find(param);
        log.info("brandOrderDao.find list:{}", JSON.toJSONString(list));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单不唯一");
        }
        return convertResp(list.get(0));
    }

    private BrandOrder convertAdd(OrderVO.BrandOrderVO request) {
        BrandOrder result = new BrandOrder();
        result.setOrderNo(request.getOrderNo());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setOrderType(request.getOrderType());
        result.setPrePay(request.getPrePay());
        result.setRestPay(request.getRestPay());
        result.setTotalPay(request.getTotalPay());
        result.setUserId(request.getUserId());
        result.setOpUserId(request.getOpUserId());
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());
        result.setPreTime(request.getPreTime());
        result.setOverTime(request.getOverTime());
        result.setProductNo(request.getProductNo());
        result.setProductName(request.getProductName());
        result.setMinPrice(request.getMinPrice());
        result.setMaxPrice(request.getMaxPrice());
        result.setCategory(request.getCategory());
        result.setCategoryName(request.getCategoryName());
        result.setImg(request.getImg());
        return result;
    }

    private BrandOrder convertUpdate(OrderVO.BrandOrderVO request) {
        BrandOrder result = new BrandOrder();
        result.setId(request.getId());
        result.setOrderNo(request.getOrderNo());
        result.setStatus(request.getStatus());
        result.setPrePay(request.getPrePay());
        result.setRestPay(request.getRestPay());
        result.setTotalPay(request.getTotalPay());
        result.setUserId(request.getUserId());
        result.setOpUserId(request.getOpUserId());
        result.setUpdateTime(new Date());
        result.setPreTime(request.getPreTime());
        result.setOverTime(request.getOverTime());
        result.setProductNo(request.getProductNo());
        return result;
    }

    private BrandOrderParam convertQuery(OrderVO.BrandOrderVO request) {
        BrandOrderParam result = new BrandOrderParam();
        result.setId(request.getId());
        result.setOrderType(request.getOrderType());
        result.setOrderNo(request.getOrderNo());
        result.setStatus(request.getStatus());
        result.setUserId(request.getUserId());
        result.setOpUserId(request.getOpUserId());
        result.setProductNo(request.getProductNo());
        result.setPageIndex(request.getPageIndex());
        result.setPageSize(request.getPageSize());
        return result;
    }

    private OrderVO.BrandOrderVO convertResp(BrandOrder request) {
        OrderVO.BrandOrderVO result = new OrderVO.BrandOrderVO();
        result.setId(request.getId());
        result.setOrderNo(request.getOrderNo());
        result.setStatus(request.getStatus());
        result.setOrderType(request.getOrderType());
        result.setPrePay(request.getPrePay());
        result.setRestPay(request.getRestPay());
        result.setTotalPay(request.getTotalPay());
        result.setUserId(request.getUserId());
        result.setOpUserId(request.getOpUserId());
        result.setCreateTime(request.getCreateTime());
        result.setUpdateTime(request.getUpdateTime());
        result.setPreTime(request.getPreTime());
        result.setOverTime(request.getOverTime());
        result.setProductNo(request.getProductNo());
        result.setProductName(request.getProductName());
        result.setCategory(request.getCategory());
        result.setCategoryName(request.getCategoryName());
        result.setMinPrice(request.getMinPrice());
        result.setMaxPrice(request.getMaxPrice());
        result.setImg(request.getImg());
        return result;
    }

    private void checkAddOrder(OrderVO.BrandOrderVO request) {
        if (Strings.isNullOrEmpty(request.getOrderNo())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单编号不能为空");
        }
        if (request.getStatus() <= 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单状态不能为空");
        }
        if (request.getOrderType() <= 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单类型不能为空");
        }
        if (request.getTotalPay() <= 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单类型不能为空");
        }
        if (request.getUserId() <= 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "购买用户id不能为空");
        }
        if (request.getOpUserId() <= 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "运营人员id不能为空");
        }
    }


}
