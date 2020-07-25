package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Brand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/resource")
@Api(tags = {"公共资源接口"})

public class ResourceController {

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

    @ApiOperation("上传图片")
    @PostMapping("/uploadPic")
    public String brandSearch(@RequestBody Brand.SearchReq request) {


        return "";
    }
}