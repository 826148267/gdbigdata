package edu.jnu.controller;

import edu.jnu.DTO.DataFileService.SaveDTO;
import edu.jnu.POJO.AuxiliaryTranslator;
import edu.jnu.VO.UploadFile2HDFSVO;
import edu.jnu.VO.UploadFile2OSSVO;
import edu.jnu.constant.StorageType;
import edu.jnu.service.DataFileService;
import edu.jnu.service.KeyFileService;
import edu.jnu.service.TagFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件业务功能接口.
 * <p>
 * 包括文件（含标签文件、密钥文件、数据文件）存、取、删、获取数据文件信息等操作,
 * 只管存储业务.
 * </p>
 * @author Guo zifan
 * @version 1.0
 * @date 2022年08月24日 11:25
 */
@RestController
@Api(tags = "文件业务功能接口")
@Slf4j
public class FileController {

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private KeyFileService keyFileService;

    @Autowired
    private TagFileService tagFileService;

    /**
     * 上传数据文件和密钥文件到OSS中.
     * <pre>
     * 具体做法：
     *      1、判断已有的文件记录中是否存在hash值与此文件hash值相同的
     *      2、如果能找到hash值相同的记录，则说明可以去重，进行去重存储；
     *         如果不能找到hash值相同的记录，则说明不可以去重，进行首次存储
     *      3、如果存在重复，
     *          （1）根据文件路径存储密钥文件，并记录到表中
     *          （2）记录数据文件的实际位置和逻辑位置到数据库表中
     *          （3）检索标签转化辅助材料
     *          （4）告知用户已存在重复文件，返回标签转化辅助材料
     *      4、如果不存在重复，
     *          （1）根据文件路径存储数据文件
     *          （2）记录数据文件的位置到数据库表中
     *          （3）告知为用户无重复文件
     * </pre>
     * @param uploadFile2OSSVO 上传文件使用到的参数对象
     * @return 200代表上传成功、不可去重；201代表上传成功、可去重
     */
    @ApiOperation(value = "数据文件的存储操作（论文实验版）",
            notes = "此接口数据文件和密钥文件根据接收到的参数去重后存储到OSS",
            httpMethod = "POST")
    @PostMapping(value = "oss")
    public ResponseEntity<Map> uploadFile2OSS(UploadFile2OSSVO uploadFile2OSSVO) {
        // 判断数据库表中是否含有相同hash值的记录
        boolean flag = dataFileService.existsByHashValue(uploadFile2OSSVO.getFileHashValue());
        /*
            如果能找到hash值相同的记录，则说明可以去重，进行去重存储
            如果不能找到hash值相同的记录，则说明不可以去重，进行首次存储
         */
        if (flag) { // 存在重复的情况
            // 根据文件路径存储密钥文件，并记录到表中
            String keyFileId = null;
            try {
                keyFileId = keyFileService.save2OSS(uploadFile2OSSVO.getFileDir(), uploadFile2OSSVO.getKeyFile());
            } catch (IOException e) {
                log.error("获取密钥文件的输入流失败，请检查输入");
                return ResponseEntity.internalServerError().body(null);
            }

            // 记录数据文件的实际位置和逻辑位置到数据库表中
            String dataFileId = dataFileService.recordPosition(uploadFile2OSSVO.getFileDir(),
                    uploadFile2OSSVO.getDataFile().getOriginalFilename(),
                    uploadFile2OSSVO.getFileHashValue(),
                    uploadFile2OSSVO.getMimeType(),
                    StorageType.OSS.getCode(),
                    keyFileId,
                    uploadFile2OSSVO.getUserId());
            // 检索标签转化辅助材料(根据密钥文件id获取对应数据文件id，再根据数据文件id获取标签转化辅助材料)
            AuxiliaryTranslator auxiliaryTranslator = dataFileService.getAuxByKeyFileId(keyFileId);
            // 告知用户已存在重复文件，返回标签转化辅助材料
            log.info("返回的辅助材料:{}", auxiliaryTranslator.toString());
            HashMap result = new HashMap<>();
            result.put("Aux", auxiliaryTranslator);
            result.put("dataFileId", dataFileId);
            return ResponseEntity.status(201).body(result);
        } else {
            // 根据文件路径存储数据文件
            // 记录数据文件的位置到数据库表中
            String keyFileId = null;
            String dataFileId = null;
            try {
                keyFileId = keyFileService.save2OSS(uploadFile2OSSVO.getFileDir(), uploadFile2OSSVO.getKeyFile());
                dataFileId = dataFileService.save2OSS(uploadFile2OSSVO.getFileDir(), uploadFile2OSSVO.getDataFile(),
                        uploadFile2OSSVO.getUserId(), keyFileId, uploadFile2OSSVO.getMimeType(), StorageType.OSS.getCode(),
                        uploadFile2OSSVO.getFileHashValue());
            } catch (IOException e) {
                log.error("获取文件的输入流失败，请检查输入");
                return ResponseEntity.internalServerError().body(null);
            }
            HashMap result = new HashMap<>();
            result.put("dataFileId", dataFileId);
            return ResponseEntity.ok(result);
            // 告知为用户无重复文件
        }
    }

    @ApiOperation(value = "数据文件的存储操作",
            notes = "此接口将数据文件根据接收到的参数去重后再存储到HDFS中",
            httpMethod = "POST")
    @PostMapping(value = "hdfs/{fileName}")
    public ResponseEntity<String> uploadFile2HDFS(@PathVariable("fileName") String fileName,
                                                  UploadFile2HDFSVO uploadFile2HDFSVO) {
        // 判断数据库表中是否含有相同hash值的记录
        boolean flag = dataFileService.existsByHashValue(uploadFile2HDFSVO.getHashValue());
        // 如果hash值相同，则说明可以去重，进行去重存储
        // 如果hash值不同，则说明不可去重，进行首次存储
        if (flag) {
            this.deduplicateStorage(fileName, uploadFile2HDFSVO);
        } else {
            this.directStorage(fileName, uploadFile2HDFSVO);
        }
        log.info("存储成功");
        return ResponseEntity.ok("存储成功");
    }

    /**
     * 直接存储相关文件.
     * @param fileName 文件名
     * @param uploadFile2HDFSVO 上传文件相关实体类
     */
    private void directStorage(String fileName, UploadFile2HDFSVO uploadFile2HDFSVO) {

        /* 存储密钥文件 */
        String keyFileId = keyFileService.save(uploadFile2HDFSVO.getFileDir(), fileName, uploadFile2HDFSVO.getKeyFile());

        /* 存储标签文件 */
        String tagFileId = tagFileService.save(uploadFile2HDFSVO.getFileDir(), fileName, uploadFile2HDFSVO.getTagFile());

        /* 存储数据文件 */
        SaveDTO saveDTO = new SaveDTO()
                .setActualFileDir(uploadFile2HDFSVO.getFileDir())
                .setActualFileName(fileName)
                .setLogicFileDir(uploadFile2HDFSVO.getFileDir())
                .setLogicFileName(fileName)
                .setKeyFileId(keyFileId)
                .setTagFileId(tagFileId)
                .setMimeType(uploadFile2HDFSVO.getMimeType())
                .setHashValue(uploadFile2HDFSVO.getHashValue())
                .setUserId(uploadFile2HDFSVO.getUserId());
        dataFileService.save(saveDTO);
        log.info("存储数据文件、标签文件、密钥文件成功");
    }

    private void deduplicateStorage(String fileName, UploadFile2HDFSVO uploadFile2HDFSVO) {

    }
}
