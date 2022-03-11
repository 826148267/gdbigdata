package edu.jnu.domain;

import org.hibernate.annotations.Table;

import javax.persistence.*;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 20:52
 */
@Entity(name = "table_user_file_position")
public class UserFilePosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "1") //自增
    private int id;
    @Column
    private int userId;
    @Column
    private String filePath;

    public UserFilePosition() {
    }

    public UserFilePosition(int userId, String filePath) {
        this.userId = userId;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
