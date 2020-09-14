package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.Enums.UploadPicEnums;
import com.md.union.admin.api.vo.Order;
import com.md.union.admin.api.vo.Ref;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.dto.RefDTO;
import com.md.union.front.client.feign.FrontClient;
import com.md.union.front.client.feign.OrderClient;
import com.md.union.front.client.feign.OrderRefClient;
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
    @Autowired
    private OrderRefClient orderRefClient;

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
}