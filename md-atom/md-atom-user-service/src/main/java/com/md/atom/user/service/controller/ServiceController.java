package com.md.atom.user.service.controller;

import com.atom.core.dao.ServiceDao;
import com.atom.core.model.Service;
import com.atom.core.model.ServiceParam;
import com.md.atom.user.service.vo.ServiceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"服务类订单 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("service")
@Slf4j
public class ServiceController {
    @Autowired
    private ServiceDao serviceDao;

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


}