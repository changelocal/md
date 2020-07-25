package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.Enums.ChangeEnums;
import com.md.union.front.api.Enums.DealEnums;
import com.md.union.front.api.Enums.RegisterEnums;
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
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        response.getResult().getHotTrademarkCates().forEach(e -> {
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
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        response.getResult().getHotTrademarks().forEach(e -> {
            Brand.GroupRes brand = new Brand.GroupRes();
            brand.setName(e.getName());
            List<Brand.SpecialRes> specialRes = new ArrayList<>();
            e.getTrademarks().forEach(f -> {

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
        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Brand.SpecialRes res = new Brand.SpecialRes();
            res.setBrandName("name"+i);
            res.setCategoryName("cate"+i);
            res.setId(""+i);
            res.setImgUrl("pic"+i);
            res.setSpecial(true);
            specialRes.add(res);
        }

        result.setList(specialRes);
        result.setTotal(5);
        return result;

//        TrademarkDTO.Search req = new TrademarkDTO.Search();
//        req.setName(request.getBrandName());
//        req.setCategory(request.getCategoryNo());
//        req.setCharacter(request.getBrandSize());
//        req.setCombination(request.getUnionType());
//        req.setPrice(request.getPriceType());
//
//        req.setPageIndex(request.getPageIndex());
//        req.setPageSize(request.getPageSize());
//
//        BaseResponse<TrademarkDTO.SearchResp> response = frontClient.search(req);
//        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
//            throw new ServiceException(response.getStatus(), response.getMessage());
//        }
//        response.getResult().getTrademarklist().forEach(e -> {
//            Brand.SpecialRes res = new Brand.SpecialRes();
//            res.setBrandName(e.getName());
//            res.setCategoryName(e.getName());
//            res.setId(e.getId());
//            res.setImgUrl(e.getPic());
//            res.setSpecial(e.isSpecialPrice());
//
//        });
//
//        result.setTotal(response.getResult().getTotal());
//        return result;
    }

    @ApiOperation("买商标分类查询")
    @PostMapping("/category")
    public Category.SearchRes categorySearch(@RequestBody Category.SearchReq request) {
        Category.SearchRes result = new Category.SearchRes();

        return result;
    }

    @ApiOperation("购买商标维权详情信息(服务类订单) brandType(1商标注册2维权3信息变更) 主键")
    @GetMapping("/deal/detail/{brandType}/{id}/")
    public Brand.DealDetail dealDtail(@PathVariable("brandType") int brandType, @PathVariable("id") int id) {
        //Brand.DealDetail result = new Brand.DealDetail();

        return getBrandDetail(brandType, id);
    }

    @ApiOperation("购买商标详情")
    @PostMapping("/buy/detail/{brandId}")
    public Brand.Detail buyDetail(@PathVariable("brandId") String brandId) {
        Brand.Detail result = new Brand.Detail();

        TrademarkDTO.Purchase req = new TrademarkDTO.Purchase();
        req.setId(brandId);
        BaseResponse<TrademarkDTO.PurchaseResp> response = frontClient.detail(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        result.setImgUrl(response.getResult().getBigPic());
        result.setPrePrice(response.getResult().getDeposit());
        result.setCategoryName(response.getResult().getConcept());
        return result;

    }

    private Brand.Person getPerson() {
        Brand.Person person = new Brand.Person();
        person.setHeadImg("http://pic.5tu.cn/uploads/allimg/1607/pic_5tu_big_201607221326573826.jpg");
        person.setName("客服1");
        person.setPhone("15688706317");
        person.setQq("571660498");
        return person;
    }

    private Brand.DealDetail getBrandDetail(int brandType, int id) {
        Brand.DealDetail result = new Brand.DealDetail();
        if (brandType == 1) {
            RegisterEnums register = RegisterEnums.valueType(id);
            result.setImgUrl(register.getImg());
            result.setBuyPrice("￥" + register.getPrice());
            result.setMarketPrice("￥" + (register.getPrice() + 500));
            result.setTotal(10);
            result.setPerson(getPerson());
        } else if (brandType == 2) {
            DealEnums deal = DealEnums.valueType(id);
            result.setImgUrl(deal.getImg());
            result.setBuyPrice("￥" + deal.getPrice());
            result.setMarketPrice("￥" + (deal.getPrice() + 500));
            result.setTotal(20);
            result.setPerson(getPerson());
        } else if (brandType == 3) {
            ChangeEnums change = ChangeEnums.valueType(id);
            result.setImgUrl(change.getImg());
            result.setBuyPrice("￥" + change.getPrice());
            result.setMarketPrice("￥" + (change.getPrice() + 500));
            result.setTotal(30);
            result.setPerson(getPerson());
        } else {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "商标类型不存在");
        }
        return result;
    }


}
