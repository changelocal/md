package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.arc.util.lang.EncryptUtil;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.MinUser;
import com.md.union.front.client.dto.WxUserDTO;
import com.md.union.front.client.feign.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/front/login")
@Api(tags = {"登录管理"}, description = "接口负责人：田秀全")
public class LoginController {

    @Autowired
    private MinCommon minCommon;
    @Autowired
    private UserClient userClient;

    @ApiOperation("小程序登录")
    @GetMapping("/min/{code}")
    public MinUser login(@PathVariable("code") String code, HttpServletRequest request) {
        MinUser minUser = minCommon.minLogin(code);
        WxUserDTO.QueryWxUser param = new WxUserDTO.QueryWxUser();
        param.setMinId(minUser.getMinId());
        BaseResponse<WxUserDTO.WxUser> userResp = userClient.getByCondition(param);
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(userResp.getStatus())) {
            throw new ServiceException(userResp.getStatus(), userResp.getMessage());
        }
        if (userResp.getResult() == null) {
            addWxUser(minUser);
        }
        minUser.setSessionId(EncryptUtil.md5(minUser.getMinId() + new Date().getTime()));
        minUser.setMobile(userResp.getResult().getMobile());
        minUser.setAppId(userResp.getResult().getAppId());
        minUser.setId(userResp.getResult().getId());

        //放入缓存
        request.getSession().setAttribute(minUser.getSessionId(), JSON.toJSONString(minUser));
        return minUser;
    }

    private void addWxUser(MinUser minUser) {
        WxUserDTO.WxUser user = new WxUserDTO.WxUser();
        user.setMinId(minUser.getMinId());
        user.setAppId(String.valueOf(new Date().getTime()));
        user.setMobile(minUser.getMobile());
        user.setOpenId(minUser.getOpenId());
        userClient.add(user);
    }
}
