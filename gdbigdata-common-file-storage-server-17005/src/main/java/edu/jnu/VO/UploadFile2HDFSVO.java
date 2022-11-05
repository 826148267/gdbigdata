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
 * @date 2022年08月24日 16:09
 */
@Getter
@Setter
@ApiModel("上传文件实体类")
public class UploadFile2HDFSVO {
    @JSONField(name = "userId")
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    @JSONField(name = "tagFile")
    @ApiModelProperty(value = "标签文件", required = true)
    private MultipartFile tagFile;
    @JSONField(name = "dataFile")
    @ApiModelProperty(value = "数据文件", required = true)
    private MultipartFile dataFile;
    @JSONField(name = "keyFile")
    @ApiModelProperty(value = "密钥文件", required = true)
    private MultipartFile keyFile;
    @JSONField(name = "blockNum")
    @ApiModelProperty(value = "数据块数", required = true)
    private Integer blockNum;
    @JSONField(name = "r")
    @ApiModelProperty(value = "验签参数", required = true)
    private String r;
    @JSONField(name = "mimeType")
    @ApiModelProperty(value = "文件格式", required = true)
    private String mimeType;
    @JSONField(name = "hashValue")
    @ApiModelProperty(value = "数据文件hash值", required = true)
    private String hashValue;
    @JSONField(name = "fileDir")
    @ApiModelProperty(value = "文件路径", required = true)
    private String fileDir;
}
