package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/front/order")
@Api(tags = {"订单管理服务"}, description = "接口负责人：田秀全")
public class OrderController {

    @ApiOperation("我的订单列表")
    @PostMapping("list")
    public Order.ListRes list(@RequestBody Order.SearchReq request) {
        Order.ListRes result = new Order.ListRes();

        return result;
    }


    @ApiOperation("我的订单提交资料")
    @PostMapping("submit")
    public void submitOrder(@RequestBody Order.SubmitOrder request) {

    }

    @ApiOperation("我的订单详细信息")
    @PostMapping("/detail/{id}")
    public Order.Detail OrderDtail(@PathVariable("id")String id) {
        Order.Detail result = new Order.Detail();

        return result;

    }

    @ApiOperation("我的订单详细信息")
    @PostMapping("/deal/detail/{id}")
    public Order.Detail OrderDealDtail(@PathVariable("id")String id) {
        Order.Detail result = new Order.Detail();

        return result;

    }

}
