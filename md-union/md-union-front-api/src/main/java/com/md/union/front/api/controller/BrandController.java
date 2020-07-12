package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/brand")
@Api(tags = {"商标管理服务"})
public class BrandController {

    @Autowired
    private FrontClient frontClient;

    @ApiOperation("热门商标分类")
    @GetMapping("/hot/category")
    public List<Brand.HotRes> hotCategory() {
        List<Brand.HotRes> result = new ArrayList<>();

        BaseResponse<TrademarkDTO.HotResp> response = frontClient.hot();
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        response.getResult().getHotTrademarkCates().forEach(e->{
            Brand.HotRes brand = new Brand.HotRes();
            brand.setCategoryName(e.getName());
            brand.setId(e.getId());
            brand.setIcon(e.getIcon());
            result.add(brand);
        });
        return result;
    }

    @ApiOperation("商标严选")
    @GetMapping("/list")
    public List<Brand.GroupRes> list() {
        List<Brand.GroupRes> result = new ArrayList<>();
        BaseResponse<TrademarkDTO.HotClickResp> response = frontClient.hotClick();
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        response.getResult().getHotTrademarks().forEach(e->{
            Brand.GroupRes brand = new Brand.GroupRes();
            brand.setName(e.getName());
            List<Brand.SpecialRes> specialRes = new ArrayList<>();
            e.getTrademarks().forEach(f->{

                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setId(f.getId());
                res.setBrandName(f.getName());
                res.setImgUrl(f.getPic());
                res.setMaxPrice(f.getMaxPrice());
                res.setMinPrice(f.getMinPrice());
                res.setSpecial(f.isSpecialPrice());

                specialRes.add(res);
            });

            brand.setList(specialRes);

            result.add(brand);
        });

        return result;
    }

    @ApiOperation("买商标查询")
    @PostMapping("/search")
    public Brand.SearchRes brandSearch(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes result = new Brand.SearchRes();

        TrademarkDTO.Search req = new TrademarkDTO.Search();
        req.setName(request.getBrandName());
        req.setCategory(request.getCategoryNo());
        req.setCharacter(request.getBrandSize());
        req.setCombination(request.getUnionType());
        req.setPrice(request.getPriceType());

        req.setPageIndex(request.getPageIndex());
        req.setPageSize(request.getPageSize());

        BaseResponse<TrademarkDTO.SearchResp> response = frontClient.search(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        response.getResult().getTrademarklist().forEach(e->{
            Brand.SpecialRes res = new Brand.SpecialRes();
            res.setBrandName(e.getName());
            res.setCategoryName(e.getName());
            res.setId(e.getId());
            res.setImgUrl(e.getPic());
            res.setSpecial(e.isSpecialPrice());

        });

        result.setTotal(response.getResult().getTotal());
        return result;
    }

    @ApiOperation("买商标分类查询")
    @PostMapping("/category")
    public Category.SearchRes categorySearch(@RequestBody Category.SearchReq request) {
        Category.SearchRes result = new Category.SearchRes();

        return result;
    }

    @ApiOperation("购买商标维权详情信息(服务类订单)")
    @GetMapping("/deal/detail/{typeId}")
    public Brand.DealDetail dealDtail(@PathVariable("typeId") String typeId) {
        Brand.DealDetail result = new Brand.DealDetail();

        return result;

    }

    @ApiOperation("购买商标详情")
    @PostMapping("/buy/detail/{brandId}")
    public Brand.Detail buyDetail(@PathVariable("brandId") String brandId) {
        Brand.Detail result = new Brand.Detail();

        TrademarkDTO.Purchase req = new TrademarkDTO.Purchase();
        req.setId(brandId);
        BaseResponse<TrademarkDTO.PurchaseResp> response = frontClient.detail(req);
        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
            throw new ServiceException(response.getStatus(),response.getMessage());
        }
        result.setImgUrl(response.getResult().getBigPic());
        result.setPrePrice(response.getResult().getDeposit());
        result.setCategoryName(response.getResult().getConcept());
        return result;

    }



}
