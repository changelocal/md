package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Name;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/name")
@Api(tags = {"商标起名服务"})
public class NameController {
    @Autowired
    private FrontClient frontClient;

    /**
     * 有成功率的查商标
     * @param request
     * @return
     */
    @ApiOperation("商标起名查询")
    @PostMapping("/search")
    public Name.SearchRes brandSearch(@RequestBody Name.SearchReq request) {
        Name.SearchRes result = new Name.SearchRes();
        List<Name.Info> infos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Name.Info item = new Name.Info();
            item.setBrand("汽车" + i);
            item.setBuyOrRegist(78 + i);
            item.setSuccessRate("90%");
            item.setId(i);
            infos.add(item);
        }
        result.setCount(5);
        return result;

//        NameDTO.SearchReq req = new NameDTO.SearchReq();
//        req.setCategory(request.getCategory());
//        req.setInput(request.getInput());
//        req.setStatus(request.getStatus());
//        req.setWordCnt(request.getWordCnt());
//
//        BaseResponse<NameDTO.SearchRes> response = frontClient.searchName(req);
//        if(!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)){
//            throw new ServiceException(response.getStatus(),response.getMessage());
//        }
//        List<Name.Info> infos = new ArrayList<>();
//        response.getResult().getList().forEach(e->{
//            Name.Info info = new Name.Info();
//            info.setBrand(e.getBrand());
//            info.setBuyOrRegist(e.getBuyOrRegist());
//            info.setId(e.getId());
//            info.setSuccessRate(e.getSuccessRate());
//
//            infos.add(info);
//        });
//
//        result.setList(infos);
//        result.setCount(response.getResult().getCount());
//
//        return result;
    }

}