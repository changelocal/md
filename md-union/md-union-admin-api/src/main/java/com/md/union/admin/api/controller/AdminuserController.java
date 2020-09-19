package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.vo.Adminuser;
import com.md.union.front.client.dto.AdminUserDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin/user")
@Api(tags = {"管理人员"})
public class AdminuserController {
    @Autowired
    private FrontClient frontClient;

    @PostMapping("/login")
    public Adminuser.LoginRes login(@RequestBody Adminuser.LoginReq request) {
        //if(true)throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE,"登录失败");
        Adminuser.LoginRes result = new Adminuser.LoginRes();
        result.setToken("super_admin");
        return result;
    }

    @GetMapping("/info")
    public Adminuser.LoginRes info() {
        Adminuser.LoginRes result = new Adminuser.LoginRes();
        result.setToken("super_admin");
        result.setUser_id("1");
        result.setAccess(Arrays.asList("super_admin", "admin"));
        result.setAvatar("https://file.iviewui.com/dist/a0e88e83800f138b94d2414621bd9704.png");
        return result;
    }

    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public Adminuser.SearchRes query(@RequestBody Adminuser.SearchReq request) {
        Adminuser.SearchRes ret = new Adminuser.SearchRes();
        AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<AdminUserDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if (!CollectionUtils.isEmpty(query.getResult().getAdminUsers())) {
            List<Adminuser.Info> infos = new ArrayList<>();
            query.getResult().getAdminUsers().forEach(p -> {
                Adminuser.Info info = new Adminuser.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });

            ret.setList(infos);
            ret.setCount(query.getResult().getTotal());
        } else {
            ret.setList(new ArrayList<>());
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Adminuser.Info request) {
        AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        //password
        String salt = DigestUtils.md5Hex(request.getPassword() + System.currentTimeMillis());
        adminUser.setPassword(DigestUtils.md5Hex(request.getPassword() + salt));//md5 getBusinessNo
        adminUser.setSalt(salt); //md5 getBusinessNo+timestamp
        BaseResponse<AdminUserDTO.Resp> query = frontClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
    }

    @PostMapping("/add")
    public void add(@RequestBody Adminuser.Info request) {
        AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        //password
        String salt = DigestUtils.md5Hex(request.getPassword() + System.currentTimeMillis());
        adminUser.setPassword(DigestUtils.md5Hex(request.getPassword() + salt));//md5 getBusinessNo
        adminUser.setSalt(salt); //md5 getBusinessNo+timestamp
        BaseResponse<AdminUserDTO.Resp> query = frontClient.add(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
    }

    @GetMapping("/home")
    public List<Adminuser.HomeRes> home() {
        List<Adminuser.HomeRes> ret = new ArrayList<>();
        BaseResponse<List<AdminUserDTO.HomeRes>> counter = frontClient.counter();
        if (!counter.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(counter.getStatus(), counter.getMessage());
        }
        counter.getResult().forEach(p->{
            Adminuser.HomeRes result = new Adminuser.HomeRes();
            BeanUtils.copyProperties(p, result);
            ret.add(result);
        });
        return ret;
    }
}