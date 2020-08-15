package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.MinUser;
import com.md.union.front.client.dto.WxUserDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/front/login")
@Api(tags = {"登录管理"}, description = "接口负责人：田秀全")
public class LoginController {

    @Autowired
    private MinCommon minCommon;
    @Autowired
    private FrontClient frontClient;

    @ApiOperation("小程序登录")
    @GetMapping("/min/{code}")
    public MinUser login(@PathVariable("code") String code) {
        MinUser minUser = minCommon.minLogin(code);

        WxUserDTO.WxUser wxUser = new WxUserDTO.WxUser();
        wxUser.setPageSize(10);
        wxUser.setPageIndex(0);
        wxUser.setOpenId(minUser.getOpenId());
        //find
        BaseResponse<WxUserDTO.QueryResp> response = frontClient.query(wxUser);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        //if null then add
        if(CollectionUtils.isEmpty(response.getResult().getItems())){
            //add
            wxUser.setCreateTime(new Date());
            wxUser.setOpenId(minUser.getOpenId());
            wxUser.setMobile(minUser.getMobile());
            frontClient.add(wxUser);
        }
        return minUser;
    }
}
