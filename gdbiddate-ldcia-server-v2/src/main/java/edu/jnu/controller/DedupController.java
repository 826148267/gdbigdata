package edu.jnu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.jnu.entity.Challenge;
import edu.jnu.entity.TransParams;
import edu.jnu.entity.dto.*;
import edu.jnu.service.FileService;
import edu.jnu.utils.AuditTool;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2022年05月05日 21时24分
 * @功能描述: 去重接口
 */
@RestController
public class DedupController {

    @Autowired
    private FileService fileService;


    /**
     * 上传数据文件
     * @return ResponseEntity<TransParams>
     */
    @PostMapping("/uploadDataFile")
    public ResponseEntity<TransParams> uploadDataFile(@RequestBody UploadDataFileDTO uploadDataFileDTO) {
        String fileAbstract = AuditTool.getFileAbstract(uploadDataFileDTO.getData().replace("\n", ""));
        // 存储文件之前，先判断是否有相同文件
        if (fileService.existFile(fileAbstract, uploadDataFileDTO.getMimeType())) {
            if (fileService.isSaveDupBySelf(fileAbstract, uploadDataFileDTO.getMimeType(), uploadDataFileDTO.getUserName(), uploadDataFileDTO.getFileName())) {
                return ResponseEntity.status(200).body(null);
            }
            TransParams transParams = fileService.addDataFileInfo(uploadDataFileDTO.getUserName(),
                                            uploadDataFileDTO.getFileName(),
                                            uploadDataFileDTO.getMimeType(),
                                            fileAbstract,
                                            uploadDataFileDTO.getDedupKeyCipher());
            return ResponseEntity.status(201).body(transParams);
        }
        // 如果没有相同文件， 则将怎见存储到对象存储中
        String fileId = fileService.saveDataFile(uploadDataFileDTO.getData());
        fileService.saveDataFileInfo(fileId,
                uploadDataFileDTO.getUserName(),
                uploadDataFileDTO.getFileName(),
                uploadDataFileDTO.getMimeType(),
                fileAbstract,
                uploadDataFileDTO.getDedupKeyCipher());
        return ResponseEntity.status(202).body(null);
    }

    @PostMapping("/uploadFileInfo")
    public ResponseEntity<TransParams> uploadFileInfo(@RequestBody UploadFileInfoDTO  uploadFileInfoDTO) {
        // 存储文件之前，先判断是否有相同文件
        TransParams transParams = fileService.addDataFileInfo(uploadFileInfoDTO.getUserName(),
                uploadFileInfoDTO.getFileName(),
                uploadFileInfoDTO.getMimeType(),
                uploadFileInfoDTO.getFileAbstract(),
                uploadFileInfoDTO.getDedupKeyCipher());
        return ResponseEntity.ok(transParams);
    }



    @PostMapping("/uploadSignFile/noDedup")
    public ResponseEntity<String> uploadSignFileAndParams(@RequestBody UploadSignFileAndParamsDTO uploadSignFileAndParamsDTO) {
        // 保存签名文件
        String signFileId = fileService.saveSignFile(uploadSignFileAndParamsDTO.getSigns());
        // 保存审计参数文件
        String auditParamsFileId = fileService.saveAuditParamsFile(uploadSignFileAndParamsDTO.getAuditParams());
        // 保存转移参数文件
        String transParamsFileId = fileService.saveTransParamsFile(uploadSignFileAndParamsDTO.getTransParams());
        // 通过用户名和文件名定位数据文件的存储记录，将标签文件、审计参数文件、所有权转移参数文件的id都写入该记录中
        fileService.perfectFileInfo(uploadSignFileAndParamsDTO.getUserName(), uploadSignFileAndParamsDTO.getFileName(), signFileId, transParamsFileId, auditParamsFileId);
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping("/uploadSignFile/canDedup")
    public ResponseEntity<?> uploadParams(@RequestBody UploadParamsDTO uploadParamsDTO) {
        // 保存转移参数
        String transParamsFileId = fileService.saveTransParamsFile(uploadParamsDTO.getTransParams());
        // 保存审计参数
        String auditParamsFileId = fileService.saveAuditParamsFile(uploadParamsDTO.getAuditParams());
        // 获取第一个上传该文件的用户的签名文件id
        String firstSaverSignFileId = fileService.getSignFileIdByUserNameAndFileName(uploadParamsDTO.getUserName(), uploadParamsDTO.getFileName());
        // 利用上一步获取的签名文件Id，转化参数对产生新用户的签名，并保存，返回新用户签名文件的id
        String ownerSignFileId = fileService.transSign(firstSaverSignFileId, JSONObject.parseObject(uploadParamsDTO.getTransParams(), TransParams.class));
        // 完善用户的文件保存信息
        fileService.perfectFileInfo(uploadParamsDTO.getUserName(), uploadParamsDTO.getFileName(), ownerSignFileId, transParamsFileId, auditParamsFileId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<DownloadFileDTO> downloadFile(@PathVariable String fileId) {
        String data = fileService.getFileAsString(fileId);
        String dedupKeyCipher = fileService.getDedupKeyCipherByFileId(fileId);
        DownloadFileDTO downloadFileDTO = DownloadFileDTO.builder()
                .data(data).dedupKeyCipher(dedupKeyCipher).build();
        return ResponseEntity.ok(downloadFileDTO);
    }

    @GetMapping("/pow/challenges/{fileAbstract}")
    public ResponseEntity<String> powChallenge(@PathVariable String fileAbstract) {
        fileAbstract = new String(Base64.getDecoder().decode(fileAbstract));
        Integer lines = fileService.getFileLines(fileAbstract);
        List<Challenge> challenges = AuditTool.yieldChallenges(460, lines);
        String jsonString = JSONArray.toJSONString(challenges);
        return ResponseEntity.ok(jsonString);
    }

    @PostMapping("/pow/proof")
    public ResponseEntity<String> powProof(@RequestBody PowProofDTO powProofDTO) {
        Element proofElement = AuditTool.str2ZpElement(powProofDTO.getProof());
        List<Challenge> challenges = JSONArray.parseArray(powProofDTO.getChallenges(), Challenge.class);
        boolean flag = fileService.checkPoWProof(proofElement, challenges, powProofDTO.getFileAbstract());
        if (flag) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable String fileId) {
        if (fileService.deleteFile(fileId) == 1) {
            return ResponseEntity.ok().body(true);
        }
        return ResponseEntity.internalServerError().build();
    }
}
