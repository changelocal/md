package com.md.union.front.api.controller;

import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.MinUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/front/login")
@Api(tags = {"登录管理"}, description = "接口负责人：田秀全")
public class LoginController {

    @Autowired
    private MinCommon minCommon;

    @ApiOperation("小程序登录")
    @GetMapping("/min/{code}")
    public MinUser login(@PathVariable("code") String code) {
        return minCommon.minLogin(code);
    }
}
