package edu.jnu.DTO.DataFileService;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据文件服务的保存文件时的参数列表.
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:54
 */
@Getter
@Setter
@Accessors(chain = true)
public class SaveDTO {
    private String logicFileDir;    // 逻辑文件路径
    private String logicFileName;   // 逻辑文件名
    private String actualFileDir;   // 实际文件目录
    private String actualFileName;  // 实际文件名
    private String tagFileId;   // 标签文件Id
    private String keyFileId;   // 密钥文件id
    private String userId;  // 用户id
    private String mimeType;    // 源数据文件的文件格式
    private String hashValue;   // 密态数据文件内容的hash值
}
