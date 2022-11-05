package edu.jnu.PO;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月01日 10:45
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "tb_data_file")
public class DataFilePO {
    @Id
    @Column
    private String id = UUID.randomUUID().toString().replace("-", "");  // 数据文件唯一id

    @NonNull
    @Column
    private String logicFileDir;    // 逻辑文件路径

    @NonNull
    @Column
    private String logicFileName;   // 逻辑文件名

    @NonNull
    @Column
    private String actualFileDir;   // 实际文件目录

    @NonNull
    @Column
    private String actualFileName;  // 实际文件名

    @Column
    private String tagFileId;   // 标签文件Id

    @NonNull
    @Column
    private String keyFileId;   // 密钥文件id

    @NonNull
    @Column
    private String userId;  // 用户id

    @NonNull
    @Column
    private String mimeType;    // 源数据文件的文件格式

    @NonNull
    @Column
    private String hashValue;   // 密态数据文件内容的hash值

    @NonNull
    @Column
    private Integer storageType;   // 存储介质
}
