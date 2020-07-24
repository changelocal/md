package com.md.union.front.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api")
@Api(tags = {"微信，小程序管理服务"})
public class WxController {

    @GetMapping(value = "/reply")
    public void wechatVerify(@RequestParam Map<String, String> reqMap, HttpServletResponse response) throws IOException {
        if (reqMap.containsKey("signature")) {
            //非企业号
            String signature = reqMap.get("signature");
            String timestamp = reqMap.get("timestamp");
            String nonce = reqMap.get("nonce");
            String echostr = reqMap.get("echostr");
            response.getWriter().write(echostr);
        }
    }

    @PostMapping("reply")
    @ApiOperation(value = "消息回复,关注取关", notes = "消息回复,关注取关")
    public void reply(HttpServletRequest req, HttpServletResponse response) throws IOException {

    }
}
