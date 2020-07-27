package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.WxUserDao;
import com.atom.core.model.WxUser;
import com.atom.core.model.WxUserParam;
import com.md.atom.user.service.vo.WxUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"管理微信用户 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("wxuser")
@Slf4j
public class WxUserController {

    @Autowired
    private WxUserDao wxUserDao;

    @ApiOperation(value = "查询所有微信用户", notes = "查询所有微信用户")
    @PostMapping("/query")
    public WxUserVO.QueryResp query(WxUserVO.WxUser user) {
        WxUserVO.QueryResp result = new WxUserVO.QueryResp();
        WxUserParam para = new WxUserParam();
        BeanUtils.copyProperties(user, para);
        para.setPageIndex(1);
        para.setPageSize(10);
        PageResult<WxUser> list = wxUserDao.query(para);

        if(!CollectionUtils.isEmpty(list)){
            BeanUtils.copyProperties(list, result);
        }

        return result;
    }

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/add")
    public WxUserVO.AddResp add(@RequestBody WxUserVO.WxUser request) {

        WxUser wxUser = new WxUser();
        wxUser.setMobile(request.getMobile());
        wxUser.setNickName(request.getNickName());
        wxUser.setOpenId(request.getOpenId());
        long id = wxUserDao.add(wxUser);

        WxUserVO.AddResp result = new WxUserVO.AddResp();
        return result;
    }

}
