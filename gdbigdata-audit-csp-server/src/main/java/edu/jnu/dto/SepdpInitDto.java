package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月09日 14:00
 */
public class SepdpInitDto {
    @JSONField(name = "R")
    private String R;
    @JSONField(name = "name")
    private String userName;
    @JSONField(name = "path")
    private String filePath;

    public SepdpInitDto() {
    }

    public SepdpInitDto(String r, String userName, String filePath) {
        R = r;
        this.userName = userName;
        this.filePath = filePath;
    }

    public String getR() {
        return R;
    }

    public void setR(String r) {
        R = r;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
