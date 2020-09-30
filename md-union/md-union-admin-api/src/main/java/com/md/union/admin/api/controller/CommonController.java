package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.Enums.ChangeEnums;
import com.md.union.admin.api.Enums.DealEnums;
import com.md.union.admin.api.vo.Brand;
import com.md.union.admin.api.vo.Consultation;
import com.md.union.front.client.dto.BrandRefreshRecordDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
        frontClient.add(brandRefreshRecordInfo);



    }
}