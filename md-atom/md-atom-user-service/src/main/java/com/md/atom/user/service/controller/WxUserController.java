package com.md.atom.user.service.controller;

import com.atom.core.dao.WxUserDao;
import com.atom.core.model.WxUser;
import com.md.atom.user.service.vo.WxUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"管理微信用户 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("wx")
@Slf4j
public class WxUserController {

    @Autowired
    private WxUserDao wxUserDao;

    @ApiOperation(value = "查询所有微信用户", notes = "查询所有微信用户")
    @GetMapping("/query")
    public WxUserVO.QueryResp query() {
        WxUserVO.QueryResp result = new WxUserVO.QueryResp();
        List<WxUser> list = wxUserDao.query();

        if(!CollectionUtils.isEmpty(list)){
            List<WxUserVO.User> users = new ArrayList<>();
            list.forEach(e->{
                WxUserVO.User item = new WxUserVO.User();
                item.setId(e.getId());
                item.setNickName(e.getNickName());
                item.setMobile(e.getMobile());
                item.setIsEnable(e.getIsEnable());
                item.setOpenId(e.getOpenId());
                users.add(item);
            });
            result.setUserList(users);
        }

        return result;
    }

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/create1")
    public WxUserVO.AddResp create(@RequestBody WxUserVO.Add request) {

        WxUser wxUser = new WxUser();
        wxUser.setMobile(request.getMobile());
        wxUser.setNickName(request.getNickName());
        wxUser.setOpenId(request.getOpenId());
        long id = wxUserDao.add(wxUser);

        WxUserVO.AddResp result = new WxUserVO.AddResp();
//        result.setId(id);
        return result;
    }

}
