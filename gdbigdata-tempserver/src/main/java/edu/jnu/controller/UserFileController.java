package edu.jnu.controller;

import edu.jnu.config.response.JsonResponse;
import edu.jnu.domain.UserFilePosition;
import edu.jnu.enums.ResponseEnum;
import edu.jnu.service.OSSService;
import edu.jnu.service.UserFilePositionService;
import edu.jnu.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
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

    @PostMapping(value = "/users/{userId}/files")
    public JsonResponse<?> uploadFile(@PathVariable("userId") int userId,
                                      @RequestParam("file") MultipartFile file) {

        // 将文件上传到OSS服务器中
        try {
            ossService.uploadObj2OSS("dupless/userfile/"+userId+"/"+file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            LOGGER.info("文件在数据流操作阶段出错："+e);
            return new JsonResponse<>(ResponseEnum.FAIL);
        }
        // 如果存储成功

        UserFilePosition userFilePosition = new UserFilePosition(userId, "dupless/userfile/"+userId+"/"+file.getOriginalFilename());
        // 记录用户文件位置
        userFilePositionService.putFilePosition(userFilePosition);
        LOGGER.info("用户id:"+userId+"的文件:"+file.getOriginalFilename()+"已存在于服务器中");
        return new JsonResponse<>(ResponseEnum.SUCCESS);
    }

    /**
     * 获取单个用户文件.
     * 根据用户ID和用户提供的文件名从OSS中获取
     * @param userId    用户ID
     * @param fileName  在OSS中的文件名，含"/"
     * @return  返回的具体数据中，主要包含对应的用户文件
     */
    @GetMapping(value = "/users/{userId}/files/{fileName}")
    public ResponseEntity<?> getUserFile(@PathVariable("userId") int userId,
                                         @PathVariable("fileName") String fileName) {
        InputStream inputStream = ossService.downloadObj("dupless/userfile/"+userId+"/"+fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add ( "Content-Disposition",String.format("attachment;filename=\"%s",fileName));
        headers.add ( "Cache-Control","no-cache,no-store,must-revalidate" );
        headers.add ( "Pragma","no-cache" );
        headers.add ( "Expires","0" );
        ResponseEntity<Object> response = ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(Tools.getContentType(fileName)))
                .body(inputStream);
        return response;
    }

    /**
     * 获取用户的所有文件路径.
     * 根据用户ID获取该用户在OSS上的所有文件路径.
     * @param userId 用户ID
     * @return  该用户所有的文件路径
     */
    @GetMapping(value = "/users/{userId}/filePaths")
    public ResponseEntity<List<String>> getUserFilePaths(@PathVariable("userId") int userId) {
        // 从数据库中获取用户id所拥有的所有文件的相对路径
        List<String> fps = userFilePositionService.listFilePaths(userId);
        ResponseEntity<List<String>> response = ResponseEntity.ok(fps);
        return response;
    }
}
