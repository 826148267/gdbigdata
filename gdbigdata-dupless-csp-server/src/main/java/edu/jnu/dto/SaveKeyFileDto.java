package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年07月10日 20:11
 */
public class SaveKeyFileDto {
    @JSONField(name = "userId")
    private String userId;    // 用户id
    @JSONField(name = "file")
    private MultipartFile file;     // 待存储文件
    @JSONField(name = "storageType")
    private Integer storageType;     //  存储类型:OSS、大数据平台等

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }
}
