package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月15日 14:21
 */
public class SaveDataFileDto {
    @JSONField(name = "userId")
    private String userId;    // 用户id
    @JSONField(name = "dataHashValue")
    private String dataHashValue;   // 文件内容的hash值
    @JSONField(name = "strategy")
    private Integer strategy;    // 去重策略:存储时去重、存储后去重
    @JSONField(name = "storageType")
    private Integer storageType;     //  存储类型:OSS、大数据平台等
    @JSONField(name = "file")
    private MultipartFile file;     // 待存储文件

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataHashValue() {
        return dataHashValue;
    }

    public void setDataHashValue(String dataHashValue) {
        this.dataHashValue = dataHashValue;
    }

    public Integer getStrategy() {
        return strategy;
    }

    public void setStrategy(Integer strategy) {
        this.strategy = strategy;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
