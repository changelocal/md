package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.Enums.BrandTypeEnums;
import com.md.union.front.api.Enums.ChangeEnums;
import com.md.union.front.api.Enums.DealEnums;
import com.md.union.front.api.Enums.RegisterEnums;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/brand")
@Api(tags = {"商标管理服务"})
@Slf4j
public class BrandController {

    @Autowired
    private FrontClient frontClient;
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ApiOperation("热门商标分类")
    @GetMapping("/hot/category")
    public List<Brand.HotRes> hotCategory() {
        List<Brand.HotRes> result = new ArrayList<>();

        BaseResponse<TrademarkDTO.HotResp> response = frontClient.hot();
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        response.getResult().getCates().forEach(e -> {
            Brand.HotRes brand = new Brand.HotRes();
            brand.setCategoryName(e.getCategoryName());
            brand.setCode(e.getCode());
            brand.setIcon(e.getIcon());
            brand.setTypeName(e.getCode()+"类");
            result.add(brand);
        });
        return result;
    }

    @ApiOperation("商标严选")
    @GetMapping("/list")
    public List<Brand.GroupRes> list() {
        List<Brand.GroupRes> result = new ArrayList<>();
//        BaseResponse<TrademarkDTO.HotClickResp> response = frontClient.hotClick();
//        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
//            throw new ServiceException(response.getStatus(), response.getMessage());
//        }
//        response.getResult().getHotTrademarks().forEach(e -> {
//            Brand.GroupRes brand = new Brand.GroupRes();
//            brand.setName(e.getName());
//            List<Brand.SpecialRes> specialRes = new ArrayList<>();
//            e.getTrademarks().forEach(f -> {
//
//                Brand.SpecialRes res = new Brand.SpecialRes();
//                res.setId(f.getId());
//                res.setBrandName(f.getName());
//                res.setImgUrl(f.getPic());
//                res.setMaxPrice(f.getMaxPrice());
//                res.setMinPrice(f.getMinPrice());
//                res.setSpecial(f.isSpecialPrice());
//
//                specialRes.add(res);
//            });



//        });
        Brand.GroupRes brand = new Brand.GroupRes();
        brand.setName("燃料油脂");
        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        for(int i=0;i<5;i++){
            Brand.SpecialRes res = new Brand.SpecialRes();
            res.setId("id"+i);
            res.setBrandName("name"+i);
            res.setImgUrl("http://ytmd-library.oss-cn-beijing.aliyuncs.com/26/0001-4c11-8cd7-5a926ecab9ac-cd30428fecf0-9a342.gif");
            res.setMaxPrice("12345");
            res.setMinPrice("123");
            res.setSpecial(true);

            specialRes.add(res);

        }
        brand.setList(specialRes);
        result.add(brand);
        brand = new Brand.GroupRes();
        brand.setName("金属材料");
        brand.setList(specialRes);
        result.add(brand);
        brand = new Brand.GroupRes();
        brand.setName("机械设备");
        brand.setList(specialRes);
        result.add(brand);
        return result;
    }

    /**
     * 查商标，准备购买
     * @param request
     * @return
     */
    @ApiOperation("买商标查询")
    @PostMapping("/search")
    public Brand.SearchRes brandSearch(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes result = new Brand.SearchRes();

        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setBrandName(request.getBrandName());
        req.setCategory(request.getCategoryNo());
        req.setBrandNameLength(request.getBrandSize());
        req.setComType(request.getUnionType());
//        req.setPrice(request.getPriceType());
        if(request.getPriceType()==1){
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(2000000));
        }else if (request.getPriceType()==2) {
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(10000));
        }else if (request.getPriceType()==3) {
            req.setPriceLow(new BigDecimal(10000));
            req.setPriceHigh(new BigDecimal(20000));
        }else if (request.getPriceType()==3) {
            req.setPriceLow(new BigDecimal(20000));
            req.setPriceHigh(new BigDecimal(50000));
        }else if (request.getPriceType()==3) {
            req.setPriceLow(new BigDecimal(50000));
            req.setPriceHigh(new BigDecimal(2000000));
        }

        req.setPageIndex(request.getPageIndex());
        req.setPageSize(request.getPageSize());

        BaseResponse<TrademarkDTO.QueryResp> response = frontClient.search(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e -> {
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setBrandName(e.getBrandName());
                res.setCategoryName(e.getCategory()+"");
                res.setId(e.getId());
                res.setImgUrl(e.getImageUrl());
                res.setSpecial(e.getPromoteFlag()==1);
                res.setMinPrice(e.getPrice().toString());
                res.setMaxPrice(e.getPrice().toString());
                specialRes.add(res);
            });
        }

        result.setList(specialRes);
        result.setTotal(response.getResult().getTotal());
        return result;
    }

    @ApiOperation("买商标分类查询")
    @PostMapping("/category")
    public Category.SearchRes categorySearch(@RequestBody Category.SearchReq request) {
        Category.SearchRes result = new Category.SearchRes();
        List<Category.Info> infos = new ArrayList<>();
        for(int i = 0;i<5;i++){
            Category.Info e = new Category.Info();
            e.setCategoryName("name"+i);
            e.setCategoryNo(""+i);
            e.setDesc("desc");
            e.setIcon("icon");
            e.setRegisterStatus(true);

        }
        result.setList(infos);
        result.setCount(5);
        return result;
    }

    @ApiOperation("购买商标维权详情信息(服务类订单) brandType(1商标注册2维权3信息变更) 主键")
    @GetMapping("/deal/detail/{brandType}/{id}")
    public Brand.DealDetail dealDtail(@PathVariable("brandType") int brandType, @PathVariable("id") int id) {
        //Brand.DealDetail result = new Brand.DealDetail();

        return getBrandDetail(brandType, id);
    }

    /**
     * 查到商标以后，点击购买
     * @param brandName
     * @return
     */
    @ApiOperation("购买商标详情")
    @PostMapping("/buy/detail")
    public Brand.Detail buyDetail( String brandName) {
        Brand.Detail result = new Brand.Detail();

        //获取商标list
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setBrandName(brandName);
        BaseResponse<TrademarkDTO.QueryResp> response = frontClient.find(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        //得到45大类
        BaseResponse<TrademarkDTO.RootBrandResp> responseCate = frontClient.root();
        if (!responseCate.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseCate.getStatus(), responseCate.getMessage());
        }
        Map<Integer,String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p->p.getCode(), q->q.getCategoryName()));

        List<Brand.TrademarkCate> trademarkCates = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e -> {
                Brand.TrademarkCate res = new Brand.TrademarkCate();
                res.setCateCode(e.getCategory());
                res.setCateName(name.get(e.getCategory()));
                res.setDeposit("666");
                res.setId(e.getId());
                res.setName(e.getBrandName());
                res.setPic(e.getImageUrl());
                trademarkCates.add(res);

                result.setFirstDate(null == e.getFirstCheckTime()?"":timeFormat.format(e.getFirstCheckTime()));
                result.setFirstNo(e.getFirstCheckId());
                result.setEndDate(null ==e.getTimeLimitEnd()?"":timeFormat.format(e.getTimeLimitEnd()));
                result.setSignUpDate(null == e.getRegisterCheckTime()?"":timeFormat.format(e.getRegisterCheckTime()));
                result.setSignUpNo(e.getRegisterCheckId());
                result.setTrademarkType(BrandTypeEnums.valueType(e.getComType()).getTitle());

                result.setLikeCate(e.getGroup());
                result.setUsage(e.getFitProjects());
                result.setColor(e.getThemeColor());

                result.setBigPic(e.getImageUrl());
                result.setConcept("设计理念");

            });
        }
        result.setTrademarkCateList(trademarkCates);

        //获取咨询人信息
        TrademarkDTO.Consultation consultation = new TrademarkDTO.Consultation();
        consultation.setId("");
        BaseResponse<TrademarkDTO.ConsultationResp> responsePerson = frontClient.consultation(consultation);
        if (!responsePerson.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responsePerson.getStatus(), responsePerson.getMessage());
        }

        Brand.Person person = new Brand.Person();
        //todo img
        person.setHeadImg(responsePerson.getResult().getNickname());
        person.setName(responsePerson.getResult().getNickname());
        person.setPhone(responsePerson.getResult().getMobile());
        person.setQq(responsePerson.getResult().getQqAccount());
        result.setPerson(person);

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
