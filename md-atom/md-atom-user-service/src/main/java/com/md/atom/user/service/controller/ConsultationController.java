package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.ConsultationDao;
import com.atom.core.model.Consultation;
import com.atom.core.model.ConsultationParam;
import com.md.atom.user.service.vo.ConsultationVO;
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

@Api(tags = {"管理人员 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("consultation")
@Slf4j
public class ConsultationController {

    @Autowired
    private ConsultationDao consultationDao;

    @PostMapping("/query")
    public ConsultationVO.QueryResp query(@RequestBody ConsultationVO.Info adminUser) {
        log.info("query:"+adminUser);
        ConsultationVO.QueryResp result = new ConsultationVO.QueryResp();
        ConsultationParam para = new ConsultationParam();
        BeanUtils.copyProperties(adminUser, para);
        PageResult<Consultation> list = consultationDao.query(para);
        List<ConsultationVO.Info> infos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list.getItems())){
            list.getItems().forEach(p->{
                ConsultationVO.Info info = new ConsultationVO.Info();
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

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/update")
    public ConsultationVO.Resp update(@RequestBody ConsultationVO.Info request) {
        Consultation user = new Consultation();
        BeanUtils.copyProperties(request, user);
        user.setUpdateTime(new Date());
        consultationDao.update(user);
        ConsultationVO.Resp result = new ConsultationVO.Resp();
        return result;
    }
}
