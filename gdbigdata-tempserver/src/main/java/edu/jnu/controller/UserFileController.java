package edu.jnu.controller;

import com.aliyun.oss.OSS;
import edu.jnu.domain.UserFilePosition;
import edu.jnu.dto.FileInfosDto;
import edu.jnu.dto.UploadFileDto;
import edu.jnu.enums.ResponseEnum;
import edu.jnu.exception.ConditionException;
import edu.jnu.service.OSSService;
import edu.jnu.service.UserFilePositionService;
import edu.jnu.utils.Tools;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户文件操作.
 * 接受用户上传的文件，对文件做相应的处理，然后进行存储，
 * 或者根据用户指令取出相应的文件或者文件夹.
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 20:03
 */
@RestController
public class UserFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFileController.class);

    @Autowired
    private OSSService ossService;

    @Autowired
    private UserFilePositionService userFilePositionService;

    /**
     * 上传文件.
     * 根据用户上传的密钥和
     * @param uploadFileDto
     * @return
     */
    @PostMapping(value = "/files")
    public ResponseEntity<?> uploadFile(UploadFileDto uploadFileDto,
                                        @RequestParam("file") MultipartFile multipartFile) {
        // 将文件上传到OSS服务器中
        OSS oss = ossService.createOss(uploadFileDto.getEndpoint(), uploadFileDto.getAccessKeyId(), uploadFileDto.getAccessKeySecret());
        try {
            ossService.uploadObj2OSS(oss, "dupless/"+uploadFileDto.getFileStoragePath()+"/"+multipartFile.getOriginalFilename(),
                    uploadFileDto.getBucketName(), multipartFile.getInputStream());
        } catch (IOException e) {
            LOGGER.info("文件在数据流操作阶段出错："+e);
            throw new ConditionException(ResponseEnum.FAIL);
        }
        // 如果存储成功
        UserFilePosition userFilePosition = new UserFilePosition("dupless/"+uploadFileDto.getFileStoragePath()+"/"+multipartFile.getOriginalFilename());
        // 记录用户文件位置
        userFilePositionService.putFilePosition(userFilePosition);
        LOGGER.info("文件:"+multipartFile.getOriginalFilename()+"已存在于服务器中");
        return ResponseEntity.ok("文件已经上传成功");
    }

    /**
     * 下载文件.
     * 根据文件名来下载数据
     * @return
     */
    @GetMapping(value = "/files/{fileName}")
    public void downloadFile(@PathVariable("fileName") String fileName,
                                          String endpoint, String accessKeyId, String accessKeySecret,
                                          String fileStoragePath, String bucketName, HttpServletResponse response) throws IOException {
        OSS oss = ossService.createOss(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = ossService.downloadObj(oss, "dupless/"+fileStoragePath+"/"+fileName, bucketName);
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        IOUtils.copy(inputStream, response.getOutputStream());
        oss.shutdown();
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(response.getOutputStream());
    }

    /**
     * 获取用户的所有文件路径.
     * 根据用户ID获取该用户在OSS上的所有文件路径.
     * @return  该用户所有的文件路径
     */
    @GetMapping(value = "/fileInfos")
    public ResponseEntity<List<FileInfosDto>> getUserFilePaths(@RequestParam(value = "page", defaultValue = "0", required = false) Integer pageOffset,
                                                               @RequestParam(value = "size", defaultValue = "8", required = false) Integer size) {

        List<FileInfosDto> page = userFilePositionService.listUserFilePosition(pageOffset, size);
        // 从数据库中获取用户id所拥有的所有文件的相对路径
        return ResponseEntity.ok(page);
    }

    /**
     * 获取文件信息总记录数
     * @return
     */
    @GetMapping(value = "/fileInfos/totals")
    public ResponseEntity<Integer> getFileInfoTotals() {
        Integer totals = userFilePositionService.getTotals();
        return ResponseEntity.ok(totals);
    }

    @DeleteMapping(value = "/files/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable("fileName") String fileName) {
        userFilePositionService.deleteFilePosition(fileName);
        return ResponseEntity.ok(null);
    }
}
