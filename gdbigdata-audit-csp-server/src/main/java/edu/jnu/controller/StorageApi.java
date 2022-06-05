package edu.jnu.controller;

import com.aliyun.oss.OSS;
import edu.jnu.dto.UploadFileDto;
import edu.jnu.entity.FilePosition;
import edu.jnu.service.FilePositionService;
import edu.jnu.service.OSSService;
import edu.jnu.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月28日 10:47
 */
@RestController
public class StorageApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageApi.class);

    @Autowired
    private OSSService ossService;

    @Autowired
    private FilePositionService filePositionService;

    /**
     * 上传文件.
     * 根据用户上传的密钥和
     * @param uploadFileDto
     * @return
     */
    @PostMapping(value = "/files")
    public ResponseEntity<?> uploadFile(UploadFileDto uploadFileDto) {
        // 将文件上传到OSS服务器中
        // 获取文件数组
        ArrayList<MultipartFile> files = uploadFileDto.getFile();
        long count = files.stream().filter(file -> {
            String preFileName = Tools.getPreNameInOriginalFile(Objects.requireNonNull(file.getOriginalFilename()));
            try {
                // 如果文件名的前缀为data，就存储到数据文件夹，否则存到标签文件夹
                ossService.uploadObj2OSS("audit/"+preFileName+"/"+uploadFileDto.getFileStoragePath()+"/"+file.getOriginalFilename(),
                        uploadFileDto.getBucketName(), file.getInputStream());
            } catch (IOException e) {
                LOGGER.info("文件"+file.getOriginalFilename()+"在数据流操作阶段出错："+e);
                return false;
            }
            // 能到这个位置说明已经成功执行oss文件上传，接下来将文件位置记录到数据库中
            boolean flag = false;
            if ("data".equals(preFileName)) {
                flag = filePositionService.recordPosition(uploadFileDto.getFileStoragePath(), file.getOriginalFilename());
            }
            if (flag) {
                LOGGER.info("文件"+file.getOriginalFilename()+"位置已经成功记录到数据库中");
                return true;
            } else {
                LOGGER.info("文件"+file.getOriginalFilename()+"位置记录到数据库中时发生错误");
                return false;
            }
        }).count();
        if (count == files.size()){     // 如果都存储成功了,返回OK
            LOGGER.info("文件全数上传成功");
            return ResponseEntity.ok("数据文件与标签文件存储成功");
        } else {    // 否则报错
            LOGGER.info("OSS上传服务出现异常，上传文件时并没有全部上传成功");
            return ResponseEntity.internalServerError().body("服务器内部错误");
        }
    }
}
