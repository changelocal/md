package com.md.union.front.api.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.http.BaseResponse;
import com.google.common.base.Strings;
import com.md.union.front.api.Enums.OrderStatusEnums;
import com.md.union.front.api.vo.Common;
import com.md.union.front.client.dto.OrderDTO;
import com.md.union.front.client.dto.RefDTO;
import com.md.union.front.client.feign.OrderClient;
import com.md.union.front.client.feign.OrderRefClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/resource")
@Api(tags = {"公共资源接口"})
@Slf4j
public class ResourceController {
    @Autowired
    OrderRefClient orderRefClient;
    @Autowired
    private OrderClient orderClient;

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
        if (Strings.isNullOrEmpty(request.getId())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "订单主键id不能为空");
        }
        if (CollectionUtils.isEmpty(request.getRefs())) {
            throw new ServiceException(BaseResponse.STATUS_HANDLE_SUCCESS, "提交资料不可为空");
        }
        if (request.getModel() == 0) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "文件所属公司、个人类型不能为空");
        }
        RefDTO.OrderRefFile fileFindReq = new RefDTO.OrderRefFile();
        fileFindReq.setOrderNo(request.getId());
        log.info("orderRefClient.findByCondition param:{}", JSON.toJSONString(fileFindReq));
        BaseResponse<List<RefDTO.OrderRefFile>> fileResp = orderRefClient.findByCondition(fileFindReq);
        log.info("orderRefClient.findByCondition result:{}", JSON.toJSONString(fileResp));
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(fileResp.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "查询历史上传文件失败");
        }
        log.info("orderRefClient.delete param:{}", request.getId());
        orderRefClient.delete(request.getId());
        log.info("orderRefClient.delete result");

        Long userId = AppUserPrincipal.getPrincipal().getId();
        List<RefDTO.OrderRefFile> addList = new ArrayList<>();
        for (int i = 0; i < request.getRefs().size(); i++) {
            RefDTO.OrderRefFile item = new RefDTO.OrderRefFile();
            item.setOrderNo(request.getId());
            item.setUserNo(String.valueOf(userId));
            item.setFileId(request.getRefs().get(i).getRefId());
            item.setFileSource(request.getModel());
            item.setFileType(request.getRefs().get(i).getType());
            item.setDel(0);
            addList.add(item);
        }
        if (!addList.isEmpty()) {
            log.info("orderRefClient.addBatch param:{}", JSON.toJSONString(addList));
            BaseResponse addResp = orderRefClient.addBatch(addList);
            log.info("orderRefClient.addBatch result:{}", JSON.toJSONString(addResp));
            if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(addResp.getStatus())) {
                throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "批量添加保存上传文件失败");
            }
        }

        //修改订单状态

        OrderDTO.BrandOrderVO brandOrderVO = new OrderDTO.BrandOrderVO();
        brandOrderVO.setId(Integer.parseInt(request.getId()));
        brandOrderVO.setStatus(OrderStatusEnums.TRUST.getType());
        log.info("orderClient.update param:{}", JSON.toJSONString(brandOrderVO));
        BaseResponse updateResp = orderClient.update(brandOrderVO);
        log.info("orderClient.update result:{}", JSON.toJSONString(updateResp));
        if (!BaseResponse.STATUS_HANDLE_SUCCESS.equals(updateResp.getStatus())) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "上传文件修改订单状态失败");
        }
        res.setRet("ok");
        return res;
    }


}