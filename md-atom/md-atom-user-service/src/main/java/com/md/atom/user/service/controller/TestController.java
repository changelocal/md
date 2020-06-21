package com.md.atom.user.service.controller;

import com.md.atom.user.service.vo.SampleVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "";
    }

    @PostMapping("/create")
    public SampleVO.TestResp create(@RequestBody SampleVO.Test request) {
        SampleVO.TestResp result = new SampleVO.TestResp();
        result.setId(request.getId());
        result.setName(request.getName());
        return result;
    }

}
