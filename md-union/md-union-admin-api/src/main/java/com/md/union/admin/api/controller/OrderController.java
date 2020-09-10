package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.vo.Order;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.feign.FrontClient;
import com.md.union.front.client.feign.OrderClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/order")
@Api(tags = {"管理人员"})
public class OrderController {
    @Autowired
    private FrontClient frontClient;
    @Autowired
    private OrderClient orderClient;
    @ApiOperation("查询")
    @PostMapping("/query")
    public Order.ListRes query(@RequestBody Order.SearchReq request) {
        Order.ListRes ret = new Order.ListRes();
        OrderDTO.BrandOrderVO param = new OrderDTO.BrandOrderVO();
        BeanUtils.copyProperties(request, param);
        BaseResponse<OrderDTO.QueryResp> query = orderClient.query(param);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getItems())){
            List<Order.OrderRes> infos = new ArrayList<>();
            query.getResult().getItems().forEach(p->{
                Order.OrderRes info = new Order.OrderRes();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });

            ret.setList(infos);
            ret.setTotal(query.getResult().getTotalCount());
        }else{
            ret.setTotal(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Order.OrderRes request) {
        OrderDTO.BrandOrderVO adminUser = new OrderDTO.BrandOrderVO();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<OrderDTO.Resp> query = orderClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
    }
}