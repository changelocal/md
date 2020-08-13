package com.md.union.front.api.controller;


import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.client.dto.WxUserDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("wx")
@Api(tags = {"微信，小程序管理服务,目前没用以后用"})
public class WxController {

    @Autowired
    private FrontClient frontClient;

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

    @PostMapping("test123")
    @ApiOperation(value = "消息回复,关注取关", notes = "消息回复,关注取关")
    public WxUserDTO.QueryResp test() throws IOException {
        WxUserDTO.QueryResp resp = new WxUserDTO.QueryResp();
        WxUserDTO.WxUser wxUser = new WxUserDTO.WxUser();
        wxUser.setPageIndex(1);
        wxUser.setPageSize(10);
        BaseResponse<WxUserDTO.QueryResp> response =frontClient.query(wxUser);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        if(null !=(response.getResult().getItems())){
            BeanUtils.copyProperties(response.getResult(), resp);
        }

        return resp;
    }
}
