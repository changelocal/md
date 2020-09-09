package com.md.union.admin.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.admin.api.vo.Brand;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.dto.WxUserDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/brand")
@Api(tags = {"商标管理"})
public class BrandController {
    @Autowired
    private FrontClient frontClient;
    @ApiOperation("商标起名查询")
    @PostMapping("/query")
    public Brand.SearchRes query(@RequestBody Brand.SearchReq request) {
        Brand.SearchRes ret = new Brand.SearchRes();
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
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
        WxUserDTO.WxUser adminUser = new WxUserDTO.WxUser();
        BeanUtils.copyProperties(request, adminUser);
        BaseResponse<WxUserDTO.Resp> query = frontClient.update(adminUser);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }

    }

}