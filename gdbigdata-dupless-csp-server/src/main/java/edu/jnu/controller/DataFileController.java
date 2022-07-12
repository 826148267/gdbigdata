package edu.jnu.controller;

import edu.jnu.constant.DeduplicateStrategy;
import edu.jnu.domain.FileInfo;
import edu.jnu.dto.GetUserFileListDto;
import edu.jnu.dto.SaveDataFileDto;
import edu.jnu.dto.SaveKeyFileDto;
import edu.jnu.service.DeduplicateService;
import edu.jnu.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * 文件操作接口.
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月15日 14:12
 */
@RestController
public class DataFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataFileController.class);

    @Autowired
    private DeduplicateService deduplicateService;

    @Autowired
    private FileService fileService;

    /**
     * 将文件根据去重策略和存储类型进行持久化存储.
     * @param saveDataFileDto 存储数据文件时的传输实体类
     * @return  返回真实路径
     */
    @PostMapping("data-file")
    public ResponseEntity<String> saveDataFile(SaveDataFileDto saveDataFileDto) {
        int strategy = saveDataFileDto.getStrategy();
        boolean flag;
        FileInfo fileInfo = new FileInfo(saveDataFileDto);
        if (strategy == DeduplicateStrategy.SAVE_WITH_DEDUPLICATE.getCode()) {     // 如果满足存储前去重的策略，则先去重再存储
            fileInfo.setDeduplicateFlag(1); // 将去重标志位置为1
            flag = deduplicateService.saveWithDeduplicate(fileInfo); //  保存文件并去重
        } else if (strategy == DeduplicateStrategy.SAVE_WITHOUT_DEDUPLICATE.getCode()) {    // 如果满足存储后去重的策略，则直接存储
            fileInfo.setDeduplicateFlag(0); // 将去重标志位置为0
            flag = fileService.saveFileSimple(fileInfo);    // 直接保存文件
        } else {    // 暂不支持其他去重策略
            LOGGER.info("暂不支持其他去重策略");
            return ResponseEntity.internalServerError().body("暂不支持其他去重策略");
        }
        if (flag) {
            LOGGER.info("文件:"+saveDataFileDto.getFile().getOriginalFilename()+"保存成功");
            return ResponseEntity.ok("文件保存成功");
        } else {
            LOGGER.error("文件:"+saveDataFileDto.getFile().getOriginalFilename()+"保存失败");
            return ResponseEntity.internalServerError().body("文件保存失败");
        }
    }

    /**
     * 简单存储密钥文件（无去重）.
     * @param saveKeyFileDto  此传输对象包含userId，file
     * @return  返回操作执行结果，true为操作成功，false为操作失败
     */
    @PostMapping("key-file")
    public ResponseEntity<String> saveKeyFile(SaveKeyFileDto saveKeyFileDto) {
        if (deduplicateService.saveKeyFile(saveKeyFileDto.getUserId(), saveKeyFileDto.getStorageType(), saveKeyFileDto.getFile())) {
            LOGGER.info("存储密钥文件成功");
            return ResponseEntity.ok(null);
        } else {
            LOGGER.error("内部错误，存储密钥文件失败");
            return ResponseEntity.internalServerError().body("存储密钥文件失败，服务器内部错误");
        }
    }

    /**
     * 进行全局去重.
     * @return 返回全局去重操作执行状态
     */
    @GetMapping("deduplication-global")
    public ResponseEntity<String> deduplicationGlobal() {
        if (deduplicateService.deduplicationGlobal()) {
            LOGGER.info("全局文件去重成功");
            return ResponseEntity.ok("全局文件去重已完成");
        } else {
            LOGGER.error("全局文件去重失败");
            return ResponseEntity.internalServerError().body("全局文件去重失败");
        }
    }

    /**
     * 根据用户Id分页获取用户文件列表.
     * @param userId 用户Id
     * @param currentPage 当前页数
     * @param size 页容量
     * @return 返回该用户Id所拥有的文件信息的列表
     */
    @GetMapping("{userId}/files")
    public ResponseEntity<GetUserFileListDto> getFileListByUserId(@PathVariable("userId") String userId,
                                                                  @RequestParam(value = "page", defaultValue = "0", required = false) Integer currentPage,
                                                                  @RequestParam(value = "size", defaultValue = "8", required = false) Integer size) {
//        根据用户id获取其所拥有的文件信息列表
        GetUserFileListDto result = fileService.getFileListByUserId(userId, currentPage, size);
        LOGGER.info("分页获取文件列表成功");
        return ResponseEntity.ok(result);
    }


    /**
     * 根据用户给定的fileId下载文件
     * @param fileId    需要下载的文件Id
     * @return  返回内存中的byte[]，byte[]为文件内容
     */
    @GetMapping("/oss/data-files/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadDateFile(@PathVariable("fileId") String fileId) {
        try {
            byte[] bytes = fileService.downloadFileByFileIdInOss(fileId);
            ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.empty());  // 由于客户端接收到文件还要做预处理，所以此处不向浏览器提下载或者是打开的建议
            headers.setLastModified(new Date().getTime());
            headers.setETag("\"" + System.currentTimeMillis() + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            LOGGER.info("文件fileId:{}下载成功", fileId);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(bytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayResource);
        } catch (Exception e) {
            LOGGER.info("文件fileId:{}下载失败，cause:{}", fileId, e);
        }
        return ResponseEntity.internalServerError().body(null);
    }

    @GetMapping("/oss/key-files/{dataFileId}")
    public ResponseEntity<ByteArrayResource> downloadKeyFile(@PathVariable("dataFileId") String dataFileId) {
        String keyFileId = deduplicateService.getKeyFileIdByDataFileId(dataFileId);
        return null;
    }
}
