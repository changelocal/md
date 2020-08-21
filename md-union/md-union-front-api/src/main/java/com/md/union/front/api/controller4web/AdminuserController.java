package com.md.union.front.api.controller4web;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo4web.Adminuser;
import com.md.union.front.client.dto.AdminUserDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/web/adminuser")
@Api(tags = {"管理人员"})
public class AdminuserController {
    @Autowired
    private FrontClient frontClient;
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
        if(!CollectionUtils.isEmpty(query.getResult().getAdminUsers())){
            List<Adminuser.Info> infos = new ArrayList<>();
            query.getResult().getAdminUsers().forEach(p->{
                Adminuser.Info info= new Adminuser.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });

            ret.setList(infos);
            ret.setCount(query.getResult().getAdminUsers().size());
        }else{
            ret.setList(new ArrayList<>());
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Adminuser.Info request) {
        AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<AdminUserDTO.Resp> query = frontClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }

    @PostMapping("/add")
    public void add(@RequestBody Adminuser.Info request) {
        AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<AdminUserDTO.Resp> query = frontClient.add(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }
}