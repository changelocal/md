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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"管理人员 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("consultation")
@Slf4j
public class ConsultationController {

    @Autowired
    private ConsultationDao consultationDao;

    @PostMapping("/query")
    public ConsultationVO.QueryResp query(Consultation adminUser) {
        ConsultationVO.QueryResp result = new ConsultationVO.QueryResp();

        ConsultationParam para = new ConsultationParam();
        BeanUtils.copyProperties(adminUser, para);
        PageResult<Consultation> list = consultationDao.query(para);
        BeanUtils.copyProperties(list, result);

        return result;
    }

    @ApiOperation(value = "新增加管理人员", notes = "新增加管理人员")
    @PostMapping("/update")
    public ConsultationVO.Resp update(@RequestBody Consultation request) {
        Consultation user = new Consultation();
        BeanUtils.copyProperties(request, user);
        consultationDao.update(user);

        ConsultationVO.Resp result = new ConsultationVO.Resp();
        return result;
    }
}
