package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo.Common;
import com.md.union.front.client.dto.RefDTO;
import com.md.union.front.client.feign.OrderRefClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/resource")
@Api(tags = {"公共资源接口"})

public class ResourceController {
    @Autowired
    OrderRefClient orderRefClient;

    @ApiOperation("权威认证的图片")
    @GetMapping("/auth")
    public List<String> hotCategory() {
        List<String> result = new ArrayList<>();
        result.add("https://www.jseea.cn/contents/channel_178/2018/11/1811211458927(3).png");
        result.add("https://www.jseea.cn/contents/channel_178/2018/11/1811211458927(3).png");
        result.add("https://www.jseea.cn/contents/channel_178/2018/11/1811211458927(3).png");
        result.add("https://www.jseea.cn/contents/channel_178/2018/11/1811211458927(3).png");
        result.add("https://www.jseea.cn/contents/channel_178/2018/11/1811211458927(3).png");
        return result;
    }

    @ApiOperation("提交资料")
    @PostMapping("/commit")
    public Common.CommitRes commit(@RequestBody Common.CommitReq request) {
        Common.CommitRes res = new Common.CommitRes();

        if (CollectionUtils.isEmpty(request.getRefs())) {
            throw new ServiceException(BaseResponse.STATUS_HANDLE_SUCCESS, "提交资料不可为空");
        }
        //覆盖之前文件信息
        RefDTO.OrderRefFile orderRefFile = new RefDTO.OrderRefFile();
        orderRefFile.setOrderNo(request.getId());
        orderRefFile.setDel(1);
        orderRefClient.update(orderRefFile);

        //保存文件信息
        for (Common.Ref p : request.getRefs()) {
            orderRefFile = new RefDTO.OrderRefFile();
            orderRefFile.setOrderNo(request.getId());
            orderRefFile.setFileSource(request.getModel());
            orderRefFile.setFileId(p.getRefId());
            orderRefFile.setFileType(p.getType());
            orderRefClient.add(orderRefFile);
        }
        //修改订单状态


        return res;
    }


}