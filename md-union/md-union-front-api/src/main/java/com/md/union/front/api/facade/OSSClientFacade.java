package com.md.union.front.api.facade;


import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class OSSClientFacade {
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.filedir}")
    private String realPath;
    @Autowired
    private OSSClient ossClient;
    /**
     * 上传文件格式
     * @param file
     * @param fileName
     * @return
     */
    public String uploadImg2Oss(MultipartFile file, String fileName) {

        String ret = "";
        InputStream instream = null;
        try {
            instream = file.getInputStream();
            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(bucketName, fileName, instream));
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("图片上传失败");
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 上传文件格式
     * @param file
     * @param fileName
     * @return
     */
    public String uploadBufferImg2Oss(BufferedImage file, String fileName) throws IOException {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(file, "jpeg", imOut);
        InputStream instream = new ByteArrayInputStream(bs.toByteArray());
        String ret = "";
        try {
            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(bucketName, fileName, instream));
            ret = putResult.getETag();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("图片上传失败");
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获得图片文件
     * @param fileName
     * @return
     */
    public byte[] getImgFromOss(String fileName) throws IOException {
        OSSObject object = ossClient.getObject(bucketName, realPath.concat(fileName));
        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectContent);
//        byte[] bytes = null;
        objectContent.close();

        return bytes;
    }
}