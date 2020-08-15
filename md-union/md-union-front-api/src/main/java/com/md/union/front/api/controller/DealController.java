package com.md.union.front.api.controller;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
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
import java.util.Arrays;
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
        Brand.DealRight result = new Brand.DealRight();
//        Brand.Person person = new Brand.Person();

        List<Brand.BrandRight> rights = new ArrayList<>();
        List<Brand.BrandRight> changes = new ArrayList<>();

        ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
        service.setServiceTypeId("1");
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if(!CollectionUtils.isEmpty(service1.getResult().getServices())){
            service1.getResult().getServices().forEach(p->{
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
        if(!CollectionUtils.isEmpty(serviceRet.getResult().getServices())){
            serviceRet.getResult().getServices().forEach(p->{
                Brand.BrandRight item = new Brand.BrandRight();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setImg(p.getImageUrl());
                item.setBrief(p.getSubTitle());
                item.setPriceDesc("￥" + p.getPrice() + "/件");
                changes.add(item);
            });
        }

//        result.setPerson(person);
        result.setRights(rights);
        result.setChanges(changes);
        return result;
    }

    @ApiOperation("商标维权-品牌权益")
    @GetMapping("/power/brand")
    public List<Brand.BrandIcon> powerBrand() {
        List<Brand.BrandIcon> result = new ArrayList<>();

        ServiceDTO.Service service = new ServiceDTO.Service();
        service.setIsChecked(1);
        service.setIsEnable(2);
//        service.setServiceTypeId("1");
        service.setServiceName("商标驳回复审");
        BaseResponse<ServiceDTO.FindResp> service1 = frontClient.findService(service);
        if (!service1.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(service1.getStatus(), service1.getMessage());
        }
        if(!CollectionUtils.isEmpty(service1.getResult().getServices())){
            service1.getResult().getServices().forEach(p->{
                Brand.BrandIcon item = new Brand.BrandIcon();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setIcon("http://47.92.65.35:8082/file/icon/message.png");
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
        if(!CollectionUtils.isEmpty(service1.getResult().getServices())){
            service1.getResult().getServices().forEach(p->{
                Brand.BrandIcon item = new Brand.BrandIcon();
                item.setId(p.getId());
                item.setBrandType(2);
                item.setTitle(p.getServiceName());
                item.setIcon("http://47.92.65.35:8082/file/icon/note.png");
                result.add(item);

            });
        }
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
