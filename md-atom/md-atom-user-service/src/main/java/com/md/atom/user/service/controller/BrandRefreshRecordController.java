package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.BrandRefreshRecordDao;
import com.atom.core.model.BrandRefreshRecord;
import com.atom.core.model.BrandRefreshRecordParam;
import com.md.atom.user.service.vo.BrandRefreshRecordVO;
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
@RequestMapping("BrandRefreshRecord")
@Slf4j
public class BrandRefreshRecordController {

    @Autowired
    private BrandRefreshRecordDao brandRefreshRecordDao;

    @PostMapping("/query")
    public BrandRefreshRecordVO.QueryResp query(@RequestBody BrandRefreshRecordVO.Info adminUser) {
//        log.info("query:"+adminUser);
        BrandRefreshRecordVO.QueryResp result = new BrandRefreshRecordVO.QueryResp();
        BrandRefreshRecordParam para = new BrandRefreshRecordParam();
        BeanUtils.copyProperties(adminUser, para);
        PageResult<BrandRefreshRecord> list = brandRefreshRecordDao.query(para);
        List<BrandRefreshRecordVO.Info> infos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list.getItems())){
            list.getItems().forEach(p->{
                BrandRefreshRecordVO.Info info = new BrandRefreshRecordVO.Info();
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
    public BrandRefreshRecordVO.Resp add(@RequestBody BrandRefreshRecordVO.Info request) {
        BrandRefreshRecord user = new BrandRefreshRecord();
        BeanUtils.copyProperties(request, user);
        user.setCreateTime(new Date());
        brandRefreshRecordDao.add(user);
        BrandRefreshRecordVO.Resp result = new BrandRefreshRecordVO.Resp();
        return result;
    }
    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/update")
    public BrandRefreshRecordVO.Resp update(@RequestBody BrandRefreshRecordVO.Info request) {
        BrandRefreshRecord user = new BrandRefreshRecord();
        BeanUtils.copyProperties(request, user);
        user.setUpdateTime(new Date());
        brandRefreshRecordDao.update(user);
        BrandRefreshRecordVO.Resp result = new BrandRefreshRecordVO.Resp();
        return result;
    }
}
