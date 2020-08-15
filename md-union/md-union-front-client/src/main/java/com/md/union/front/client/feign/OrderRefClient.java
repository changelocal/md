package com.md.union.front.client.feign;


import com.md.union.front.client.dto.RefDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 服务接口
 */
@FeignClient(name = "md-atom-order-service", contextId = "order-ref", path = "/ref/")
public interface OrderRefClient {

    @ApiOperation(value = "修改上传文件", notes = "修改上传文件")
    @PostMapping("/update")
    RefDTO.Resp update(@RequestBody RefDTO.OrderRefFile request);

    @ApiOperation(value = "新增加", notes = "新增加")
    @PostMapping("/add")
    void add(@RequestBody RefDTO.OrderRefFile request);

    @ApiOperation(value = "查找订单附件", notes = "查找订单附件")
    @PostMapping("/find/by/condition")
    List<RefDTO.OrderRefFile> findByCondition(@RequestBody RefDTO.OrderRefFile request);

}