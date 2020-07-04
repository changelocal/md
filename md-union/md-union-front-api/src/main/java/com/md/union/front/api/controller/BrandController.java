package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Brand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("brand")
@Api(tags = {"商标管理服务"})
public class BrandController {

    @ApiOperation("热门商标分类")
    @GetMapping("hot/category")
    public List<Brand.HotRes> hotCategory() {
        List<Brand.HotRes> result = new ArrayList<>();

        return result;
    }

    @ApiOperation("商标严选")
    @GetMapping("list")
    public List<Brand.GroupRes> list() {
        List<Brand.GroupRes> result = new ArrayList<>();

        return result;
    }

    @ApiOperation("买商标查询")
    @PostMapping("search")
    public Brand.SearchRes search(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes result = new Brand.SearchRes();

        return result;
    }
}
