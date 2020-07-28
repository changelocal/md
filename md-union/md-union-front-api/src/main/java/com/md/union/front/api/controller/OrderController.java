package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.api.vo.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/order")
@Api(tags = {"订单管理服务"}, description = "接口负责人：田秀全")
public class OrderController {

    @ApiOperation("我的订单列表")
    @PostMapping("list")
    public Order.ListRes list(@RequestBody Order.SearchReq request) {
        Order.ListRes result = new Order.ListRes();
        result.setList(getOrder());
        result.setTotal(10);
        return result;
    }

    @ApiOperation("我的订单提交资料")
    @PostMapping("submit")
    public void submitOrder(@RequestBody Order.SubmitOrder request) {
        //common 里已经有实现，可以拷贝
    }

    @ApiOperation("我的订单详细信息")
    @PostMapping("/detail/{id}")
    public Order.Detail OrderDtail(@PathVariable("id") int id) {
        Order.Detail result = new Order.Detail();

        return result;

    }

    @ApiOperation("我的订单详细信息")
    @PostMapping("/deal/detail/{id}")
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

}
