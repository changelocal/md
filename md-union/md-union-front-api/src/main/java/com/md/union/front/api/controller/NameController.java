package com.md.union.front.api.controller;

import com.md.union.front.api.vo.Name;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/front/name")
@Api(tags = {"商标起名服务"})
public class NameController {
    @Autowired
    private FrontClient frontClient;


    @ApiOperation("热门商标字")
    @GetMapping("/hot")
    public List<String> hotName() {
        String[] chars = {"美","雄","雷","魔","星","阳","宝","飞","顶","齐","龙","电","丹"
                ,"田","东","豪","世","好","丸","丰","海","罗","中","丰","丹","本","主","丽"
                ,"上","下","不","专","叶","史","杰","吉","可","古","有","原","友","参","厦"};
        List<String> names = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<16;i++){
            int j = random.nextInt(chars.length);
            names.add(chars[j]);
        }
        return names;
    }
    /**
     * 起名的查商标
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
    }

}