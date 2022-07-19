package com.meta.utils;

import cn.hutool.core.util.IdUtil;
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

    @Value("${QINIU_AK:}")
    private String QINIU_AK;
    @Value("${QINIU_SK:}")
    private String QINIU_SK;
    @Value("${QINIU_BUCKET:}")
    private String QINIU_BUCKET;
    @Value("${QINIU_DOMAIN:}")
    private String QINIU_DOMAIN;

    // 上传本地文件(默认不指定key的情况下，以文件内容的hash值作为文件名)
    public String uploadLocalFile(String localFilePath, String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(QINIU_AK, QINIU_SK);
        String upToken = auth.uploadToken(QINIU_BUCKET);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return QINIU_DOMAIN + "/" + key;
        } catch (QiniuException ex) {
            log.info("文件上传失败，localFilePath:{},key:{}", localFilePath, key);
            return null;
        }
    }

    // 上传流文件
    public String uploadStream(MultipartFile file){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(QINIU_AK, QINIU_SK);
        String upToken = auth.uploadToken(QINIU_BUCKET);
        // 获取文件名
        String fileName = file.getOriginalFilename().replace(" ","");
        int index = fileName.lastIndexOf(".");
        String fileNamePrefix = fileName.substring(0, index);
        String fileNameSuffix = fileName.substring(index);
        String key = IdUtil.simpleUUID()+fileNameSuffix;
        try {
            Response response = uploadManager.put(file.getInputStream(),key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return QINIU_DOMAIN + "/" + key;
        } catch (Exception ex) {
            log.info("文件上传失败,key:{}", key);
            return null;
        }
    }

    // 下载私有空间文件
    public String downloadPrivate(String baseUrl) {
        String downloadUrl = null;
        try{
            Auth auth = Auth.create(QINIU_AK, QINIU_SK);
            downloadUrl = auth.privateDownloadUrl(baseUrl, 86400L);
            log.info("downloadPrivate ---> {}", downloadUrl);
        }catch (Exception e){
            log.info("downloadPrivate ---> error, message:{}", e.getMessage());
        }
        return downloadUrl;
    }

}
