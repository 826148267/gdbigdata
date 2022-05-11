package edu.jnu.controller;

import edu.jnu.dto.SepdpInitDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 存储用户文件的类
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月09日 13:38
 */
@RestController
public class StorageApi {

    @PostMapping(value = "/sepdp/init")
    public ResponseEntity<?> sepdpInitStorage(SepdpInitDto sepdpInitDto,
                                              @RequestParam("file") MultipartFile multipartFile) {
        System.out.println("进来了");
        return null;
    }

}
