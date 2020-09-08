package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.Enums.ChangeEnums;
import com.md.union.front.api.Enums.DealEnums;
import com.md.union.front.api.Enums.RegisterEnums;
import com.md.union.front.api.vo.Brand;
import com.md.union.front.client.dto.ServiceDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/deal")
@Api(tags = {"客户服务咨询管理"})
public class DealController {

    @Autowired
    private FrontClient frontClient;

    @ApiOperation("商标注册介绍")
    @GetMapping("/register")
    public List<Brand.BrandRegister> register() {
        List<Brand.BrandRegister> result = new ArrayList<>();
        for (RegisterEnums value : RegisterEnums.values()) {
            Brand.BrandRegister item = new Brand.BrandRegister();
            item.setId(value.getNo());
            item.setPriceDesc("￥" + value.getPrice() + "件");
            item.setTitle(value.getTitle());
            item.setIcon(value.getIcon());
            item.setBrief(value.getBrief());
            result.add(item);
        }


        /*ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
        service.setIsVideoDefault(2);
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if(!CollectionUtils.isEmpty(service1.getResult().getServices())){
            service1.getResult().getServices().forEach(p->{
                Brand.BrandRegister item = new Brand.BrandRegister();
                item.setId(p.getId());
                item.setPriceDesc(p.getPrice().toString() );
                item.setTitle(p.getServiceName());
                item.setIcon("https://pay.mdlogo.cn/file/brand-register/"+p.getId()+".png");
                item.setBrief(p.getSubTitle());
                result.add(item);
            });
        }*/

        return result;
    }

    @ApiOperation("商标设计list")
    @GetMapping("/design")
    public Brand.Design design() {
        Brand.Design ret = new Brand.Design();
        List<Brand.BrandDesign> brandDesigns = new ArrayList<>();
        ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
        service.setServiceTypeId("8");
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if (!CollectionUtils.isEmpty(service1.getResult().getServices())) {
            service1.getResult().getServices().forEach(p -> {
                Brand.BrandDesign item = new Brand.BrandDesign();
                item.setId(p.getId());
                item.setTitle(p.getServiceName());
                item.setIcon("https://pay.mdlogo.cn/file/brand-design/" + p.getId() + ".png");
                item.setBrief(p.getSubTitle());
                brandDesigns.add(item);
            });
        }
        ret.setDesigns(brandDesigns);
        return ret;
    }

    @ApiOperation("商标维权介绍")
    @GetMapping("/power")
    public Brand.DealRight power() {
        Brand.DealRight result = new Brand.DealRight();


//        Brand.Person person = new Brand.Person();

        List<Brand.BrandRight> rights = new ArrayList<>();
        List<Brand.BrandRight> changes = new ArrayList<>();

        for (DealEnums value : DealEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setBrandType(value.getType());
            item.setTitle(value.getTitle());
            item.setImg(value.getIcon());
            item.setBrief(value.getBrief());
            item.setPriceDesc("￥" + value.getPrice() + "/件");
            rights.add(item);
        }
        for (ChangeEnums value : ChangeEnums.values()) {
            Brand.BrandRight item = new Brand.BrandRight();
            item.setId(value.getNo());
            item.setBrandType(value.getType());
            item.setTitle(value.getTitle());
            item.setImg(value.getIcon());
            item.setBrief(value.getBrief());
            item.setPriceDesc("￥" + value.getPrice() + "/件");
            changes.add(item);
        }


        /*ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
        service.setServiceTypeId("2");
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if (!CollectionUtils.isEmpty(service1.getResult().getServices())) {
            service1.getResult().getServices().forEach(p -> {
                Brand.BrandRight item = new Brand.BrandRight();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setImg(p.getImageUrl());
                item.setBrief(p.getSubTitle());
                item.setPriceDesc("￥" + p.getPrice() + "/件");
                rights.add(item);

            });
        }

        ServiceDTO.Service serviceChg = new ServiceDTO.Service();
        serviceChg.setIsChecked(1);
        serviceChg.setIsEnable(2);
        serviceChg.setServiceTypeId("4");
        BaseResponse<ServiceDTO.FindResp> serviceRet = frontClient.findService(serviceChg);
        if (!serviceRet.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(serviceRet.getStatus(), serviceRet.getMessage());
        }
        if (!CollectionUtils.isEmpty(serviceRet.getResult().getServices())) {
            serviceRet.getResult().getServices().forEach(p -> {
                Brand.BrandRight item = new Brand.BrandRight();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setImg(p.getImageUrl());
                item.setBrief(p.getSubTitle());
                item.setPriceDesc("￥" + p.getPrice() + "/件");
                changes.add(item);
            });
        }*/

//        result.setPerson(person);


        result.setRights(rights);
        result.setChanges(changes);
        return result;
    }

    @ApiOperation("商标维权-品牌权益")
    @GetMapping("/power/brand")
    public List<Brand.BrandIcon> powerBrand() {
        List<Brand.BrandIcon> result = new ArrayList<>();

        /*ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
//        service.setServiceTypeId("1");
        service.setServiceName("商标驳回复审");
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if (!CollectionUtils.isEmpty(service1.getResult().getServices())) {
            service1.getResult().getServices().forEach(p -> {
                Brand.BrandIcon item = new Brand.BrandIcon();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setIcon("https://pay.mdlogo.cn/file/icon/message.png");
                result.add(item);

            });
        }
        service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
//        service.setServiceTypeId("1");
        service.setServiceName("商标异议答辩");
        service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if (!CollectionUtils.isEmpty(service1.getResult().getServices())) {
            service1.getResult().getServices().forEach(p -> {
                Brand.BrandIcon item = new Brand.BrandIcon();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setIcon("https://pay.mdlogo.cn/file/icon/note.png");
                result.add(item);

            });
        }*/

        DealEnums[] dealEnums = DealEnums.values();
        for (int i = 0; i < 2; i++) {
            Brand.BrandIcon item = new Brand.BrandIcon();
            item.setId(dealEnums[i].getNo());
            item.setBrandType(dealEnums[i].getType());
            item.setTitle(dealEnums[i].getTitle());
            item.setIcon(dealEnums[i].getIcon());
            result.add(item);
        }


        return result;
    }

    @ApiOperation("权威认证")
    @GetMapping("/power/auth")
    public List<Brand.PowerAuth> powerAuth() {
        List<Brand.PowerAuth> result = new ArrayList<>();

        for (int i = 1; i < 23; i++) {
            Brand.PowerAuth item = new Brand.PowerAuth();
            item.setId(i);
            item.setImg("https://pay.mdlogo.cn/file/enterprise/" + i + ".jpg");
            item.setTitle("权威认证");
            result.add(item);
        }


        return result;
    }


}
