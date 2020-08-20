package com.md.union.front.api.controller4web;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo4web.Wxuser;
import com.md.union.front.client.dto.WxUserDTO;
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
@RequestMapping("/web/consultation")
@Api(tags = {"管理人员"})
public class ConsultationController {
    @Autowired
    private FrontClient frontClient;
    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public Wxuser.SearchRes query(@RequestBody Wxuser.SearchReq request) {
        Wxuser.SearchRes ret = new Wxuser.SearchRes();
        WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<WxUserDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getItems())){
            List<Wxuser.Info> infos = new ArrayList<>();
            BeanUtils.copyProperties(query.getResult().getItems(), infos);
            ret.setList(infos);
            ret.setCount(query.getResult().getItems().size());
        }else{
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Wxuser.Info request) {
        WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<WxUserDTO.Resp> query = frontClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }

}