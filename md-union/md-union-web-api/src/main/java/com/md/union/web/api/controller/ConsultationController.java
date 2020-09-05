package com.md.union.web.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.web.api.vo.Consultation;
import com.md.union.front.client.dto.ConsultationDTO;
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
@Api(tags = {"咨询"})
public class ConsultationController {
    @Autowired
    private FrontClient frontClient;
    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public Consultation.SearchRes query(@RequestBody Consultation.SearchReq request) {
        Consultation.SearchRes ret = new Consultation.SearchRes();
        ConsultationDTO.Info adminUser = new ConsultationDTO.Info();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<ConsultationDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getInfos())){
            List<Consultation.Info> infos = new ArrayList<>();
            query.getResult().getInfos().forEach(p->{
                Consultation.Info info = new Consultation.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });

            ret.setList(infos);
            ret.setCount(query.getResult().getInfos().size());
        }else{
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Consultation.Info request) {
        ConsultationDTO.Info info = new ConsultationDTO.Info();
        BeanUtils.copyProperties(request, info);
        BaseResponse<ConsultationDTO.Resp> query = frontClient.update(info);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }

}