package com.md.union.front.api.controller;

import com.arc.util.auth.AppUserPrincipal;
import com.md.union.front.api.Enums.OrderStatusEnums;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.api.vo.Order;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.feign.OrderClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/front/order")
@Api(tags = {"订单管理服务"})
public class OrderController {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private MinCommon minCommon;

    @ApiOperation("我的订单列表")
    @PostMapping("list")
    public Order.ListRes list(@RequestBody Order.SearchReq request) {
        String aa = AppUserPrincipal.getPrincipal().getAppId();
        Order.ListRes result = new Order.ListRes();
        result.setList(getOrder());
        result.setTotal(10);
        return result;
    }

    @ApiOperation("提交订单")
    @PostMapping("submit")
    public void submitOrder(@RequestBody Order.SubmitOrder request) {
        OrderDTO.BrandOrderVO order = convert(request);
        orderClient.add(order);
    }

    @ApiOperation("我的订单提交资料")
    @PostMapping("submit/file")
    public void submitOrderFile(@RequestBody Order.SubmitOrder request) {

    }

    @ApiOperation("我的商品订单详情")
    @GetMapping("/detail/{id}")
    public Order.Detail OrderDtail(@PathVariable("id") int id) {
        Order.Detail result = new Order.Detail();
        result.setId(1);
        result.setUserId(10);
        result.setOrderNo("348484434");
        result.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595652198157&di=96ccdca4a57760b03b43489df7ce953a&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170602%2F5402bd3037b44135a7362532ae67e2b7_th.png");
        result.setPerson(getPerson());
        result.setCategoryName("名典");
        result.setPayPrice("100.00");
        result.setPrePrice("100.00");
        result.setOrderStatus(1);
        result.setCreateTime("2020-01-01");
        result.setOverTime("2020-01-02");
        result.setTotalPrice("2000");
        result.setCategroyList(getlist());
        return result;

    }

    @ApiOperation("我的服务订单详情")
    @GetMapping("/deal/detail/{id}")
    public Order.Detail OrderDealDtail(@PathVariable("id") int id) {
        Order.Detail result = new Order.Detail();
        result = getOrderDetail(id);
        return result;

    }

    private List<Order.OrderRes> getOrder() {
        List<Order.OrderRes> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Order.OrderRes item = new Order.OrderRes();
            item.setId(i + 1);
            item.setOrderNo("BR5943838" + i);
            item.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595652198157&di=96ccdca4a57760b03b43489df7ce953a&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170602%2F5402bd3037b44135a7362532ae67e2b7_th.png");
            item.setImgNo("rer2324");
            item.setBrandName("商标订单" + i);
            item.setCategoryName("学而思");
            item.setMaxPrice("￥" + 3000);
            item.setMinPrice("￥" + 1000);
            item.setPrePrice("￥" + 10);
            item.setOrderStatus(1);
            result.add(item);
        }
        for (int i = 3; i < 10; i++) {
            Order.OrderRes item = new Order.OrderRes();
            item.setId(i + 1);
            item.setOrderNo("BR5943838" + i);
            item.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595652523529&di=17f6912c66cdd33b2feec7fc7394ab4a&imgtype=0&src=http%3A%2F%2Fimg1.cache.netease.com%2Fcatchpic%2FC%2FCD%2FCDE2DE878F89F6244CEF5F6894EFA642.jpeg");
            item.setImgNo("hddgrt55");
            item.setBrandName("五菱汽车" + i);
            item.setCategoryName("汽车");
            item.setMaxPrice("￥" + 2000);
            item.setMinPrice("￥" + 1000);
            item.setPrePrice("￥" + 10);
            item.setOrderStatus(5);
            result.add(item);
        }
        return result;
    }

    private Order.Detail getOrderDetail(int id) {
        Order.Detail result = new Order.Detail();
        result.setId(id);
        result.setUserId(id);
        result.setOrderNo("BR854nnjj" + id);
        result.setImgUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595652523529&di=17f6912c66cdd33b2feec7fc7394ab4a&imgtype=0&src=http%3A%2F%2Fimg1.cache.netease.com%2Fcatchpic%2FC%2FCD%2FCDE2DE878F89F6244CEF5F6894EFA642.jpeg");
        result.setCategoryName("汽车");
        result.setPayPrice("1000");
        result.setPrePrice("500");
        result.setOrderStatus(3);
        result.setTotalPrice("2000");
        result.setOverTime("2020-07-01");
        result.setCreateTime("2020-01-01");
        result.setPerson(getPerson());
        result.setCategroyList(getlist());
        return result;
    }

    private Brand.Person getPerson() {
        Brand.Person person = new Brand.Person();
        person.setHeadImg("http://pic.5tu.cn/uploads/allimg/1607/pic_5tu_big_201607221326573826.jpg");
        person.setName("客服1");
        person.setPhone("15688706317");
        person.setQq("571660498");
        return person;
    }

    private List<Category.Info> getlist() {
        List<Category.Info> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category.Info item = new Category.Info();
            item.setCategoryName("汽车" + i);
            item.setCategoryNo("ca5554" + i);
            item.setDesc("五菱汽车");
            item.setIcon("");
            result.add(item);
        }
        return result;
    }

    private OrderDTO.BrandOrderVO convert(Order.SubmitOrder request) {
        OrderDTO.BrandOrderVO result = new OrderDTO.BrandOrderVO();
        result.setOrderNo("" + System.currentTimeMillis());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setPrePay(Integer.parseInt(request.getPrePay()));
        result.setRestPay(Integer.parseInt(request.getRestPay()));
        result.setTotalPay(Integer.parseInt(request.getTotalPay()));
        result.setUserId(1);
        result.setOpUserId(1);
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());


        result.setOrderType(request.getOrderType());
        result.setProductNo(request.getProductNo());
        return result;
    }

}
