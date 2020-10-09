package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.arc.util.tmkoo.Tmkoo;
import com.arc.util.tmkoo.TmkooCommon;
import com.md.union.admin.api.Enums.ChangeEnums;
import com.md.union.admin.api.Enums.DealEnums;
import com.md.union.admin.api.Enums.SaleTypeEnums;
import com.md.union.admin.api.facade.MinCommon;
import com.md.union.admin.api.vo.Brand;
import com.md.union.admin.api.vo.Consultation;
import com.md.union.front.client.dto.BrandRefreshRecordDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/common")
public class CommonController {
    @Autowired
    private FrontClient frontClient ;
    @Autowired
    private MinCommon minCommon;

    @GetMapping("/loadService")
    public Brand.DealRight loadService() {
        Brand.DealRight result = new Brand.DealRight();
        List<Brand.BrandRight> rights = new ArrayList<>();
        List<Brand.BrandRight> changes = new ArrayList<>();
        for (DealEnums value : DealEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setTitle(value.getTitle());
            item.setPrice(""+ value.getPrice() );
            item.setPrepay(""+ value.getPrepay() );
            rights.add(item);
        }
        for (ChangeEnums value : ChangeEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setTitle(value.getTitle());
            item.setPrice("" + value.getPrice());
            item.setPrepay(""+ value.getPrepay() );
            changes.add(item);
        }
        result.setRights(rights);
        result.setChanges(changes);
        return result;
    }

    @ApiOperation("45大分类详情")
    @GetMapping("/brandclass/{code}")
    public Consultation.BrandClassDetailsResp brandClass(@PathVariable("code") int code) {
        Consultation.BrandClassDetailsResp res = new Consultation.BrandClassDetailsResp();

        BaseResponse<TrademarkDTO.RootBrandResp> response = frontClient.details(code);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Consultation.BrandClass> rootBrandClasses = new ArrayList<>();
        response.getResult().getCates().forEach(e -> {
            Consultation.BrandClass node = new Consultation.BrandClass();
            node.setId(e.getCode());
            node.setDesc(e.getDes());
            node.setName(e.getCode() + " " + e.getCategoryName());
            rootBrandClasses.add(node);
        });
        res.setBrandClasses(rootBrandClasses);
//        BeanUtils.copyProperties(response.getResult().getCates(), res);
        return res;
    }

    @ApiOperation("刷新所有商标状态")
    @GetMapping("/brand/refresh")
    public void refresh() {
        BrandRefreshRecordDTO.BrandRefreshRecordInfo brandRefreshRecordInfo = new BrandRefreshRecordDTO.BrandRefreshRecordInfo();
        brandRefreshRecordInfo.setCreateTime(new Date());
        brandRefreshRecordInfo.setUpdateTime(new Date());
        brandRefreshRecordInfo.setNote("开始更新");
        BaseResponse<BrandRefreshRecordDTO.Resp> add = frontClient.add(brandRefreshRecordInfo);

        TrademarkDTO.MdBrand mdBrand = new TrademarkDTO.MdBrand();
        mdBrand.setIsSale(SaleTypeEnums.not_sale.getType());
        mdBrand.setIsDelete(1);
        mdBrand.setIsEnable(2);
        mdBrand.setType(1);
        BaseResponse<TrademarkDTO.QueryResp> queryRespBaseResponse = frontClient.find(mdBrand);
        if (!queryRespBaseResponse.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(queryRespBaseResponse.getStatus(), queryRespBaseResponse.getMessage());
        }
        if(CollectionUtils.isEmpty(queryRespBaseResponse.getResult().getMdBrands())){

        }

        List<String> ids = new ArrayList<>();
        queryRespBaseResponse.getResult().getMdBrands().forEach(p->{
            Tmkoo.Flow info = TmkooCommon.info(minCommon.getHost(), p.getRegNo(), p.getCategory());
            if(!CollectionUtils.isEmpty(info.getFlowInfos())){
                for (Tmkoo.FlowInfo q : info.getFlowInfos()) {
                    if (q.getCode().equals("2") ||
                            q.getCode().equals("7") ||
                            q.getCode().equals("10") ||
                            q.getCode().equals("12") ||
                            q.getCode().equals("17") ||
                            q.getCode().equals("21") ||
                            q.getCode().equals("25") ||
                            q.getCode().equals("51") ||
                            q.getCode().equals("89") ||
                            q.getCode().equals("90") ||
                            q.getCode().equals("94") ||
                            q.getCode().equals("95") ||
                            q.getCode().equals("96") ||
                            q.getCode().equals("101") ||
                            q.getCode().equals("102") ||
                            q.getCode().equals("103") ||
                            q.getCode().equals("104") ||
                            q.getCode().equals("105") ||
                            q.getCode().equals("118") ||
                            q.getCode().equals("119") ||
                            q.getCode().equals("127") ||
                            q.getCode().equals("135") ||
                            q.getCode().equals("152") ||
                            q.getCode().equals("153") ||
                            q.getCode().equals("155") ||
                            q.getCode().equals("168") ||
                            q.getCode().equals("169") ||
                            q.getCode().equals("170") ||
                            q.getCode().equals("171") ||
                            q.getCode().equals("177") ||
                            q.getCode().equals("181") ||
                            q.getCode().equals("182")) {
                        ids.add(p.getId());
                        break;
                    }
                }
            }
        });
        if(!CollectionUtils.isEmpty(ids)){
            ids.forEach(s->{
                TrademarkDTO.MdBrand mdBrand1 = new TrademarkDTO.MdBrand();
                mdBrand1.setId(s);
                //已经删除
                mdBrand1.setIsDelete(2);
                frontClient.update(mdBrand1);
            });
        }

        BrandRefreshRecordDTO.BrandRefreshRecordInfo brandRefreshRecordInfo1 = new BrandRefreshRecordDTO.BrandRefreshRecordInfo();
        brandRefreshRecordInfo1.setId(add.getResult().getCode());
        brandRefreshRecordInfo1.setUpdateTime(new Date());
        brandRefreshRecordInfo1.setNote("更新完成");
        frontClient.update(brandRefreshRecordInfo1);

    }
}