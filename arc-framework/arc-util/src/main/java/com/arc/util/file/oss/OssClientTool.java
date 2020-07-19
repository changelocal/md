package com.arc.util.file.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;


@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "oss.client",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false)
@Slf4j
public class OssClientTool {

    private final OssProperties properties;
    private final OSSClient ossClient;

    public OssClientTool(OssProperties properties) {
        this.properties = properties;
        ossClient = new OSSClient(properties.getEndpoint(), (CredentialsProvider)(new DefaultCredentialProvider(properties.getAccessKeyId()
                , properties.getAccessKeySecret())), (ClientConfiguration)null);
    }


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
            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(properties.getBucketName(), fileName, instream));
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
            PutObjectResult putResult = ossClient.putObject(new PutObjectRequest(properties.getBucketName(), fileName, instream));
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
        OSSObject object = ossClient.getObject(properties.getBucketName(), properties.getRealPath().concat(fileName));
        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        byte[] bytes = IOUtils.toByteArray(objectContent);
        objectContent.close();

        return bytes;
    }
}
