package com.md.union.order.service.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.atom.core.dao.BrandOrderDao;
import com.atom.core.model.BrandOrder;
import com.google.common.base.Strings;
import com.md.union.order.service.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/brand/order")
public class BrandOrderController {

    @Autowired
    private BrandOrderDao brandOrderDao;


    @PostMapping("/add")
    public void add(@RequestBody OrderVO.BrandOrderVO request) {
        log.info("com.md.union.order.service.controller.BrandOrderController.add param:{}", JSON.toJSONString(request));
        checkAddOrder(request);
        brandOrderDao.add(convertAdd(request));
        log.info("com.md.union.order.service.controller.BrandOrderController.add success orderNo:{}", request.getOrderNo());
    }

    @PostMapping("/update")
    public void update(@RequestBody OrderVO.BrandOrderVO request) {

    }

    private BrandOrder convertAdd(OrderVO.BrandOrderVO request) {
        BrandOrder result = new BrandOrder();

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
