package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 20:56
 */
public class ProofIntegrityDto {
    @JSONField(name = "bucketName")
    private String bucketName;
    @JSONField(name = "filePath")
    private String filePath;
    @JSONField(name = "tagPath")
    private String tagPath;
    @JSONField(name = "iList")
    private ArrayList<Integer> iList;
    @JSONField(name = "vList")
    private ArrayList<BigInteger> vList;

    public ProofIntegrityDto() {
    }

    public ProofIntegrityDto(String bucketName, String filePath, ArrayList<Integer> iList, ArrayList<BigInteger> vList) {
        this.bucketName = bucketName;
        this.filePath = filePath;
        this.iList = iList;
        this.vList = vList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Integer> getiList() {
        return iList;
    }

    public void setiList(ArrayList<Integer> iList) {
        this.iList = iList;
    }

    public ArrayList<BigInteger> getvList() {
        return vList;
    }

    public void setvList(ArrayList<BigInteger> vList) {
        this.vList = vList;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }
}
