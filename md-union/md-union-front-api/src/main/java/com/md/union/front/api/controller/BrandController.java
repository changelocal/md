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
import java.util.HashMap;
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

    /**
     * 八大热门分类
     * @return
     */
    @ApiOperation("热门商标分类")
    @GetMapping("/hot/category")
    public Brand.HotList hotCategory() {
        Brand.HotList res = new Brand.HotList();
        List<Brand.HotRes> result = new ArrayList<>();

        BaseResponse<TrademarkDTO.HotResp> response = frontClient.hot();
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Brand.GroupRes> groups = new ArrayList<>();
        response.getResult().getCates().forEach(e -> {
            Brand.HotRes brand = new Brand.HotRes();
            brand.setCategoryName(e.getCategoryName());
            brand.setCode(e.getCode());
            brand.setIcon(e.getIcon());
            brand.setTypeName(e.getCode()+"类");
            result.add(brand);

            Brand.GroupRes list = list(e.getCode());
            groups.add(list);
        });
        res.setCate(result);
        res.setGroup(groups);

        return res;
    }

    /**
     * 点击热门分类后获取几个热门商标
     * @param code
     * @return
     */
//    @ApiOperation("商标严选")
//    @GetMapping("/list/{code}")
    private Brand.GroupRes list(int code) {

        //获取商标list
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setCategory(code);
        req.setIsQuality(2);
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

        Brand.GroupRes brand = new Brand.GroupRes();
        brand.setCode(code);
        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e->{
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setId(e.getId());
                res.setCategoryName(e.getCategory()+"类 "+name.get(e.getCategory()));
                res.setBrandName(e.getBrandName());
                res.setImgUrl(e.getImageUrl());
                res.setMaxPrice(e.getPrice().toString());
                res.setMinPrice(e.getPrice().toString());
                res.setSpecial(true);
                specialRes.add(res);
            });
        }
        brand.setList(specialRes);
        return brand;
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
            req.setBrandNameLengthLow(5);
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

        BaseResponse<TrademarkDTO.QueryResp> response = frontClient.search(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        //得到45大类
        BaseResponse<TrademarkDTO.RootBrandResp> responseCate = frontClient.root();
        if (!responseCate.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseCate.getStatus(), responseCate.getMessage());
        }
        Map<Integer,String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p->p.getCode(), q->q.getCategoryName()));

        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e -> {
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setBrandName(e.getBrandName());
                res.setCategoryName(e.getCategory()+"类 "+name.get(e.getCategory()));
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
     * @param request
     * @return
     */
    @ApiOperation("购买商标详情")
    @PostMapping("/buy/detail")
    public Brand.Detail buyDetail(@RequestBody Category.BuyDetailReq request) {
        Brand.Detail result = new Brand.Detail();

        //获取商标list
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setBrandName(request.getBrandName());
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
                res.setCateName(e.getCategory()+"类 "+name.get(e.getCategory()));
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

    /**
     * 首页查商标的详情，有成功率的
     * @param request
     * @return
     */
    @ApiOperation("首页查商标的详情")
    @PostMapping("/home/search")
    public Brand.SearchDetail searchDetail(@RequestBody Category.BuyDetailReq request) {
        Brand.SearchDetail result = new Brand.SearchDetail();

        //获取商标list
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        req.setBrandName(request.getBrandName());
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

        Map<String,String> ids = new HashMap<>();
        Map<Integer,String> codes = new HashMap<>();
        List<Brand.TrademarkCateSearch> trademarkCates = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e -> {
                Brand.TrademarkCateSearch res = new Brand.TrademarkCateSearch();
                res.setCateCode(e.getCategory());
                res.setCateName(e.getCategory()+"类 "+name.get(e.getCategory()));
                res.setId(e.getId());
                res.setName(e.getBrandName());
                trademarkCates.add(res);
            });
            ids = response.getResult().getMdBrands().stream().collect(Collectors.toMap(p->p.getId(), q->q.getId()));
            codes = response.getResult().getMdBrands().stream().collect(Collectors.toMap(p->p.getCategory(), q->q.getId()));
        }
        result.setTrademarkCateListRegist(trademarkCates);
        result.setRegistCount(trademarkCates.size());
        result.setTotal(trademarkCates.size());
        result.setSuccess("34");

        //未注册的
        List<Brand.TrademarkCateSearch> trademarkCatesUnRegist = new ArrayList<>();
        for (TrademarkDTO.Cate cate : responseCate.getResult().getCates()) {
            if (codes.containsKey(cate.getCode())) {
                continue;
            }
            Brand.TrademarkCateSearch res = new Brand.TrademarkCateSearch();
            res.setCateCode(cate.getCode());
            res.setCateName(cate.getCode() + "类 " + cate.getCategoryName());
            trademarkCatesUnRegist.add(res);
        }
        result.setTrademarkCateListUnRegist(trademarkCatesUnRegist);
        result.setUnRegistCount(trademarkCatesUnRegist.size());

        //相似商标
        TrademarkDTO.MdBrand requestFamilar = new TrademarkDTO.MdBrand();
        requestFamilar.setBrandName(request.getBrandName());
        requestFamilar.setPriceHigh(new BigDecimal(0));
        requestFamilar.setPriceLow(new BigDecimal(0));
        requestFamilar.setPageIndex(1);
        requestFamilar.setPageSize(10);
        BaseResponse<TrademarkDTO.QueryResp> responseFamilar = frontClient.search(requestFamilar);
        if (!responseFamilar.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseFamilar.getStatus(), responseFamilar.getMessage());
        }
        List<Brand.SpecialRes> trademarkCatesFamilar = new ArrayList<>();
        if(!CollectionUtils.isEmpty(responseFamilar.getResult().getMdBrands())){
            for (TrademarkDTO.MdBrand e : responseFamilar.getResult().getMdBrands()) {
                if (ids.containsKey(e.getId())) {
                    continue;
                }
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setId(e.getId());
                res.setCategoryName(e.getCategory() + "类 " + name.get(e.getCategory()));
                res.setBrandName(e.getBrandName());
                res.setImgUrl(e.getImageUrl());
                res.setMinPrice(e.getPrice().toString());
                res.setMaxPrice(e.getPrice().toString());
                res.setSpecial(e.getPromoteFlag()==1);
                trademarkCatesFamilar.add(res);
            }
        }
        result.setFamiliar(trademarkCatesFamilar);

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
            result.setImgUrl(register.getIcon());
            result.setBuyPrice("￥" + register.getPrice());
            result.setMarketPrice("￥" + (register.getPrice() + 500));
            result.setTotal(10);
            result.setPerson(getPerson());
        } else if (brandType == 2) {
            DealEnums deal = DealEnums.valueType(id);
            result.setImgUrl(deal.getIcon());
            result.setBuyPrice("￥" + deal.getPrice());
            result.setMarketPrice("￥" + (deal.getPrice() + 500));
            result.setTotal(20);
            result.setPerson(getPerson());
        } else if (brandType == 3) {
            ChangeEnums change = ChangeEnums.valueType(id);
            result.setImgUrl(change.getIcon());
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
