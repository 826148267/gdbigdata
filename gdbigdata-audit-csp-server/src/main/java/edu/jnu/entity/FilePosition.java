package edu.jnu.entity;

import javax.persistence.*;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 20:52
 */
@Entity(name = "table_file_position")
public class FilePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "1") //自增
    private int id;
    @Column
    private String dataFilePath;
    @Column
    private String tagFilePath;

    public FilePosition(String fileStoragePath, String originalFilename) {
        this.dataFilePath = "audit/data/"+fileStoragePath+"/"+originalFilename;
        String[] tmps = originalFilename.split("-");
        String tagFileName = "tag";
        for (int i = 1; i < tmps.length; i++) {
            tagFileName = "-"+tmps[i];
        }
        this.tagFilePath = "audit/tag/"+fileStoragePath+"/"+tagFileName;
    }

    public FilePosition() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getTagFilePath() {
        return tagFilePath;
    }

    public void setTagFilePath(String tagFilePath) {
        this.tagFilePath = tagFilePath;
    }
}
