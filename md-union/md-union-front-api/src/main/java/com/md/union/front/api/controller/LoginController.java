package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.Anonymous;
import com.arc.util.http.BaseResponse;
import com.arc.util.lang.EncryptUtil;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.MinUser;
import com.md.union.front.client.dto.WxUserDTO;
import com.md.union.front.client.feign.UserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/front/login")
@Api(tags = {"登录管理"})
@Slf4j
public class LoginController {

    @Autowired
    private MinCommon minCommon;
    @Autowired
    private UserClient userClient;

    @ApiOperation("小程序登录")
    @GetMapping("/min/{code}")
    @Anonymous
    public MinUser login(@PathVariable("code") String code, HttpServletRequest request) {
        MinUser minUser = minCommon.minLogin(code);
        WxUserDTO.QueryWxUser param = new WxUserDTO.QueryWxUser();
        param.setMinId(minUser.getMinId());
        log.info("userClient.getByCondition param:{}", JSON.toJSONString(param));
        BaseResponse<WxUserDTO.WxUser> userResp = userClient.getByCondition(param);
        log.info("userClient.getByCondition result:{}", JSON.toJSONString(userResp));
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(userResp.getStatus())) {
            throw new ServiceException(userResp.getStatus(), userResp.getMessage());
        }
        if (userResp.getResult() == null) {
            addWxUser(minUser);
        }
        minUser.setSessionId(EncryptUtil.md5(minUser.getMinId()+"hhh"));
        minUser.setMobile(userResp.getResult().getMobile());
        minUser.setAppId(userResp.getResult().getAppId());
        minUser.setId(userResp.getResult().getId());

        //放入缓存
        request.getSession().setAttribute(minUser.getSessionId(), JSON.toJSONString(minUser));
        log.info("放入缓存的值:{}", JSON.toJSONString(minUser));
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
