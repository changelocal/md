package com.md.union.front.client.feign;


import com.md.union.front.client.dto.OrderDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-order-service", contextId = "order", path = "/brand/order")
public interface OrderClient {

    @ApiOperation(value = "查询所有用户订单", notes = "查询所有用户订单")
    @PostMapping("/query")
    OrderDTO.QueryResp query(@RequestBody OrderDTO.BrandOrderVO request);

    @ApiOperation(value = "添加用户订单", notes = "添加用户订单")
    @PostMapping("/add")
    void add(@RequestBody OrderDTO.BrandOrderVO request);

    @ApiOperation(value = "修改用户订单", notes = "修改用户订单")
    @PostMapping("/update")
    void update(@RequestBody OrderDTO.BrandOrderVO request);

    @ApiOperation(value = "按调价查询用户订单 只能获取一条", notes = "按调价查询用户订单 只能获取一条")
    @PostMapping("/get/by/condition")
    OrderDTO.BrandOrderVO getByCondition(@RequestBody OrderDTO.BrandOrderVO request);
}