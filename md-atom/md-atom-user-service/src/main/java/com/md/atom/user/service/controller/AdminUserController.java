package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.AdminUserDao;
import com.atom.core.dao.BrandOrderDao;
import com.atom.core.dao.MdBrandDao;
import com.atom.core.dao.WxUserDao;
import com.atom.core.model.AdminUser;
import com.atom.core.model.AdminUserParam;
import com.md.atom.user.service.vo.AdminUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Api(tags = {"管理人员 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("adminuser")
@Slf4j
public class AdminUserController {

    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private WxUserDao wxUserDao;
    @Autowired
    private MdBrandDao mdBrandDao;
    @Autowired
    private BrandOrderDao brandOrderDao;

    @PostMapping("/query")
    public AdminUserVO.QueryResp query(@RequestBody AdminUserVO.AdminUser adminUser) {
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
    public AdminUserVO.QueryResp find(@RequestBody AdminUserVO.AdminUser adminUser) {
        AdminUserVO.QueryResp result = new AdminUserVO.QueryResp();

        AdminUserParam para = new AdminUserParam();
        BeanUtils.copyProperties(adminUser, para);
        List<AdminUser> list = adminUserDao.find(para);

        List<AdminUserVO.AdminUser> adminUsers = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(p->{
                AdminUserVO.AdminUser adminUser1 = new AdminUserVO.AdminUser();
                BeanUtils.copyProperties(p, adminUser1);
                adminUsers.add(adminUser1);
            });
        }

        result.setAdminUsers(adminUsers);

        return result;
    }

    @PostMapping("/consultation")
    public AdminUserVO.AdminUser consultation(@RequestBody AdminUserVO.AdminUser adminUser) {
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
        user.setCreateTime(new Date());
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

    @GetMapping("/counter")
    public List<AdminUserVO.HomeRes> counter() {
        List<AdminUserVO.HomeRes> ret = new ArrayList<>();
        AdminUserVO.HomeRes res = new AdminUserVO.HomeRes();
        res.setTitle("累计员工");
        res.setColor("#2d8cf0");
        res.setIcon("md-person-add");
        res.setCount(adminUserDao.counter());
        ret.add(res);

         res = new AdminUserVO.HomeRes();
        res.setTitle("累计用户");
        res.setColor("#19be6b");
        res.setIcon("md-locate");
        res.setCount(wxUserDao.counter());
        ret.add(res);

         res = new AdminUserVO.HomeRes();
        res.setTitle("累计商标");
        res.setColor("#ff9900");
        res.setIcon("md-help-circle");
        res.setCount(mdBrandDao.counter());
        ret.add(res);

        res = new AdminUserVO.HomeRes();
        res.setTitle("累计订单");
        res.setColor("#ed3f14");
        res.setIcon("md-share");
        res.setCount(brandOrderDao.counter());
        ret.add(res);
        return ret;
    }

}
