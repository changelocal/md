package com.md.union.order.service.controller;

import com.alibaba.fastjson.JSON;
import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.atom.core.dao.OrderRefFileDao;
import com.atom.core.model.OrderRefFile;
import com.atom.core.model.OrderRefFileParam;
import com.google.common.base.Strings;
import com.md.union.order.service.vo.RefVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
    public void update(@RequestBody RefVO.OrderRefFile request) {
        log.info("orderRefFileDao.update param:{}", JSON.toJSONString(request));
        orderRefFileDao.update(convert(request));
        log.info("orderRefFileDao.update result:{}");
    }

    @ApiOperation(value = "新增加", notes = "新增加")
    @PostMapping("/add")
    public void add(@RequestBody RefVO.OrderRefFile request) {
        log.info("orderRefFileDao.add param:{}", JSON.toJSONString(request));
        orderRefFileDao.add(convert(request));
        log.info("orderRefFileDao.add result:{}");
    }

    @ApiOperation(value = "批量新增加", notes = "批量新增加")
    @PostMapping("/add/batch")
    public void addBatch(@RequestBody List<RefVO.OrderRefFile> request) {
        if (CollectionUtils.isEmpty(request)) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "文件不存在");
        }
        List<OrderRefFile> file = new ArrayList<>();
        request.forEach(p -> {
            file.add(convert(p));
        });
        log.info("orderRefFileDao.addBatch param:{}", JSON.toJSONString(file));
        orderRefFileDao.addBatch(file);
        log.info("orderRefFileDao.addBatch result");
    }

    @ApiOperation(value = "批量修改", notes = "批量修改")
    @PostMapping("/update/batch")
    public void updateBatch(@RequestBody List<RefVO.OrderRefFile> request) {
        if (CollectionUtils.isEmpty(request)) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "文件不存在");
        }
        List<OrderRefFile> file = new ArrayList<>();
        request.forEach(p -> {
            file.add(convert(p));
        });
        log.info("orderRefFileDao.updateBatch param:{}", JSON.toJSONString(file));
        orderRefFileDao.updateBatch(file);
        log.info("orderRefFileDao.updateBatch result");
    }

    @ApiOperation(value = "删除上传文件", notes = "删除上传文件")
    @PostMapping("/delete/{orderNo}")
    public void delete(@PathVariable("orderNo") String orderNo) {
        if (Strings.isNullOrEmpty(orderNo)) {
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "删除文件的订单编号不存在");
        }
        log.info("orderRefFileDao.delete param:{}", orderNo);
        orderRefFileDao.delete(orderNo);
        log.info("orderRefFileDao.delete result");
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
        result.setCreateTime(new Date());
        return result;
    }

    private OrderRefFile convert(RefVO.OrderRefFile request) {
        OrderRefFile result = new OrderRefFile();
        result.setId(request.getId());
        result.setOrderNo(request.getOrderNo());
        result.setUserNo(request.getUserNo());
        result.setFileId(request.getFileId());
        result.setFileSource(request.getFileSource());
        result.setFileType(request.getFileType());
        result.setDel(request.getDel());
        result.setCreateTime(new Date());
        return result;
    }

}
