package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.SearchRecordDao;
import com.atom.core.model.SearchRecord;
import com.atom.core.model.SearchRecordParam;
import com.md.atom.user.service.vo.SearchRecordVO;
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
@RequestMapping("searchRecord")
@Slf4j
public class SearchRecordController {

    @Autowired
    private SearchRecordDao searchRecordDao;

    @PostMapping("/query")
    public SearchRecordVO.QueryResp query(@RequestBody SearchRecordVO.Info adminUser) {
//        log.info("query:"+adminUser);
        SearchRecordVO.QueryResp result = new SearchRecordVO.QueryResp();
        SearchRecordParam para = new SearchRecordParam();
        BeanUtils.copyProperties(adminUser, para);
        PageResult<SearchRecord> list = searchRecordDao.query(para);
        List<SearchRecordVO.Info> infos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list.getItems())){
            list.getItems().forEach(p->{
                SearchRecordVO.Info info = new SearchRecordVO.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });
            result.setTotal(list.getTotalCount());
            result.setInfos(infos);
        }else{
            result.setInfos(infos);
            result.setTotal(0);
        }
        return result;
    }
    @PostMapping("/add")
    public SearchRecordVO.Resp add(@RequestBody SearchRecordVO.Info request) {
        SearchRecord user = new SearchRecord();
        BeanUtils.copyProperties(request, user);
        user.setCreateTime(new Date());
        searchRecordDao.add(user);
        SearchRecordVO.Resp result = new SearchRecordVO.Resp();
        return result;
    }
    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/update")
    public SearchRecordVO.Resp update(@RequestBody SearchRecordVO.Info request) {
        SearchRecord user = new SearchRecord();
        BeanUtils.copyProperties(request, user);
        user.setUpdateTime(new Date());
        searchRecordDao.update(user);
        SearchRecordVO.Resp result = new SearchRecordVO.Resp();
        return result;
    }
}
