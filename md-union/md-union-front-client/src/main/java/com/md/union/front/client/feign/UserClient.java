package com.md.union.front.client.feign;


import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.WxUserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信用户服务接口
 */
@FeignClient(name = "md-atom-user-service", contextId = "wxuser", path = "/wxuser/")
public interface UserClient {

    @ApiOperation(value = "查询所有微信用户", notes = "查询所有微信用户")
    @PostMapping("/query")
    BaseResponse<WxUserDTO.QueryResp> query(WxUserDTO.QueryWxUser user);

    @ApiOperation(value = "添加微信用户记录", notes = "添加微信用户记录")
    @PostMapping("/add")
    BaseResponse<Long> add(@RequestBody WxUserDTO.WxUser request);

    @ApiOperation(value = "修改微信用户记录", notes = "修改微信用户记录")
    @PostMapping("/update")
    BaseResponse update(@RequestBody WxUserDTO.UpdateWxUser request);

    @ApiOperation(value = "查询微信用户记录", notes = "查询微信用户记录")
    @PostMapping("/get/by/condtion")
    BaseResponse<WxUserDTO.WxUser> getByCondition(@RequestBody WxUserDTO.QueryWxUser request);


}