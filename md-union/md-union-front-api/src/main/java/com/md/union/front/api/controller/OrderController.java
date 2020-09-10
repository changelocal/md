package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.front.api.Enums.OrderStatusEnums;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.api.vo.Order;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import com.md.union.front.client.feign.OrderClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/order")
@Api(tags = {"订单管理服务"})
@Slf4j
public class OrderController {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private FrontClient frontClient;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @ApiOperation("我的订单列表")
    @PostMapping("list")
    public Order.ListRes list(@RequestBody Order.SearchReq request) {
        Order.ListRes result = new Order.ListRes();
        long userId = AppUserPrincipal.getPrincipal().getId();
        OrderDTO.BrandOrderVO param = new OrderDTO.BrandOrderVO();
        param.setUserId(userId);
        param.setPageIndex(request.getPageIndex() == 0 ? 1 : request.getPageIndex());
        param.setPageSize(request.getPageSize() == 0 ? 10 : request.getPageSize());
        log.info("orderClient.query param:{}", JSON.toJSONString(param));
        BaseResponse<OrderDTO.QueryResp> resp = orderClient.query(param);
        log.info("orderClient.query result:{}", JSON.toJSONString(resp));
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(resp.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "查询订单列表失败");
        }
        result.setList(convertOrder(resp.getResult().getItems()));
        result.setTotal(resp.getResult().getTotalCount());
        return result;
    }

    @ApiOperation("提交订单")
    @PostMapping("submit")
    public void submitOrder(@RequestBody Order.SubmitOrder request) {
        OrderDTO.BrandOrderVO order = convert(request);
        BaseResponse response = orderClient.add(order);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(response.getStatus())) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
    }

    /*@ApiOperation("我的订单提交资料")
    @PostMapping("submit/file")
    public void submitOrderFile(@RequestBody Order.SubmitOrderFile request) {
        if (request.getOrderNo() == null) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单编号不能为空");
        }
        if (request.getSubmitType() == 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "上传文件类型不能为空");
        }
        if (CollectionUtils.isEmpty(request.getFileIds())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "上传文件资料不能为空");
        }
    }*/

    @ApiOperation("我的商品订单详情")
    @GetMapping("/detail/{id}")
    public Order.Detail OrderDtail(@PathVariable("id") int id) {
        Order.Detail result = new Order.Detail();
        OrderDTO.BrandOrderVO request = new OrderDTO.BrandOrderVO();
        request.setId(id);
        log.info("orderClient.getByCondition param:{}", request);
        BaseResponse<OrderDTO.BrandOrderVO> response = orderClient.getByCondition(request);
        log.info("orderClient.getByCondition result:{}", JSON.toJSONString(response));
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(response.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "查询订单详情失败");
        }
        if (response.getResult() == null) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "查询订单id不存在");
        }
        OrderDTO.BrandOrderVO order = response.getResult();
        result.setId(order.getId());
        result.setUserId(order.getUserId());
        result.setOrderNo(order.getOrderNo());
        result.setImgUrl(order.getImg());
        result.setPerson(getPerson());
        result.setCategoryName(order.getCategoryName());
        result.setPayPrice("" + (order.getPrePay() + order.getRestPay()));
        result.setPrePrice("" + order.getPrePay());
        result.setOrderStatus(order.getStatus());
        if (order.getCreateTime() != null)
            result.setCreateTime(sf.format(order.getCreateTime()));
        if (order.getOverTime() != null)
            result.setOverTime(sf.format(order.getOverTime()));
        result.setTotalPrice(order.getTotalPay() + "");
        result.setCategroyList(getlist());
        result.setBrandName(order.getProductName());
        result.setMinPrice("" + order.getMinPrice());
        result.setMaxPrice("" + order.getMaxPrice());
        return result;
    }

    @ApiOperation("我的服务订单详情")
    @GetMapping("/deal/detail/{id}")
    public Order.Detail OrderDealDtail(@PathVariable("id") int id) {
        Order.Detail result = new Order.Detail();
        result = getOrderDetail(id);
        return result;

    }

    private List<Order.OrderRes> convertOrder(List<OrderDTO.BrandOrderVO> request) {
        List<Order.OrderRes> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(request))
            return result;
        List<String> brandIds = request.stream().map(p -> p.getProductNo()).collect(Collectors.toList());
        request.stream().forEach(p -> {
            Order.OrderRes item = new Order.OrderRes();
            item.setId(p.getId());
            item.setOrderNo(p.getOrderNo());
            item.setImgUrl(p.getImg());
            item.setBrandName(p.getProductName());
            item.setCategoryName(p.getCategoryName());
            item.setImgNo("redxydd");
            item.setMaxPrice("￥" + 3000);
            item.setMinPrice("￥" + 1000);
            item.setPrePrice("￥" + p.getPrePay());
            item.setOrderStatus(p.getStatus());
            item.setOrderType(p.getOrderType());
            result.add(item);
        });
        /*for (int i = 0; i < 3; i++) {
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
        }*/
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
        if (request.getTotalPay() == null) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商品总价不能为空");
        }
        if (request.getPrePay() == null) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "预支付价不能为空");
        }
        int restPay = Integer.parseInt(request.getTotalPay()) - Integer.parseInt(request.getPrePay());
        OrderDTO.BrandOrderVO result = new OrderDTO.BrandOrderVO();
        result.setOrderNo("" + System.currentTimeMillis());
        result.setStatus(OrderStatusEnums.PRE_PAY.getType());
        result.setPrePay(Integer.parseInt(request.getPrePay()));
        result.setRestPay(restPay);
        result.setTotalPay(Integer.parseInt(request.getTotalPay()));
        result.setUserId(AppUserPrincipal.getPrincipal().getId());
        result.setOpUserId(1);
        result.setCreateTime(new Date());
        result.setUpdateTime(new Date());


        result.setOrderType(request.getOrderType());
        result.setProductNo(request.getProductNo());
        result.setMinPrice(10000);
        result.setMinPrice(20000);
        if (Strings.isNullOrEmpty(request.getProductNo())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标编号不能为空");
        }
        Map<String, TrademarkDTO.MdBrand> brandMap = getBrandByBrandIds(Arrays.asList(request.getProductNo()));
        if (!brandMap.containsKey(request.getProductNo()))
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标编号不存在");
        TrademarkDTO.MdBrand brand = brandMap.get(request.getProductNo());
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
