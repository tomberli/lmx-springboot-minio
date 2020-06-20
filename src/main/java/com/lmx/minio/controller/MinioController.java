package com.lmx.minio.controller;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author：lmx
 */
@RestController
public class MinioController {
    final Logger logger = LoggerFactory.getLogger(MinioController.class);
    @Value("${minio.endpoint}")
    private  String ENDPOINT;
    @Value("${minio.bucketName}")
    private  String BUCKETNAME;
    @Value("${minio.accessKey}")
    private  String ACCESSKEY;
    @Value("${minio.secretKey}")
    private  String SECRETKEY;

    @Autowired
    private MinioClient minioClient;

    /**
     * 上传文件 图片
     * @param file
     * @return
     */
    @PostMapping("/upload-file")
    public String upload(MultipartFile file) {
        try {
            if (!minioClient.bucketExists(BUCKETNAME)) {
                minioClient.makeBucket(BUCKETNAME);
            }
            String filename = file.getOriginalFilename();
            String objectName = "minio-lmx/" + UUID.randomUUID()+filename;
            minioClient.putObject(BUCKETNAME, objectName, file.getInputStream(), file.getContentType());
            String path = ENDPOINT+"/"+BUCKETNAME+"/"+objectName;
            logger.info("上传文件路径："+path);

        } catch (Exception e) {
            logger.error("错误：{}",e.getMessage());
            return "上传失败";
        }
        return "上传成功";
    }

    /**
     * 根据文件路径删除，这里的文件路径除了桶名，其它路径要完整。
     * @param fileName
     * @return
     */
    @DeleteMapping("delete-file")
    public String delete(String fileName) {
        try {
            minioClient.removeObject(BUCKETNAME, fileName);
        } catch (Exception e) {
            return "删除失败"+e.getMessage();
        }
        return "删除成功";
    }
}
