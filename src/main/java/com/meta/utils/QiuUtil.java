package com.meta.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Component
public class QiuUtil {

    @Value("${qiu.accessKey:}")
    private String accessKey;
    @Value("${qiu.secretKey:}")
    private String secretKey;
    @Value("${qiu.bucket.name:}")
    private String bucketName;
    @Value("${qiu.domain:}")
    private String domain;

    // 上传本地文件(默认不指定key的情况下，以文件内容的hash值作为文件名)
    public String uploadLocalFile(String localFilePath, String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return domain + "/" + key;
        } catch (QiniuException ex) {
            log.info("文件上传失败，localFilePath:{},key:{}", localFilePath, key);
            return null;
        }
    }

    // 上传流文件
    public String uploadStream(MultipartFile file, String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);
        try {
            Response response = uploadManager.put(file.getInputStream(),key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            System.out.println(domain + "/" + key);
            return domain + "/" + key;
        } catch (Exception ex) {
            log.info("文件上传失败,key:{}", key);
            return null;
        }
    }

    // 下载私有空间文件
    public String downloadPrivate(String baseUrl){
        Auth auth = Auth.create(accessKey, secretKey);
        String downloadUrl = auth.privateDownloadUrl(baseUrl);
        return downloadUrl;
    }

}
