package edu.jnu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月09日 22时27分
 * @功能描述: TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileInfoDTO {
    private String fileAbstract;
    private String userName;
    private String fileName;
    private String mimeType;
    private String dedupKeyCipher;
}
