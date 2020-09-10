package com.md.atom.user.service.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.data.PageResult;
import com.arc.util.http.BaseResponse;
import com.atom.core.dao.WxUserDao;
import com.atom.core.model.WxUser;
import com.atom.core.model.WxUserParam;
import com.md.atom.user.service.vo.WxUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = {"管理微信用户 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("wxuser")
@Slf4j
public class WxUserController {

    @Autowired
    private WxUserDao wxUserDao;
    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmsssss");

    @ApiOperation(value = "查询所有微信用户", notes = "查询所有微信用户")
    @PostMapping("/query")
    public WxUserVO.QueryResp query(@RequestBody WxUserVO.QueryWxUser user) {
        WxUserVO.QueryResp result = new WxUserVO.QueryResp();
        List<WxUserVO.WxUser> list = new ArrayList<>();
        PageResult<WxUser> pageResult = wxUserDao.query(convert(user));
        if (!CollectionUtils.isEmpty(pageResult.getItems())) {
            pageResult.getItems().forEach(p -> {
                list.add(convert(p));
            });
        }
        result.setTotalCount(pageResult.getTotalCount());
        result.setItems(list);
        log.info("result:"+result);
        return result;
    }

    @ApiOperation(value = "添加微信用户记录", notes = "添加微信用户记录")
    @PostMapping("/add")
    public void add(@RequestBody WxUserVO.WxUser request) {
        log.info("com.md.atom.user.service.controller.WxUserController.add param:{}", JSON.toJSONString(request));
        WxUser wxUser = new WxUser();
        wxUser.setAppId(request.getAppId());
        wxUser.setOpenId(request.getOpenId());
        wxUser.setMobile(request.getMobile());
        wxUser.setNickName(request.getNickName());
        wxUser.setMinId(request.getMinId());
        wxUser.setIdCard(request.getIdCard());
        wxUser.setAddress(request.getAddress());
        wxUser.setCreateTime(new Date());
        wxUserDao.add(wxUser);
        log.info("com.md.atom.user.service.controller.WxUserController.add success:{}", wxUser.getAppId());

    }

    @ApiOperation(value = "修改微信用户记录", notes = "修改微信用户记录")
    @PostMapping("/update")
    public void update(@RequestBody WxUserVO.UpdateWxUser request) {
        log.info("com.md.atom.user.service.controller.WxUserController.update param:{}", JSON.toJSONString(request));
        WxUser wxUser = new WxUser();
        wxUser.setId(request.getId());
        wxUser.setMobile(request.getMobile());
        wxUser.setNickName(request.getNickName());
        wxUser.setMinId(request.getMinId());
        wxUser.setIdCard(request.getIdCard());
        wxUser.setAddress(request.getAddress());
        wxUser.setUpdateTime(new Date());
        wxUserDao.update(wxUser);
        log.info("com.md.atom.user.service.controller.WxUserController.update success:{}", wxUser.getId());

    }

    @ApiOperation(value = "查询微信用户记录", notes = "查询微信用户记录")
    @PostMapping("/get/by/condtion")
    public WxUserVO.WxUser getByCondition(@RequestBody WxUserVO.QueryWxUser request) {
        List<WxUser> users = wxUserDao.find(convert(request));
        if(CollectionUtils.isEmpty(users)){
            return null;
        }
        if (users.size() > 1) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "用户不唯一");
        }
        return convert(users.get(0));
    }

    private WxUserParam convert(WxUserVO.QueryWxUser user) {
        WxUserParam result = new WxUserParam();
        result.setPageIndex(user.getPageIndex());
        result.setPageSize(user.getPageSize());
        result.setOpenId(user.getOpenId());
        result.setId(user.getId());
        result.setAppId(user.getAppId());
        result.setUnionId(user.getUnionId());
        result.setMinId(user.getMinId());
        result.setIdCard(user.getIdCard());
        result.setMobile(user.getMobile());
        result.setBusinessNo(user.getBusinessNo());
        result.setPageSize(user.getPageSize());
        result.setPageIndex(user.getPageIndex());
        return result;
    }

    private WxUserVO.WxUser convert(WxUser request) {
        WxUserVO.WxUser result = new WxUserVO.WxUser();
        result.setId(request.getId());
        result.setAppId(request.getAppId());
        result.setUnionId(request.getUnionId());
        result.setOpenId(request.getOpenId());
        result.setMinId(request.getMinId());
        result.setFollowStatus(request.getFollowStatus());
        result.setSex(request.getSex());
        result.setNickName(request.getNickName());
        result.setRealName(request.getRealName());
        result.setIdCard(request.getIdCard());
        result.setMobile(request.getMobile());
        result.setAddress(request.getAddress());
        result.setCreateTime(request.getCreateTime());
        result.setBusinessNo(request.getBusinessNo());
        return result;
    }


}
