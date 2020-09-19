package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.arc.util.tmkoo.Tmkoo;
import com.arc.util.tmkoo.TmkooCommon;
import com.md.union.front.api.Enums.BrandTypeEnums;
import com.md.union.front.api.facade.MinCommon;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.api.vo.Category;
import com.md.union.front.client.dto.SearchRecordDTO;
import com.md.union.front.client.dto.ServiceDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/front/brand")
@Api(tags = {"商标管理服务"})
@Slf4j
public class BrandController {
    @Autowired
    private MinCommon minCommon;
    @Autowired
    private FrontClient frontClient;
    SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 八大热门分类
     *
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
            brand.setIcon("https://pay.mdlogo.cn/file/brand_class/" + e.getCode() + ".png");
            brand.setTypeName(e.getCode() + "类");
            result.add(brand);

            Brand.GroupRes list = list(e.getCode(), e.getCategoryName());
            groups.add(list);
        });
        res.setCate(result);
        res.setGroup(groups);

        return res;
    }

    /**
     * 点击热门分类后获取几个热门商标
     *
     * @param code
     * @return
     */
    private Brand.GroupRes list(int code, String cateName) {
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
        Map<Integer, String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p -> p.getCode(), q -> q.getCategoryName()));

        Brand.GroupRes brand = new Brand.GroupRes();
        brand.setCode(code);
        brand.setName(cateName);
        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(response.getResult().getMdBrands())) {
            response.getResult().getMdBrands().forEach(e -> {
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setId(e.getId());
                res.setCategoryName(e.getCategory() + "类 " + name.get(e.getCategory()));
                res.setBrandName(e.getBrandName());
                res.setImgUrl(e.getImageUrl());
                res.setMaxPrice(e.getPriceHigh().toString());
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
     *
     * @param request
     * @return
     */
    @ApiOperation("买商标查询")
    @PostMapping("/search")
    public Brand.SearchRes brandSearch(@RequestBody Brand.SearchReq request) {
        log.info("brand search:", request);
        Brand.SearchRes result = new Brand.SearchRes();

        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        //名字
        req.setBrandName(request.getBrandName());
        //分类
        req.setCategory(request.getCategoryNo());
        //字符数
        req.setBrandNameLength(request.getBrandSize());
        if (request.getBrandSize() == 1) {
            req.setBrandNameLengthLow(1);
            req.setBrandNameLengthHigh(2);
        } else if (request.getBrandSize() == 2) {
            req.setBrandNameLength(3);
        } else if (request.getBrandSize() == 3) {
            req.setBrandNameLength(4);
        } else if (request.getBrandSize() == 4) {
            req.setBrandNameLengthLow(5);
            req.setBrandNameLengthHigh(99);
        }

        //组合  0 不限
        req.setComType(request.getUnionType());
        //价格
        if (request.getPriceType() == 1) {
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(2000000));
        } else if (request.getPriceType() == 2) {
            req.setPriceLow(new BigDecimal(1));
            req.setPriceHigh(new BigDecimal(10000));
        } else if (request.getPriceType() == 3) {
            req.setPriceLow(new BigDecimal(10000));
            req.setPriceHigh(new BigDecimal(20000));
        } else if (request.getPriceType() == 4) {
            req.setPriceLow(new BigDecimal(20000));
            req.setPriceHigh(new BigDecimal(50000));
        } else if (request.getPriceType() == 5) {
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
        Map<Integer, String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p -> p.getCode(), q -> q.getCategoryName()));

        List<Brand.SpecialRes> specialRes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(response.getResult().getMdBrands())) {
            response.getResult().getMdBrands().forEach(e -> {
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setBrandName(e.getBrandName());
                res.setCategoryName(e.getCategory() + "类 " + name.get(e.getCategory()));
                res.setId(e.getId());
                res.setImgUrl(e.getImageUrl());
                res.setSpecial(e.getPromoteFlag() == 1);
                res.setMinPrice(e.getPrice().toString());
                res.setMaxPrice(e.getPriceHigh().toString());
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
        for (int i = 0; i < 5; i++) {
            Category.Info e = new Category.Info();
            e.setCategoryName("name" + i);
            e.setCategoryNo("" + i);
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
    public Brand.DealDetail dealDtail(@PathVariable("brandType") int brandType, @PathVariable("id") String id) {
        //Brand.DealDetail result = new Brand.DealDetail();

        return getBrandDetail(brandType, id);
    }

    /**
     * 查到商标以后，点击购买
     *
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
        Map<Integer, String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p -> p.getCode(), q -> q.getCategoryName()));

        List<Brand.TrademarkCate> trademarkCates = new ArrayList<>();
        AtomicInteger cate = new AtomicInteger();
        AtomicReference<String> regNo = new AtomicReference<>("");
        if (!CollectionUtils.isEmpty(response.getResult().getMdBrands())) {
            response.getResult().getMdBrands().forEach(e -> {
                Brand.TrademarkCate res = new Brand.TrademarkCate();
                res.setCateCode(e.getCategory());
                res.setCateName(e.getCategory() + "类 " + name.get(e.getCategory()));
                res.setDeposit(e.getPrice().multiply(new BigDecimal(0.2)).toString());
                res.setPriceLow(e.getPrice().toString());
                res.setPriceHigh(e.getPriceHigh().toString());
                res.setId(e.getId());
                res.setName(e.getBrandName());
                res.setPic(e.getImageUrl());
                trademarkCates.add(res);

                result.setFirstDate(null == e.getFirstCheckTime() ? "" : timeFormat.format(e.getFirstCheckTime()));
                result.setFirstNo(e.getFirstCheckId() == null ? "" : e.getFirstCheckId());
                result.setEndDate(null == e.getTimeLimitEnd() ? "" : timeFormat.format(e.getTimeLimitEnd()));
                result.setSignUpDate(null == e.getRegisterCheckTime() ? "" : timeFormat.format(e.getRegisterCheckTime()));
                result.setSignUpNo(e.getRegisterCheckId() == null ? "" : e.getRegisterCheckId());
                result.setTrademarkType(BrandTypeEnums.valueType(e.getComType()).getTitle());

                result.setLikeCate(e.getGroup());
                result.setUsage(e.getFitProjects());
                result.setColor(e.getThemeColor());

                result.setBigPic(e.getImageUrl());

                String fit = e.getFitCategory()==null? e.getBrandName():e.getFitCategory();

                String concept = " 简约又不失时尚的【"+e.getBrandName()+"】是属于【"+res.getCateName()+"】商标， 该商标设计匠心独运，构思大胆且贴近商品，" +
                        "传达出与众不同的独特意义。同时符合使用在【"+fit+"】等领域。目前的最新状态是商标注册申请完成," +
                        "使用期限于 【"+timeFormat.format(e.getOwnTimeEnd())+"】到期。商标是走向市场，打造品牌的第一 步，亚太名典知识产权助您第一时间拥有自己的品牌!\n" +
                        "\n" +
                        "注册类别分析：当前商标属于【"+res.getCateName()+"】类商品，主要用于"+fit+"行业核心类别。价格从20000到90000不等，" +
                        "总体来说价值比一般的商标要高得多。随着商标名称不断得被注册，价值还会攀升。";
                result.setConcept(concept);
                result.setBrandNo(e.getBrandId());

                cate.set(e.getCategory());
                regNo.set(e.getRegNo());
            });
        }
        result.setTrademarkCateList(trademarkCates);

        //获取咨询人信息
        TrademarkDTO.Consultation consultation = new TrademarkDTO.Consultation();
        BaseResponse<TrademarkDTO.ConsultationResp> responsePerson = frontClient.consultation(consultation);
        if (!responsePerson.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responsePerson.getStatus(), responsePerson.getMessage());
        }

        Brand.Person person = new Brand.Person();
        person.setTitle(responsePerson.getResult().getTitle());
        person.setHeadImg(responsePerson.getResult().getAvatar());
        person.setName(responsePerson.getResult().getNickname());
        person.setPhone(responsePerson.getResult().getMobile());
        person.setQq(responsePerson.getResult().getQqAccount());
        person.setId(responsePerson.getResult().getId());
        result.setPerson(person);
        result.setOrderType("4");

        if (null != regNo.get()) {
            Tmkoo.Flow info = TmkooCommon.info(minCommon.getHost(), regNo.get(), cate.get());
            List<Brand.FlowInfo> flowInfos = new ArrayList<>();
            for (Tmkoo.FlowInfo p : info.getFlowInfos()) {
                Brand.FlowInfo flowInfo = new Brand.FlowInfo();
                flowInfo.setDate(p.getDate());
                flowInfo.setName(p.getName());
                flowInfos.add(flowInfo);
            }
            result.setFlowInfos(flowInfos);
        }

        return result;
    }

    /**
     * 首页查商标的详情，有成功率的
     *
     * @param request
     * @return
     */
    @ApiOperation("首页查商标的详情")
    @PostMapping("/home/search")
    public Brand.SearchDetail searchDetail(@RequestBody Category.BuyDetailReq request) throws UnsupportedEncodingException {
        Brand.SearchDetail result = new Brand.SearchDetail();
        //得到45大类
        BaseResponse<TrademarkDTO.RootBrandResp> responseCate = frontClient.root();
        if (!responseCate.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseCate.getStatus(), responseCate.getMessage());
        }
        Map<Integer, String> name = responseCate.getResult().getCates().stream().collect(Collectors.toMap(p -> p.getCode(), q -> q.getCategoryName()));

        //获取商标list
        SearchRecordDTO.SearchRecordInfo req = new SearchRecordDTO.SearchRecordInfo();
        req.setSearchWord(request.getBrandName());
        req.setPageIndex(1);
        req.setPageSize(10);
        BaseResponse<SearchRecordDTO.QueryResp> response = frontClient.query(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Brand.TrademarkCateSearch> trademarkCatesRegis = new ArrayList<>();
        List<Brand.TrademarkCateSearch> trademarkCatesUnRegis = new ArrayList<>();
        //数据库有记录
        if (!CollectionUtils.isEmpty(response.getResult().getInfos()) && response.getResult().getInfos().size() > 0) {
            List<String> regis = Arrays.asList(response.getResult().getInfos().get(0).getRegistCate().split(","));
            List<String> regisUn = Arrays.asList(response.getResult().getInfos().get(0).getUnregistCate().split(","));
            responseCate.getResult().getCates().forEach(p -> {
                if (regis.contains(p.getCode())) {
                    Brand.TrademarkCateSearch info = new Brand.TrademarkCateSearch();
                    if (p.getCode() < 10) {
                        info.setCateName("0" + p.getCode() + "-" + p.getCategoryName());
                    } else {
                        info.setCateName(p.getCode() + "-" + p.getCategoryName());
                    }
                    info.setCateCode(p.getCode());
                    trademarkCatesRegis.add(info);
                } else if (regisUn.contains(p.getCode())) {
                    Brand.TrademarkCateSearch info = new Brand.TrademarkCateSearch();
                    if (p.getCode() < 10) {
                        info.setCateName("0" + p.getCode() + "-" + p.getCategoryName());
                    } else {
                        info.setCateName(p.getCode() + "-" + p.getCategoryName());
                    }
                    info.setCateCode(p.getCode());
                    trademarkCatesUnRegis.add(info);
                } else {
                    Brand.TrademarkCateSearch info = new Brand.TrademarkCateSearch();
                    if (p.getCode() < 10) {
                        info.setCateName("0" + p.getCode() + "-" + p.getCategoryName());
                    } else {
                        info.setCateName(p.getCode() + "-" + p.getCategoryName());
                    }
                    info.setCateCode(p.getCode());
                    trademarkCatesUnRegis.add(info);
                }
            });
            result.setTotal(1);
        }
        //数据库没有记录，要发请求
        else {
            Tmkoo.Result search = TmkooCommon.search(minCommon.getHost(), request.getBrandName());
            if (!CollectionUtils.isEmpty(search.getRegisters())) {

                List<Integer> collect = search.getRegisters().stream().map(p -> p.getCate()).collect(Collectors.toList());
                responseCate.getResult().getCates().forEach(p -> {
                    if (collect.contains(p.getCode())) {
                        Brand.TrademarkCateSearch info = new Brand.TrademarkCateSearch();
                        if (p.getCode() < 10) {
                            info.setCateName("0" + p.getCode() + "-" + p.getCategoryName());
                        } else {
                            info.setCateName(p.getCode() + "-" + p.getCategoryName());
                        }
                        info.setCateCode(p.getCode());
                        trademarkCatesRegis.add(info);
                    } else {
                        Brand.TrademarkCateSearch info = new Brand.TrademarkCateSearch();
                        if (p.getCode() < 10) {
                            info.setCateName("0" + p.getCode() + "-" + p.getCategoryName());
                        } else {
                            info.setCateName(p.getCode() + "-" + p.getCategoryName());
                        }
                        info.setCateCode(p.getCode());
                        trademarkCatesUnRegis.add(info);
                    }
                });

                //  增加到数据库
                SearchRecordDTO.SearchRecordInfo insert = new SearchRecordDTO.SearchRecordInfo();
                insert.setSearchWord(search.getBrandName());
                insert.setRegistNo(StringUtils.join(search.getRegNos(), ","));

                List<Integer> collect1 = search.getRegisters().stream().map(q -> q.getCate()).collect(Collectors.toList());
                insert.setRegistCate(StringUtils.join(collect1, ","));
                List<Integer> collect2 = trademarkCatesUnRegis.stream().map(q -> q.getCateCode()).collect(Collectors.toList());
                insert.setUnregistCate(StringUtils.join(collect2, ","));

//                insert.setBuyerMobile(AppUserPrincipal.getPrincipal().getMobile());
//                insert.setOpenId(AppUserPrincipal.getPrincipal().getMinId());
                insert.setBuyerMobile("8964");
                insert.setOpenId("999999");
                insert.setStatus(1);
                BaseResponse<SearchRecordDTO.Resp> update = frontClient.add(insert);
                if (!update.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                    throw new ServiceException(update.getStatus(), update.getMessage());
                }
                result.setTotal(1);
            } else {
                result.setTotal(0);
            }
        }

        result.setTrademarkCateListRegist(trademarkCatesRegis);
        result.setRegistCount(trademarkCatesRegis.size());
        result.setSuccess("34");
        result.setTrademarkCateListUnRegist(trademarkCatesUnRegis);
        result.setUnRegistCount(trademarkCatesUnRegis.size());

        //****************************************相似商标
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
        if (!CollectionUtils.isEmpty(responseFamilar.getResult().getMdBrands())) {
            for (TrademarkDTO.MdBrand e : responseFamilar.getResult().getMdBrands()) {
                if (request.getBrandName().equals(e.getBrandName())) {
                    continue;
                }
                Brand.SpecialRes res = new Brand.SpecialRes();
                res.setId(e.getId());
                res.setCategoryName(e.getCategory() + "类 " + name.get(e.getCategory()));
                res.setBrandName(e.getBrandName());
                res.setImgUrl(e.getImageUrl());
                res.setMinPrice(e.getPrice().toString());
                res.setMaxPrice(e.getPriceHigh().toString());
                res.setSpecial(e.getPromoteFlag() == 1);
                trademarkCatesFamilar.add(res);
            }
        }
        result.setFamiliar(trademarkCatesFamilar);
        return result;
    }

    private Brand.DealDetail getBrandDetail(int brandType, String id) {
        Brand.DealDetail result = new Brand.DealDetail();

        BaseResponse<ServiceDTO.Service> responseFamilar = frontClient.getService(id);
        if (!responseFamilar.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseFamilar.getStatus(), responseFamilar.getMessage());
        }

        TrademarkDTO.Consultation consultation = new TrademarkDTO.Consultation();
        //consultation.setId(id);
        BaseResponse<TrademarkDTO.ConsultationResp> response = frontClient.consultation(consultation);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        Brand.Person person = new Brand.Person();
        person.setTitle(response.getResult().getTitle());
        person.setQq(response.getResult().getQqAccount());
        person.setPhone(response.getResult().getMobile());
        person.setHeadImg(response.getResult().getAvatar());
        person.setName(response.getResult().getNickname());
        person.setId(response.getResult().getId());


        result.setTitle(responseFamilar.getResult().getServiceName());
        result.setSubTitle(responseFamilar.getResult().getSubTitle());
        result.setImgUrl(responseFamilar.getResult().getImageUrl());
        result.setBuyPrice("￥" + responseFamilar.getResult().getPrice());
        result.setMarketPrice("￥" + (responseFamilar.getResult().getPrice().add(new BigDecimal(500))));
        result.setTotal(10);
        result.setPerson(person);
        result.setDes(responseFamilar.getResult().getDes());
        result.setId(id);
        result.setOrderType(String.valueOf(brandType));

        return result;
    }

    /**
     * 查到商标以后，点击购买
     *
     * @param request
     * @return
     */
    @ApiOperation("购买商标详情")
    @PostMapping("/buy/regist/detail")
    public Brand.Detail registDetail(@RequestBody Category.BuyDetailReq request) {
        Brand.Detail detail = new Brand.Detail();
        return detail;

    }
}
