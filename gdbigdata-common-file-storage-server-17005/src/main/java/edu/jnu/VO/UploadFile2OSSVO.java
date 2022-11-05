package edu.jnu.VO;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月23日 19:42
 */
@Getter
@Setter
@ApiModel("上传文件至本地存储实体类")
public class UploadFile2OSSVO {
    @JSONField(name = "userId")
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @JSONField(name = "dataFile")
    @ApiModelProperty(value = "数据文件", required = true)
    private MultipartFile dataFile;
    @JSONField(name = "keyFile")
    @ApiModelProperty(value = "密钥文件", required = true)
    private MultipartFile keyFile;
    @JSONField(name = "mimeType")
    @ApiModelProperty(value = "文件格式", required = true)
    private String mimeType;
    @JSONField(name = "fileHashValue")
    @ApiModelProperty(value = "数据文件hash值", required = true)
    private String fileHashValue;
    @JSONField(name = "fileDir")
    @ApiModelProperty(value = "文件路径", required = true)
    private String fileDir;
}
