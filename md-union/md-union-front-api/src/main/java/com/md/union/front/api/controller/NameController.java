package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo.Name;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/front/name")
@Api(tags = {"商标起名服务"})
public class NameController {
    @Autowired
    private FrontClient frontClient;

    @ApiOperation("热门商标字")
    @GetMapping("/hot")
    public List<String> hotName() {
        String[] chars = {"美","雄","雷","魔","星","阳","宝","飞","顶","齐","龙","电","丹"
                ,"田","东","豪","世","好","丸","丰","海","罗","中","丰","申","本","主","丽"
                ,"上","下","不","专","叶","史","杰","吉","可","古","有","原","友","参","厦"};
        List<String> names = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<16;i++){
            int j = random.nextInt(chars.length);
            names.add(chars[j]);
        }
        return names;
    }
    /**
     * 起名的查商标
     * @param request
     * @return
     */
    @ApiOperation("商标起名查询")
    @PostMapping("/search")
    public Name.SearchRes brandSearch(@RequestBody Name.SearchReq request) {
        Name.SearchRes result = new Name.SearchRes();
        TrademarkDTO.MdBrand req = new TrademarkDTO.MdBrand();
        //名字
        req.setBrandName(request.getInput());
        //分类
        req.setCategory(request.getCategory());
        //字符数
        req.setBrandNameLength(request.getWordCnt());
        if(request.getWordCnt()==1){
            req.setBrandNameLengthLow(1);
            req.setBrandNameLengthHigh(2);
        }
        else if (request.getWordCnt()==2){
            req.setBrandNameLength(3);
        }
        else if (request.getWordCnt()==3){
            req.setBrandNameLength(4);
        }
        else if (request.getWordCnt()==4){
            req.setBrandNameLengthLow(5);
            req.setBrandNameLengthHigh(99);
        }
        //组合  0 不限
        req.setComType(0);
        //价格 不限
        req.setPriceLow(new BigDecimal(1));
        req.setPriceHigh(new BigDecimal(2000000));

        req.setPageIndex(request.getPageIndex());
        req.setPageSize(request.getPageSize());

        BaseResponse<TrademarkDTO.QueryResp> response = frontClient.search(req);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Name.Info> infos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(response.getResult().getMdBrands())){
            response.getResult().getMdBrands().forEach(e->{
                Name.Info item = new Name.Info();
                item.setBrand(e.getBrandName());
                item.setBuyOrRegist(1);
                item.setSuccessRate("90%");
                item.setId(e.getId());
                infos.add(item);
            });
        }
        result.setCount(infos.size());
        result.setList(infos);
        return result;
    }

}