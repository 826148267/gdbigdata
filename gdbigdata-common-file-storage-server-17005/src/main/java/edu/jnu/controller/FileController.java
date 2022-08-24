package edu.jnu.controller;

import edu.jnu.VO.UploadFile2HDFSVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件业务功能接口.
 * <p>
 * 包括文件（含标签文件、密钥文件、数据文件）存、取、删、获取数据文件信息等操作.
 * </p>
 * @author Guo zifan
 * @version 1.0
 * @date 2022年08月24日 11:25
 */
@RestController
@Api(tags = "文件业务功能接口")
public class FileController {

    @ApiOperation(value = "数据文件的存储操作", notes = "此接口将数据文件根据接收到的参数去重后再存储到相应的存储介质中", httpMethod = "POST")
    @GetMapping(value = "hdfs/{fileDir}/{fileName}")
    public ResponseEntity<String> uploadFile2HDFS(@PathVariable("fileDir") String fileDir,
                                                  @PathVariable("fileName") String fileName,
                                                  UploadFile2HDFSVO uploadFile2HDFSVO) {

    }
}
