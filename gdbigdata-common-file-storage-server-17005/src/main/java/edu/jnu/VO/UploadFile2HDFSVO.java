package edu.jnu.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年08月24日 16:09
 */
@ApiModel("上传文件实体类")
public class UploadFile2HDFSVO {
    @JSONField(name = "userId")
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @JSONField(name = "fileStoragePath")
    @ApiModelProperty(value = "文件存储路径", required = true)
    private String fileStoragePath;
    @JSONField(name = "tagFile")
    @ApiModelProperty(value = "标签文件", required = true)
    private MultipartFile tagFile;
    @JSONField(name = "dataFile")
    @ApiModelProperty(value = "数据文件", required = true)
    private MultipartFile dataFile;
    @JSONField(name = "blockNum")
    @ApiModelProperty(value = "数据块数", required = true)
    private Integer blockNum;
    @JSONField(name = "r")
    @ApiModelProperty(value = "验签参数", required = true)
    private String r;
    @JSONField(name = "mimeType")
    @ApiModelProperty(value = "文件格式", required = true)
    private String mimeType;
}
