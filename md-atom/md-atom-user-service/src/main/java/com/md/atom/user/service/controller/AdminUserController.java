package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.AdminUserDao;
import com.atom.core.model.AdminUser;
import com.atom.core.model.AdminUserParam;
import com.md.atom.user.service.vo.AdminUserVO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Api(tags = {"管理人员 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("adminuser")
@Slf4j
public class AdminUserController {

    @Autowired
    private AdminUserDao adminUserDao;

    @PostMapping("/query")
    public AdminUserVO.QueryResp query(AdminUserVO.AdminUser adminUser) {
        AdminUserVO.QueryResp result = new AdminUserVO.QueryResp();

        AdminUserParam para = new AdminUserParam();
        BeanUtils.copyProperties(adminUser, para);
        PageResult<AdminUser> list = adminUserDao.query(para);

        List<AdminUserVO.AdminUser> adminUsers = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list.getItems())){
            list.getItems().forEach(p->{
                AdminUserVO.AdminUser adminUser1 = new AdminUserVO.AdminUser();
                BeanUtils.copyProperties(p, adminUser1);
                adminUsers.add(adminUser1);
            });
        }

        result.setAdminUsers(adminUsers);
        result.setTotal(list.getTotalCount());
        return result;
    }

    @PostMapping("/find")
    public AdminUserVO.QueryResp find(AdminUserVO.AdminUser adminUser) {
        AdminUserVO.QueryResp result = new AdminUserVO.QueryResp();

        AdminUserParam para = new AdminUserParam();
        BeanUtils.copyProperties(adminUser, para);
        List<AdminUser> list = adminUserDao.find(para);
        BeanUtils.copyProperties(list, result);

        return result;
    }

    @PostMapping("/consultation")
    public AdminUserVO.AdminUser consultation(AdminUserVO.AdminUser adminUser) {
        AdminUserVO.AdminUser result = new AdminUserVO.AdminUser();

        AdminUserParam para = new AdminUserParam();
        para.setIsEnable(1);

        List<AdminUser> list = adminUserDao.find(para);
        if(!CollectionUtils.isEmpty(list)){
            Random random = new Random();
            BeanUtils.copyProperties(list.get(random.nextInt(list.size())), result);
        }

        return result;
    }

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/add")
    public AdminUserVO.Resp add(@RequestBody AdminUserVO.AdminUser request) {
        AdminUser user = new AdminUser();
        BeanUtils.copyProperties(request, user);
        adminUserDao.add(user);

        AdminUserVO.Resp result = new AdminUserVO.Resp();
        return result;
    }
    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/update")
    public AdminUserVO.Resp update(@RequestBody AdminUserVO.AdminUser request) {
        AdminUser user = new AdminUser();
        BeanUtils.copyProperties(request, user);
        adminUserDao.update(user);

        AdminUserVO.Resp result = new AdminUserVO.Resp();
        return result;
    }
}
