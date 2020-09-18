package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.vo.SearchRecord;
import com.md.union.front.client.dto.SearchRecordDTO;
import com.md.union.front.client.feign.FrontClient;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/web/searchRecord")
@Api(tags = {"search"})
@Slf4j
public class SearchRecordController {
    @Autowired
    private FrontClient frontClient;
    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public SearchRecord.SearchRes query(@RequestBody SearchRecord.SearchReq request) {
        log.info("query:"+request);
        SearchRecord.SearchRes ret = new SearchRecord.SearchRes();
        SearchRecordDTO.Info adminUser = new SearchRecordDTO.Info();
        BeanUtils.copyProperties(request, adminUser);

        if(request.getDateRange().length>0){
            adminUser.setCreateTimeBegin(new Date(request.getDateRange()[0]));
            adminUser.setCreateTimeEnd(new Date(request.getDateRange()[1]));
        }else{
            adminUser.setCreateTimeBegin(null);
            adminUser.setCreateTimeEnd(null);
        }

        log.info("adminUser:"+adminUser);
        BaseResponse<SearchRecordDTO.QueryResp> query = frontClient.query(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getInfos())){
            List<SearchRecord.Info> infos = new ArrayList<>();
            query.getResult().getInfos().forEach(p->{
                SearchRecord.Info info = new SearchRecord.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });
            ret.setList(infos);
            ret.setCount(query.getResult().getTotal());
        }else{
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody SearchRecord.Info request) {
        SearchRecordDTO.Info info = new SearchRecordDTO.Info();
        BeanUtils.copyProperties(request, info);
        info.setUpdateTime(new Date());
        BaseResponse<SearchRecordDTO.Resp> query = frontClient.update(info);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
    }

}