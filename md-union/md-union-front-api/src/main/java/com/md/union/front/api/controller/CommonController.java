package com.md.union.front.api.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.md.union.front.api.facade.OSSClientFacade;
import com.md.union.front.api.vo.Consultation;
import com.md.union.front.api.vo.OssFileInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/front/common")
@Api(tags = {"小程序通用功能"})
public class CommonController {
    @Value("${oss.filedir}")
    private String realPath;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.endpoint}")
    private String endpoint;


    @Autowired
    private OSSClientFacade ossClientFacade;

    @ApiOperation(tags = "上传文件相关", value = "上传文件到oss", notes = "上传文件到oss")
    @ResponseBody
    @PostMapping("upfile2oss")
    public OssFileInfo upFile(@RequestParam(value = "fileObj") MultipartFile fileObj) {
        String[] ex = fileObj.getContentType().split("/");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMM");
        String fileName = dateFormat2.format(new Date()) + DigestUtils.md5Hex(fileObj.getOriginalFilename()
                + System.currentTimeMillis() + new Random().nextLong()) + "." + ex[1];
        ossClientFacade.uploadImg2Oss(fileObj, realPath.concat(fileName));
//        logger.info("==================================http://compensate-info.oss-cn-zhangjiakou.aliyuncs.com/" + realPath + fileName);
        OssFileInfo res = new OssFileInfo();
        res.setName(fileName);
        return res;
    }


    @ApiOperation(tags = "上传文件相关", value = "下载文件从oss，返回文件对象", notes = "下载文件从oss，返回文件对象")
    @ResponseBody
    @GetMapping("getfile")
    public void getFile(@RequestParam @Valid @NotBlank String filename, String insuranceId, HttpServletResponse response) throws IOException {
        if (Strings.isNullOrEmpty(filename)) {
//            logger.info(" insuranceId:{}", insuranceId);
            if (Strings.isNullOrEmpty(insuranceId)) {
                response.setContentType("application/json");
                JSONObject result = new JSONObject();
                result.put("code", 200);
                result.put("message", "no file");
                result.put("data", 400);
                response.getWriter().write(result.toJSONString());
                return;
            } else {
//                String ossFileName = redisClient.get(insuranceId + "_sign");
                String ossFileName =  "_sign";
                if (Strings.isNullOrEmpty(ossFileName)) {
                    response.setContentType("application/json");
                    JSONObject result = new JSONObject();
                    result.put("code", 200);
                    result.put("message", "no file");
                    result.put("data", 400);
                    response.getWriter().write(result.toJSONString());
                    return;
                } else {
                    filename = ossFileName;
                }
            }
        }
//        logger.info("getFile filename:{}", filename);
        byte[] imgFromOss = ossClientFacade.getImgFromOss((filename));
        String[] ex = filename.split("\\.");
//        logger.info("getFile ex:{}", ex[0]);
//        response.setContentType("image/"+ex[1].concat(";charset=utf-8"));
        response.setContentType("image/png".concat(";charset=utf-8"));
        response.setHeader("Content-disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
        response.getOutputStream().write(imgFromOss);
        response.getOutputStream().flush();
//        logger.info("getFile response:{}", response);
    }

    @ApiOperation("咨询按钮接口")
    @GetMapping("/consultation/{id}")
    public Consultation.ConsultationResp dealDtail(@PathVariable("id") String id) {
        Consultation.ConsultationResp result = new Consultation.ConsultationResp();

        return result;
    }

}
