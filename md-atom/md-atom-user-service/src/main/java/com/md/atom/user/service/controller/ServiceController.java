package com.md.atom.user.service.controller;

import com.atom.core.dao.ConsultationDao;
import com.atom.core.dao.ServiceDao;
import com.atom.core.model.Consultation;
import com.atom.core.model.Service;
import com.atom.core.model.ServiceParam;
import com.md.atom.user.service.vo.ServiceVO;
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

@Api(tags = {"服务类订单 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("service")
@Slf4j
public class ServiceController {
    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private ConsultationDao consultationDao;

    @ApiOperation(value = "获得", notes = "获得")
    @GetMapping("/get/{code}")
    public ServiceVO.Service details(@PathVariable("code") String code) {
        ServiceVO.Service ret = new ServiceVO.Service();

        ServiceParam serviceParam = new ServiceParam();
        serviceParam.setId(code);
        List<Service> services = serviceDao.find(serviceParam);
        BeanUtils.copyProperties(services.get(0), ret);

        return ret;
    }

    @ApiOperation(value = "增加咨询记录", notes = "增加咨询记录")
    @PostMapping("/add/consultation")
    public ServiceVO.Service addConsultation(@RequestBody Consultation con) {
        ServiceVO.Service ret = new ServiceVO.Service();

        Consultation consultation = new Consultation();
        BeanUtils.copyProperties(con, consultation);
        consultation.setCreateTime(new Date());

        int add = consultationDao.add(consultation);

        return ret;
    }

    @ApiOperation(value = "增加咨询记录", notes = "增加咨询记录")
    @PostMapping("/find")
    public ServiceVO.FindResp find(@RequestBody ServiceVO.Service con) {
        ServiceVO.FindResp ret = new ServiceVO.FindResp();

        ServiceParam param = new ServiceParam();
        BeanUtils.copyProperties(con, param);
        List<Service> services = serviceDao.find(param);
        if(!CollectionUtils.isEmpty(services)){
            List<ServiceVO.Service> services1 = new ArrayList<>();
            services.forEach(p->{
                ServiceVO.Service vo = new ServiceVO.Service();
                BeanUtils.copyProperties(p, vo);
                services1.add(vo);
            });
            ret.setServices(services1);
        }

        return ret;
    }

}