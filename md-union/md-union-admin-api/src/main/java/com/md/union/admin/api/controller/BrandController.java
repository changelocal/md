package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.vo.Brand;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/brand")
@Api(tags = {"商标管理"})
public class BrandController {
    @Autowired
    private FrontClient frontClient;
    @ApiOperation("商标查询")
    @PostMapping("/query")
    public Brand.SearchRes query(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes ret = new Brand.SearchRes();
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        //名字
        req.setBrandName(request.getBrandName());
        //分类
        req.setCategory(request.getCategoryNo());
        //字符数
        req.setBrandNameLength(request.getBrandSize());
        if(request.getBrandSize()==1){
            req.setBrandNameLengthLow(1);
            req.setBrandNameLengthHigh(2);
        }
        else if (request.getBrandSize()==2){
            req.setBrandNameLength(3);
        }
        else if (request.getBrandSize()==3){
            req.setBrandNameLength(4);
        }
        else if (request.getBrandSize()==4){
            req.setBrandNameLength(5);
        }
        else if (request.getBrandSize()==5){
            req.setBrandNameLengthLow(6);
            req.setBrandNameLengthHigh(99);
        }
        //组合  0 不限
        req.setComType(request.getUnionType());
        //价格
        if(request.getPriceType()==1){
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(2000000));
        }else if (request.getPriceType()==2) {
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(10000));
        }else if (request.getPriceType()==3) {
            req.setPriceLow(new BigDecimal(10000));
            req.setPriceHigh(new BigDecimal(20000));
        }else if (request.getPriceType()==4) {
            req.setPriceLow(new BigDecimal(20000));
            req.setPriceHigh(new BigDecimal(50000));
        }else if (request.getPriceType()==5) {
            req.setPriceLow(new BigDecimal(50000));
            req.setPriceHigh(new BigDecimal(2000000));
        }
        req.setPageIndex(request.getPageIndex());
        req.setPageSize(request.getPageSize());

        BeanUtils.copyProperties(request, req);
        BaseResponse<TrademarkDTO.QueryResp> query = frontClient.search(req);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        if(!CollectionUtils.isEmpty(query.getResult().getMdBrands())){
            List<Brand.Info> infos = new ArrayList<>();
            query.getResult().getMdBrands().forEach(p->{
                Brand.Info info = new Brand.Info();
                BeanUtils.copyProperties(p, info);
                infos.add(info);
            });

            ret.setList(infos);
            ret.setCount(query.getResult().getTotal());
        }else{
            ret.setCount(0);
        }
        return ret;
    }

    @PostMapping("/update")
    public void update(@RequestBody Brand.Info request) {
        TrademarkDTO.MdBrand adminUser = new TrademarkDTO.MdBrand();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<TrademarkDTO.Resp> query = frontClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
    }

}