package com.md.union.front.api.controller;

import com.md.union.front.api.Enums.ChangeEnums;
import com.md.union.front.api.Enums.DealEnums;
import com.md.union.front.api.Enums.RegisterEnums;
import com.md.union.front.api.vo.Brand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/front/deal")
@Api(tags = {"客户服务咨询管理"})
public class DealController {


    @ApiOperation("商标注册介绍")
    @GetMapping("/register")
    public List<Brand.BrandRegister> register() {
        List<Brand.BrandRegister> result = new ArrayList<>();
        Arrays.stream(RegisterEnums.values()).forEach(p -> {
            Brand.BrandRegister item = new Brand.BrandRegister();
            item.setId(p.getType());
            item.setBrandType(1);
            item.setTitle(p.getTitle());
            item.setBrief(p.getBrief());
            item.setPriceDesc("￥" + p.getPrice() + "/件fd");
            result.add(item);
        });
        return result;
    }

    @ApiOperation("商标维权介绍")
    @GetMapping("/power")
    public Brand.DealRight power() {
        String img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595638504208&di=754606fdcb88e6b4ee9ea1476f4c2a5f&imgtype=0&src=http%3A%2F%2Fimage-ali.bianjiyi.com%2F1%2F2018%2F0710%2F14%2F15312043093853.jpg";
        Brand.DealRight result = new Brand.DealRight();
        Brand.Person person = new Brand.Person();
        List<Brand.BrandRight> rights = new ArrayList<>();
        List<Brand.BrandRight> changes = new ArrayList<>();
        result.setPerson(person);
        result.setRights(rights);
        result.setChanges(changes);

        person.setHeadImg("http://pic.5tu.cn/uploads/allimg/1607/pic_5tu_big_201607221326573826.jpg");
        person.setName("客服1");
        person.setPhone("15688706317");
        person.setQq("571660498");

        Arrays.stream(DealEnums.values()).forEach(p -> {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(p.getType());
            item.setBrandType(2);
            item.setTitle(p.getTitle());
            item.setImg(img);
            item.setBrief(p.getBrief());
            item.setId(p.getType());
            item.setPriceDesc("￥" + p.getPrice() + "/件");
            rights.add(item);
        });
        Arrays.stream(ChangeEnums.values()).forEach(p -> {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(p.getType());
            item.setBrandType(3);
            item.setTitle(p.getTitle());
            item.setImg(img);
            item.setBrief(p.getBrief());
            item.setId(p.getType());
            item.setPriceDesc("￥" + p.getPrice() + "/件");
            changes.add(item);
        });



        return result;
    }
}
