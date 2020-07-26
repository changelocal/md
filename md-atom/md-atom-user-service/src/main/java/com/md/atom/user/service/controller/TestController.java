package com.md.atom.user.service.controller;

import com.atom.core.dao.OrderDao;
import com.md.atom.user.service.vo.SampleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"测试订单 "}, description = "接口负责人：田秀全")
@RestController
@RequestMapping("api")
@Slf4j
public class TestController {

    @Autowired
    private OrderDao orderDao;

    @GetMapping("/hello")
    public String hello() {
        //List<Order> list = orderDao.query();
        log.info("list = {}", "dffd");
        return "aaaa";
    }

    @ApiOperation(value = "测试订单", notes = "测试订单")
    @PostMapping("/create")
    public SampleVO.TestResp create(@RequestBody SampleVO.Test request) {

        SampleVO.TestResp result = new SampleVO.TestResp();
        result.setId(request.getId());
        result.setName(request.getName());
        return result;
    }

}
