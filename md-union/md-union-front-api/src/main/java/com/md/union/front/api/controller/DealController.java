package com.md.union.front.api.controller;

import com.md.union.front.api.Enums.ChangeEnums;
import com.md.union.front.api.Enums.DealEnums;
import com.md.union.front.api.Enums.RegisterEnums;
import com.md.union.front.api.vo.Brand;
import io.swagger.annotations.Api;
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
            item.setIcon(p.getIcon());
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
            item.setImg(p.getIcon());
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
            item.setImg(p.getIcon());
            item.setBrief(p.getBrief());
            item.setId(p.getType());
            item.setPriceDesc("￥" + p.getPrice() + "/件");
            changes.add(item);
        });


        return result;
    }

    @ApiOperation("商标维权-品牌权益")
    @GetMapping("/power/brand")
    public List<Brand.BrandIcon> powerBrand() {
        List<Brand.BrandIcon> result = new ArrayList<>();
        Brand.BrandIcon item1 = new Brand.BrandIcon();
        item1.setId(1);
        item1.setIcon("http://47.92.65.35:8082/file/icon/message.png");
        item1.setBrandType(DealEnums.BRAND_REFUSE_DEAL.getType());
        item1.setTitle(DealEnums.BRAND_REFUSE_DEAL.getTitle());
        result.add(item1);

        Brand.BrandIcon item2 = new Brand.BrandIcon();
        item2.setId(2);
        item2.setIcon("http://47.92.65.35:8082/file/icon/note.png");
        item2.setBrandType(DealEnums.BRAND_DISCUSS_DEAL.getType());
        item2.setTitle(DealEnums.BRAND_DISCUSS_DEAL.getTitle());
        result.add(item2);
        return result;
    }

    @ApiOperation("权威认证")
    @GetMapping("/power/auth")
    public List<Brand.PowerAuth> powerAuth() {
        List<Brand.PowerAuth> result = new ArrayList<>();
        String [] imgs = new String[]{
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=278311229,1239881690&fm=26&gp=0.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596528776843&di=5437c3ebb293f41beba72910acce6df5&imgtype=0&src=http%3A%2F%2F2b.zol-img.com.cn%2Fproduct%2F121_500x2000%2F511%2FceoccsCV9o.gif",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596528842606&di=ba4e365c2660234af731029a001737bc&imgtype=0&src=http%3A%2F%2Fimg.sm160.net%2FImg%2Fmarket%2F00%2F05%2F23%2F49%2F5234988.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1596528859451&di=5cb55bef12920bd05b2a1cdc1c18657a&imgtype=0&src=http%3A%2F%2Fimgs.soufun.com%2Fnews%2F2011_06%2F29%2Fhome%2F1309340221537_000.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2267034892,3785324037&fm=26&gp=0.jpg"
        };
        for (int i = 0; i < 5; i++) {
            Brand.PowerAuth item = new Brand.PowerAuth();
            item.setId(i+1);
            item.setImg(imgs[i]);
            item.setTitle("权威认证");
            result.add(item);
        }


        return result;
    }



}
