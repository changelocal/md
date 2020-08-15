package com.md.union.order.service.controller;

import com.alibaba.fastjson.JSON;
import com.atom.core.dao.OrderRefFileDao;
import com.atom.core.model.OrderRefFile;
import com.atom.core.model.OrderRefFileParam;
import com.md.union.order.service.vo.RefVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("ref")
public class RefController {

    @Autowired
    private OrderRefFileDao orderRefFileDao;

    @ApiOperation(value = "修改上传文件", notes = "修改上传文件")
    @PostMapping("/update")
    public RefVO.Resp update(@RequestBody RefVO.OrderRefFile request) {
        RefVO.Resp resp = new RefVO.Resp();

        OrderRefFile orderRefFile = new OrderRefFile();
        BeanUtils.copyProperties(request, orderRefFile);
        orderRefFileDao.update(orderRefFile);

        return resp;
    }

    @ApiOperation(value = "新增加", notes = "新增加")
    @PostMapping("/add")
    public void add(@RequestBody RefVO.OrderRefFile request) {

        OrderRefFile orderRefFile = new OrderRefFile();
        BeanUtils.copyProperties(request, orderRefFile);
        orderRefFile.setCreateTime(new Date());
        orderRefFileDao.add(orderRefFile);

    }

    @ApiOperation(value = "查找订单附件", notes = "查找订单附件")
    @PostMapping("/find/by/condition")
    public List<RefVO.OrderRefFile> findByCondition(@RequestBody RefVO.OrderRefFile request) {
        log.info("com.md.union.order.service.controller.RefController.findByCondition param :{}", JSON.toJSONString(request));
        List<RefVO.OrderRefFile> result = new ArrayList<>();
        OrderRefFileParam param = new OrderRefFileParam();
        param.setOrderNo(request.getOrderNo());
        param.setUserNo(request.getUserNo());
        param.setDel(request.getDel());
        List<OrderRefFile> list = orderRefFileDao.find(param);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        list.forEach(p -> {
            result.add(convert(p));
        });
        log.info("com.md.union.order.service.controller.RefController.findByCondition result:{}", JSON.toJSONString(result));
        return result;

    }

    private RefVO.OrderRefFile convert(OrderRefFile request) {
        RefVO.OrderRefFile result = new RefVO.OrderRefFile();
        result.setId(request.getId());
        result.setOrderNo(request.getOrderNo());
        result.setUserNo(request.getUserNo());
        result.setFileId(request.getFileId());
        result.setFileSource(request.getFileSource());
        result.setFileType(request.getFileType());
        result.setDel(request.getDel());
        result.setCreateTime(request.getCreateTime());
        return result;
    }


}
