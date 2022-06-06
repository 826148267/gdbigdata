package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import edu.jnu.entity.FilePosition;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月05日 16:50
 */
public class GetFileListDto {
    @JSONField(name = "fileName")
    private String fileName;
    @JSONField(name = "dataPath")
    private String dataPath;
    @JSONField(name = "tagPath")
    private String tagPath;
    @JSONField(name = "blockNum")
    private Integer blockNum;

    public GetFileListDto(FilePosition filePosition) {
        this.fileName = filePosition.getFileName();
        this.dataPath = filePosition.getDataFilePath();
        this.tagPath = filePosition.getTagFilePath();
        this.blockNum = filePosition.getBlockNum();
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public Integer getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(Integer blockNum) {
        this.blockNum = blockNum;
    }
}
