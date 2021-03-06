package com.md.atom.user.service.controller;

import com.atom.core.dao.BrandClassDao;
import com.atom.core.model.BrandClass;
import com.md.atom.user.service.vo.BrandClassVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = {"45大分类 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("brand")
@Slf4j
public class BrandClassController {

    @Autowired
    private BrandClassDao brandClassDao;

    @ApiOperation(value = "获得热门", notes = "获得热门")
    @GetMapping("/hot")
    public BrandClassVO.HotResp hot() {
        BrandClassVO.HotResp result = new BrandClassVO.HotResp();
        BrandClass brandClass = new BrandClass();
        brandClass.setPcode(0);
        brandClass.setIsHot(1);
        List<BrandClass> brandClasses = brandClassDao.find(brandClass);
        if(!CollectionUtils.isEmpty(brandClasses)){
            List<BrandClassVO.Cate> cates = new ArrayList<>();
            brandClasses.forEach(e->{
                BrandClassVO.Cate item = new BrandClassVO.Cate();
                item.setCode(e.getCode());
                item.setCategoryName(e.getName());
                cates.add(item);
            });

            result.setCates(cates);
        }
        return result;
    }

    @ApiOperation(value = "获得45大分类", notes = "获得45大分类")
    @GetMapping("/root")
    public BrandClassVO.HotResp root() {
        BrandClassVO.HotResp result = new BrandClassVO.HotResp();
        BrandClass brandClass = new BrandClass();
        brandClass.setPcode(0);
        List<BrandClass> brandClasses = brandClassDao.find(brandClass);
        if(!CollectionUtils.isEmpty(brandClasses)){
            List<BrandClassVO.Cate> cates = new ArrayList<>();
            brandClasses.forEach(e->{
                BrandClassVO.Cate item = new BrandClassVO.Cate();
                item.setCode(e.getCode());
                item.setCategoryName(e.getName());
                cates.add(item);
            });
            result.setCates(cates);
        }
        return result;
    }

    @ApiOperation(value = "获得45大分类详细", notes = "获得45大分类详细")
    @GetMapping("/details/{code}")
    public BrandClassVO.HotResp details(@PathVariable("code") int code) {
        BrandClassVO.HotResp result = new BrandClassVO.HotResp();
        BrandClass brandClass = new BrandClass();
        brandClass.setPcode(code);
        List<BrandClass> brandClasses = brandClassDao.find(brandClass);
        if(!CollectionUtils.isEmpty(brandClasses)){
            List<BrandClassVO.Cate> cates = new ArrayList<>();
            brandClasses.forEach(e->{
                BrandClassVO.Cate item = new BrandClassVO.Cate();
                item.setCode(e.getCode());
                item.setCategoryName(e.getName());
                item.setDes(e.getDes());
                cates.add(item);
            });
            result.setCates(cates);
        }
        return result;
    }
}
