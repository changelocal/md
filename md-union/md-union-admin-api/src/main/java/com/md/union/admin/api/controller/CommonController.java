package com.md.union.admin.api.controller;

import com.md.union.admin.api.Enums.ChangeEnums;
import com.md.union.admin.api.Enums.DealEnums;
import com.md.union.admin.api.vo.Brand;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/web/common")
public class CommonController {
    @GetMapping("/loadService")
    public Brand.DealRight loadService() {
        Brand.DealRight result = new Brand.DealRight();
        List<Brand.BrandRight> rights = new ArrayList<>();
        List<Brand.BrandRight> changes = new ArrayList<>();
        for (DealEnums value : DealEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setTitle(value.getTitle());
            item.setPrice(""+ value.getPrice() );
            item.setPrepay(""+ value.getPrepay() );
            rights.add(item);
        }
        for (ChangeEnums value : ChangeEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setTitle(value.getTitle());
            item.setPrice("" + value.getPrice());
            item.setPrepay(""+ value.getPrepay() );
            changes.add(item);
        }
        result.setRights(rights);
        result.setChanges(changes);
        return result;
    }
}