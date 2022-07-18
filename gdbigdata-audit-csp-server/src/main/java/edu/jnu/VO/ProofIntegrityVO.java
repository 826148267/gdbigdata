package edu.jnu.VO;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 20:56
 */
public class ProofIntegrityVO {
    @JSONField(name = "filePath")
    private String filePath;
    @JSONField(name = "fileName")
    private String fileName;
    @JSONField(name = "tagFileId")
    private String tagFileId;
    @JSONField(name = "iList")
    private ArrayList<Integer> iList;
    @JSONField(name = "vList")
    private ArrayList<BigInteger> vList;

    public ProofIntegrityVO() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTagFileId() {
        return tagFileId;
    }

    public void setTagFileId(String tagFileId) {
        this.tagFileId = tagFileId;
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
}
