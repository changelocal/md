package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.api.vo.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/brand")
@Api(tags = {"商标管理服务"})
public class BrandController {

    @ApiOperation("热门商标分类")
    @GetMapping("/hot/category")
    public List<Brand.HotRes> hotCategory() {
        List<Brand.HotRes> result = new ArrayList<>();

        return result;
    }

    @ApiOperation("商标严选")
    @GetMapping("/list")
    public List<Brand.GroupRes> list() {
        List<Brand.GroupRes> result = new ArrayList<>();

        return result;
    }

    @ApiOperation("买商标查询")
    @PostMapping("/search")
    public Brand.SearchRes brandSearch(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes result = new Brand.SearchRes();

        return result;
    }

    @ApiOperation("买商标分类查询")
    @PostMapping("/category")
    public Category.SearchRes categorySearch(@RequestBody Category.SearchReq request) {
        Category.SearchRes result = new Category.SearchRes();

        return result;
    }

    @ApiOperation("购买商标维权详情信息")
    @GetMapping("/deal/detail/{typeId}")
    public Brand.DealDetail dealDtail(@PathVariable("typeId") String typeId) {
        Brand.DealDetail result = new Brand.DealDetail();

        return result;

    }

    @ApiOperation("购买商标详情")
    @PostMapping("/buy/detail/{brandId}")
    public Brand.Detail buyDetail(@PathVariable("brandId") String brandId) {
        Brand.Detail result = new Brand.Detail();

        return result;

    }



}
