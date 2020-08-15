package com.md.union.front.api.controller;


import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo.Consultation;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/front/common")
@Api(tags = {"小程序通用功能"})
public class CommonController {

    @Autowired
    private FrontClient frontClient;
//    @Autowired
//    private OssClientTool ossClientTool;

//    private String realPath = "md";

//    @ApiOperation(tags = "上传文件相关", value = "上传文件到oss", notes = "上传文件到oss")
//    @ResponseBody
//    @PostMapping("upfile2oss")
//    public OssFileInfo upFile(@RequestParam(value = "fileObj") MultipartFile fileObj) {
//        String[] ex = fileObj.getContentType().split("/");
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMM");
//        String fileName = dateFormat2.format(new Date()) + DigestUtils.md5Hex(fileObj.getOriginalFilename()
//                + System.currentTimeMillis() + new Random().nextLong()) + "." + ex[1];
//        ossClientTool.uploadImg2Oss(fileObj, realPath.concat(fileName));
////        logger.info("==================================http://compensate-info.oss-cn-zhangjiakou.aliyuncs.com/" + realPath + fileName);
//        OssFileInfo res = new OssFileInfo();
//        res.setName(fileName);
//        return res;
//    }


//    @ApiOperation(tags = "上传文件相关", value = "下载文件从oss，返回文件对象", notes = "下载文件从oss，返回文件对象")
//    @ResponseBody
//    @GetMapping("getfile")
//    public void getFile(@RequestParam @Valid @NotBlank String filename, String insuranceId, HttpServletResponse response) throws IOException {
//        if (Strings.isNullOrEmpty(filename)) {
////            logger.info(" insuranceId:{}", insuranceId);
//            if (Strings.isNullOrEmpty(insuranceId)) {
//                response.setContentType("application/json");
//                JSONObject result = new JSONObject();
//                result.put("code", 200);
//                result.put("message", "no file");
//                result.put("data", 400);
//                response.getWriter().write(result.toJSONString());
//                return;
//            } else {
////                String ossFileName = redisClient.get(insuranceId + "_sign");
//                String ossFileName = "_sign";
//                if (Strings.isNullOrEmpty(ossFileName)) {
//                    response.setContentType("application/json");
//                    JSONObject result = new JSONObject();
//                    result.put("code", 200);
//                    result.put("message", "no file");
//                    result.put("data", 400);
//                    response.getWriter().write(result.toJSONString());
//                    return;
//                } else {
//                    filename = ossFileName;
//                }
//            }
//        }
////        logger.info("getFile filename:{}", filename);
//        byte[] imgFromOss = ossClientTool.getImgFromOss((filename));
//        String[] ex = filename.split("\\.");
////        logger.info("getFile ex:{}", ex[0]);
////        response.setContentType("image/"+ex[1].concat(";charset=utf-8"));
//        response.setContentType("image/png".concat(";charset=utf-8"));
//        response.setHeader("Content-disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
//        response.getOutputStream().write(imgFromOss);
//        response.getOutputStream().flush();
////        logger.info("getFile response:{}", response);
//    }

    @ApiOperation("咨询按钮接口")
    @GetMapping("/consultation/{id}")
    public Consultation.ConsultationResp dealDtail(@PathVariable("id") String id) {

        Consultation.ConsultationResp result = new Consultation.ConsultationResp();

        TrademarkDTO.Consultation consultation = new TrademarkDTO.Consultation();
        consultation.setId(id);
        BaseResponse<TrademarkDTO.ConsultationResp> response = frontClient.consultation(consultation);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        //返回任意销售信息
        result.setName(response.getResult().getNickname());
        result.setQq(response.getResult().getQqAccount());
        result.setTel(response.getResult().getMobile());
        result.setTitle(response.getResult().getTitle());
        result.setAvatar(response.getResult().getAvatar());
        //应该记录一下咨询记录 todo
//        ServiceDTO.Consultation consultation1 = new ServiceDTO.Consultation();
//        consultation1.setOpUserId(response.getResult().getId());
//        consultation1.setOrderNo(id);
//        consultation1.setStatus(1);
//        consultation1.setOpenId(AppUserPrincipal.getPrincipal().getOpenId());
//
//        BaseResponse<ServiceDTO.Resp> responseAdd = frontClient.addConsultation(consultation1);
//        if (!responseAdd.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
//            throw new ServiceException(responseAdd.getStatus(), responseAdd.getMessage());
//        }

        return result;
    }

    @ApiOperation("首页热门搜索，搜索次数")
    @GetMapping("/home/hot")
    public Consultation.HomeHotResp homeHot() {
        Consultation.HomeHotResp res = new Consultation.HomeHotResp();
        res.setSearchCount("26354");
        List<String> strings = new ArrayList<>();
        strings.add("口罩");
        strings.add("新基建");
        strings.add("医学鉴证");
        strings.add("环保材料");
        strings.add("5G");
        res.setHotSearch(strings);
        return res;
    }

    @ApiOperation("所有45大分类")
    @GetMapping("/allbrandclass")
    public Consultation.BrandClassResp allBrandClass() {
        Consultation.BrandClassResp res = new Consultation.BrandClassResp();

        BaseResponse<TrademarkDTO.RootBrandResp> response = frontClient.root();
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Consultation.RootBrandClass> rootBrandClasses = new ArrayList<>();
        response.getResult().getCates().forEach(e -> {
            Consultation.RootBrandClass node = new Consultation.RootBrandClass();
            node.setCode(e.getCode());
            node.setName(e.getCode()+"类 "+e.getCategoryName());
            rootBrandClasses.add(node);
        });
        res.setRootBrandClasses(rootBrandClasses);
        return res;
    }

    @ApiOperation("45大分类详情")
    @GetMapping("/brandclass/{code}")
    public Consultation.BrandClassDetailsResp brandClass(@PathVariable("code") int code) {
        Consultation.BrandClassDetailsResp res = new Consultation.BrandClassDetailsResp();

        BaseResponse<TrademarkDTO.RootBrandResp> response = frontClient.details(code);
        if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(response.getStatus(), response.getMessage());
        }
        List<Consultation.BrandClass> rootBrandClasses = new ArrayList<>();
        response.getResult().getCates().forEach(e -> {
            Consultation.BrandClass node = new Consultation.BrandClass();
            node.setDesc(e.getDes());
            node.setName(e.getCode()+" "+e.getCategoryName());
            rootBrandClasses.add(node);
        });
        res.setBrandClasses(rootBrandClasses);
//        BeanUtils.copyProperties(response.getResult().getCates(), res);
        return res;
    }
}
