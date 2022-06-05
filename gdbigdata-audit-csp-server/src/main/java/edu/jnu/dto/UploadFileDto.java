package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月12日 21:52
 */
public class UploadFileDto {
    @JSONField(name = "userName")
    private String userName;
    @JSONField(name = "bucketName")
    private String bucketName;
    @JSONField(name = "fileStoragePath")
    private String fileStoragePath;
    @JSONField(name = "file")
    private ArrayList<MultipartFile> file;

    public ArrayList<MultipartFile> getFile() {
        return file;
    }

    public void setFile(ArrayList<MultipartFile> file) {
        this.file = file;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileStoragePath() {
        return fileStoragePath;
    }

    public void setFileStoragePath(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

}
