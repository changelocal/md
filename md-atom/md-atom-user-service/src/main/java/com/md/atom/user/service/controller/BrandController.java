package com.md.atom.user.service.controller;

import com.arc.util.data.PageResult;
import com.atom.core.dao.MdBrandDao;
import com.atom.core.model.MdBrand;
import com.atom.core.model.MdBrandParam;
import com.md.atom.user.service.vo.BrandVO;
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
import java.util.List;

@Api(tags = {"品牌管理 "}, description = "接口负责人：sxj")
@RestController
@RequestMapping("mdbrand")
@Slf4j
public class BrandController {

    @Autowired
    private MdBrandDao mdBrandDao;

    @PostMapping("/query")
    public BrandVO.QueryResp query(@RequestBody BrandVO.MdBrand request) {
        log.info("brand query:"+request);
        BrandVO.QueryResp result = new BrandVO.QueryResp();
        MdBrandParam mdBrand = new MdBrandParam();
        BeanUtils.copyProperties(request, mdBrand);
        mdBrand.setIsEnable(2);
        mdBrand.setIsDelete(1);
        mdBrand.setIsSale(1);
        mdBrand.setType(1);
        PageResult<MdBrand> query = mdBrandDao.query(mdBrand);
        if(!CollectionUtils.isEmpty(query.getItems())){
            List<BrandVO.MdBrand> mdBrands = new ArrayList<>();
            query.getItems().forEach(e->{
                BrandVO.MdBrand brand = new BrandVO.MdBrand();
                BeanUtils.copyProperties(e, brand);
                mdBrands.add(brand);
            });
            result.setMdBrands(mdBrands);
        }
        result.setTotal(query.getTotalCount());
        return result;
    }

    @PostMapping("/find")
    public BrandVO.QueryResp find(@RequestBody BrandVO.MdBrand request) {
        BrandVO.QueryResp result = new BrandVO.QueryResp();
        MdBrandParam mdBrand = new MdBrandParam();
        BeanUtils.copyProperties(request, mdBrand);
        mdBrand.setIsEnable(2);
        mdBrand.setIsDelete(1);
        mdBrand.setIsSale(1);
        mdBrand.setType(1);
        mdBrand.setBrandIds(request.getBrandIds());
        List<MdBrand> query = mdBrandDao.find(mdBrand);
        if(!CollectionUtils.isEmpty(query)){
            List<BrandVO.MdBrand> mdBrands = new ArrayList<>();
            query.forEach(e->{
                BrandVO.MdBrand brand = new BrandVO.MdBrand();
                BeanUtils.copyProperties(e, brand);
                mdBrands.add(brand);
            });
            result.setMdBrands(mdBrands);
        }

        return result;
    }

    @ApiOperation(value = "create", notes = "create")
    @PostMapping("/create")
    public BrandVO.Resp create(@RequestBody BrandVO.MdBrand request) {
        BrandVO.Resp result = new BrandVO.Resp();
        MdBrand mdBrand = new MdBrand();
        BeanUtils.copyProperties(request, mdBrand);
        mdBrandDao.add(mdBrand);
        return result;
    }

    @ApiOperation(value = "update", notes = "update")
    @PostMapping("/update")
    public BrandVO.Resp update(@RequestBody BrandVO.MdBrand request) {
        BrandVO.Resp result = new BrandVO.Resp();
        MdBrand mdBrand = new MdBrand();
        BeanUtils.copyProperties(request, mdBrand);
        mdBrandDao.update(mdBrand);
        return result;
    }
}
