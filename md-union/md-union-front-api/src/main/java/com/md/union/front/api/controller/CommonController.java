package com.md.union.front.api.controller;


import com.alibaba.fastjson.JSONObject;
import com.arc.common.ServiceException;
import com.arc.util.auth.AppUserPrincipal;
import com.arc.util.file.oss.OssClientTool;
import com.arc.util.http.BaseResponse;
import com.md.union.front.api.vo.Common;
import com.md.union.front.api.vo.Consultation;
import com.md.union.front.api.vo.OssFileInfo;
import com.md.union.front.client.dto.AdminUserDTO;
import com.md.union.front.client.dto.ConsultationDTO;
import com.md.union.front.client.dto.ServiceDTO;
import com.md.union.front.client.dto.TrademarkDTO;
import com.md.union.front.client.feign.FrontClient;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

@RestController
@RequestMapping("/front/common")
@Api(tags = {"小程序通用功能"})
public class CommonController {

    @Autowired
    private FrontClient frontClient;
    @Autowired
    private OssClientTool ossClientTool;

    private String realPath = "md/";

    @ApiOperation(tags = "上传文件相关", value = "上传文件到oss", notes = "上传文件到oss")
    @ResponseBody
    @PostMapping("upfile2oss")
    public OssFileInfo upFile(@RequestParam(value = "fileObj") MultipartFile fileObj) {
        /*String[] ex = fileObj.getContentType().split("/");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMM");
        String fileId = dateFormat2.format(new Date()) + DigestUtils.md5Hex(fileObj.getOriginalFilename()
                + System.currentTimeMillis() + new Random().nextLong()) + "." + ex[1];*/
        String fileId = new Date().getTime() + "T" + new Random().nextLong();
        ossClientTool.uploadImg2Oss(fileObj, realPath.concat(fileId));
//        logger.info("==================================https://tianxiuquan.oss-cn-zhangjiakou.aliyuncs.com/" + realPath + fileName);
        String url = "https://tianxiuquan.oss-cn-zhangjiakou.aliyuncs.com/" + realPath + fileId;
        OssFileInfo res = new OssFileInfo();
        res.setName(url);
        return res;
    }


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
        //判断最近什么时候咨询过
        boolean hadConsultation = false;
        ConsultationDTO.Info request = new ConsultationDTO.Info();
        request.setOpenId(AppUserPrincipal.getPrincipal().getMinId());
        BaseResponse<ConsultationDTO.QueryResp> query = frontClient.query(request);
        if (!query.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(query.getStatus(), query.getMessage());
        }
        ConsultationDTO.Info info = null;
        if (query.getResult() != null) {
            if (!CollectionUtils.isEmpty(query.getResult().getInfos())) {
                info = query.getResult().getInfos().get(0);
                //一天内咨询过
                if (info.getCreateTime().getTime() > new Date().getTime() - 24 * 60 * 60 * 1000 * 1) {
                    hadConsultation = true;
                }
            }
        }
        ServiceDTO.Consultation consultation1 = new ServiceDTO.Consultation();
        //咨询过
        if (hadConsultation) {
            AdminUserDTO.AdminUser adminUser = new AdminUserDTO.AdminUser();
            adminUser.setId(info.getOpUserId());
            BaseResponse<AdminUserDTO.QueryResp> queryRespBaseResponse = frontClient.find(adminUser);
            result.setName(queryRespBaseResponse.getResult().getAdminUsers().get(0).getNickname());
            result.setQq(queryRespBaseResponse.getResult().getAdminUsers().get(0).getQqAccount());
            result.setTel(queryRespBaseResponse.getResult().getAdminUsers().get(0).getMobile());
            result.setTitle(queryRespBaseResponse.getResult().getAdminUsers().get(0).getTitle());
            result.setAvatar(queryRespBaseResponse.getResult().getAdminUsers().get(0).getAvatar());

            consultation1.setOpUserId(info.getOpUserId());
            consultation1.setOpUserMobile(queryRespBaseResponse.getResult().getAdminUsers().get(0).getMobile());
            consultation1.setOpUserName(queryRespBaseResponse.getResult().getAdminUsers().get(0).getNickname());

        } else {
            TrademarkDTO.Consultation consultation = new TrademarkDTO.Consultation();
            consultation.setId(id);
            BaseResponse<TrademarkDTO.ConsultationResp> response = frontClient.consultation(consultation);
            if (!response.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
                throw new ServiceException(response.getStatus(), response.getMessage());
            }
            //返回任意销售信息
            result.setName(response.getResult().getNickname());
            result.setQq(response.getResult().getQqAccount() == null ? "" : response.getResult().getQqAccount());
            result.setTel(response.getResult().getMobile());
            result.setTitle(response.getResult().getTitle() == null ? "" : response.getResult().getTitle());
            result.setAvatar(response.getResult().getAvatar());

            consultation1.setOpUserId(response.getResult().getId());
//            consultation1.setOpUserMobile(response.getResult().getMobile());
//            consultation1.setOpUserName(response.getResult().getNickname());
        }

        //应该记录一下咨询记录
        consultation1.setOrderNo(id == null ? "" : id);
        consultation1.setStatus(1);
        consultation1.setOpenId(AppUserPrincipal.getPrincipal().getMinId());
//        consultation1.setBuyerMobile(AppUserPrincipal.getPrincipal().getName());
//        consultation1.setBuyerName(AppUserPrincipal.getPrincipal().getMinId());

        BaseResponse<ServiceDTO.Resp> responseAdd = frontClient.addConsultation(consultation1);
        if (!responseAdd.getStatus().equals(BaseResponse.STATUS_HANDLE_SUCCESS)) {
            throw new ServiceException(responseAdd.getStatus(), responseAdd.getMessage());
        }

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
            node.setName(e.getCode() + "类 " + e.getCategoryName());
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
            node.setName(e.getCode() + " " + e.getCategoryName());
            rootBrandClasses.add(node);
        });
        res.setBrandClasses(rootBrandClasses);
//        BeanUtils.copyProperties(response.getResult().getCates(), res);
        return res;
    }

    @ApiOperation("小程序获取手机")
    @PostMapping("/min/phone")
    public Object getPhoneNumber(@RequestBody Common.MinGetPhone minGetPhone) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(minGetPhone.getEncryptedData());
        // 加密秘钥
        byte[] keyByte = Base64.decode(minGetPhone.getSession_key());
        // 偏移量
        byte[] ivByte = Base64.decode(minGetPhone.getIv());
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
