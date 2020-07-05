package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Brand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/deal")
@Api(tags = {"客户服务咨询管理"})
public class DealController {


    @ApiOperation("商标注册介绍")
    @GetMapping("/register")
    public List<Brand.BrandRegister> register() {
        List<Brand.BrandRegister> result = new ArrayList<>();
        return result;
    }

    @ApiOperation("商标维权介绍")
    @GetMapping("/power")
    public List<Brand.DealRight> power() {
        List<Brand.DealRight> result = new ArrayList<>();
        return result;
    }
}
