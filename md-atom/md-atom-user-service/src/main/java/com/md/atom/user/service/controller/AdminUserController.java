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
import org.springframework.web.bind.annotation.*;

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
        BeanUtils.copyProperties(list, result);
//        if(!CollectionUtils.isEmpty(list)){
//            List<AdminUserVO.AdminUser> users = new ArrayList<>();
//            list.forEach(e->{
//                AdminUserVO.AdminUser item = new AdminUserVO.AdminUser();
//                item.setId(e.getId());
//                item.setNickname(e.getNickname());
//                item.setMobile(e.getMobile());
//                item.setQqAccount(e.getQqAccount());
//                item.setWxAccount(e.getWxAccount());
//                users.add(item);
//            });
//            result.setUserList(users);
//        }

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
