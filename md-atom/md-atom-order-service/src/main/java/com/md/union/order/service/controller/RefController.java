package com.md.union.order.service.controller;

import com.atom.core.dao.OrderRefFileDao;
import com.atom.core.model.OrderRefFile;
import com.md.union.order.service.vo.RefVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("ref")
public class RefController {

    @Autowired
    private OrderRefFileDao orderRefFileDao;

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
    public RefVO.Resp add(@RequestBody RefVO.OrderRefFile request) {
        RefVO.Resp resp = new RefVO.Resp();

        OrderRefFile orderRefFile = new OrderRefFile();
        BeanUtils.copyProperties(request, orderRefFile);
        orderRefFile.setCreateTime(new Date());
        orderRefFileDao.add(orderRefFile);

        return resp;
    }

}
