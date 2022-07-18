package edu.jnu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月12日 21:52
 */
public class UploadFileVO {
    @JSONField(name = "userId")
    private String userId;
    @JSONField(name = "fileStoragePath")
    private String fileStoragePath;
    @JSONField(name = "tagFile")
    private MultipartFile tagFile;
    @JSONField(name = "dataFile")
    private MultipartFile dataFile;
    @JSONField(name = "blockNum")
    private Integer blockNum;
    @JSONField(name = "R")
    private String R;
    @JSONField(name = "mimeType")
    private String mimeType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileStoragePath() {
        return fileStoragePath;
    }

    public void setFileStoragePath(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

    public MultipartFile getTagFile() {
        return tagFile;
    }

    public void setTagFile(MultipartFile tagFile) {
        this.tagFile = tagFile;
    }

    public MultipartFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(MultipartFile dataFile) {
        this.dataFile = dataFile;
    }

    public Integer getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }

    public String getR() {
        return R;
    }

    public void setR(String r) {
        R = r;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
