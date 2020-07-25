package com.md.atom.user.service.controller;

import com.atom.core.dao.AdminUserDao;
import com.atom.core.model.AdminUser;
import com.md.atom.user.service.vo.AdminUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"管理人员 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("api")
@Slf4j
public class AdminUserController {

    @Autowired
    private AdminUserDao adminUserDao;

    @GetMapping("/query")
    public AdminUserVO.QueryResp query() {
        AdminUserVO.QueryResp result = new AdminUserVO.QueryResp();
        List<AdminUser> list = adminUserDao.query();

        if(!CollectionUtils.isEmpty(list)){
            List<AdminUserVO.User> users = new ArrayList<>();
            list.forEach(e->{
                AdminUserVO.User item = new AdminUserVO.User();
                item.setId(e.getId());
                item.setName(e.getNickName());
                item.setMobile(e.getMobile());
                item.setQqAccount(e.getQqAccount());
                item.setWxAccount(e.getWxAccount());
                users.add(item);
            });
            result.setUserList(users);
        }

        return result;
    }

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/create1")
    public AdminUserVO.AddResp create(@RequestBody AdminUserVO.Add request) {

        AdminUser adminUser = new AdminUser();
        adminUser.setMobile(request.getMobile());
        adminUser.setNickName(request.getName());
        adminUser.setQqAccount(request.getQqAccount());
        long id = adminUserDao.add(adminUser);

        AdminUserVO.AddResp result = new AdminUserVO.AddResp();
//        result.setId(id);
        return result;
    }

}
