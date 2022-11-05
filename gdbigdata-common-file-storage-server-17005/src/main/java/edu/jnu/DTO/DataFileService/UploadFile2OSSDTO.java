package edu.jnu.DTO.DataFileService;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月23日 23:10
 */
@Getter
@Setter
@ApiModel("上传文件至本地存储实体类")
public class UploadFile2OSSDTO {
    @JSONField(name = "aux")
    private String aux;

    private String v;

    private String w;
}
