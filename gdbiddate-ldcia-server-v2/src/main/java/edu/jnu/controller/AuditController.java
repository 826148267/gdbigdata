package edu.jnu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.model.OSSObject;
import edu.jnu.entity.Challenge;
import edu.jnu.entity.FileListDTO;
import edu.jnu.entity.IntegrityProof;
import edu.jnu.service.FileService;
import edu.jnu.service.OSSService;
import edu.jnu.utils.AuditTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2022年05月05日 21时24分
 * @功能描述: 审计接口
 */
@RestController
public class AuditController {

    @Autowired
    private FileService fileService;

    @Autowired
    private OSSService ossService;

    @GetMapping("/files/{userName}")
    public ResponseEntity<List<FileListDTO>> uploadParams(@PathVariable("userName") String userName) {
        List<FileListDTO> files = fileService.listFiles(userName);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/auditParams/{fileId}")
    public ResponseEntity<String> getAuditParams(@PathVariable("fileId") String fileId) {
        return ResponseEntity.ok(fileService.getAuditParams(fileId));
    }

    /**
     * 获取完整性证明的接口
     * @param fileId 数据文件的逻辑id
     * @param challengesStr 挑战集合的字符串
     * @return 作为完整性证明的集合签名和集合数据块
     */
    @PostMapping("/proof/{fileId}")
    public ResponseEntity<IntegrityProof> proof(@PathVariable("fileId") String fileId, @RequestBody String challengesStr) {
        // 将挑战从请求字符串中提取成List的形式
        List<Challenge> challenges = JSONArray.parseArray(JSONObject.parseObject(challengesStr).getString("challenges"), Challenge.class);
        // 根据挑战提取出下标集合
        ArrayList<Integer> integerList = new ArrayList<>(challenges.stream()
                .map(Challenge::getIndex)
                .toList());

        CompletableFuture<String> dataAggregationFuture = CompletableFuture.supplyAsync(() -> {
            // 根据数据文件的逻辑id检索获得实际文件id，再根据实际文件id获取到数据文件
            OSSObject dataFileObject = ossService.getObj(fileService.getDataFileId(fileId), "gdbigdata");
            // 在输入流中按照行号数据进行数据读取，并按顺序存入List中
            ArrayList<String> dataList = fileService.getListFromPointedLinesArrayInInputStream(dataFileObject.getObjectContent(), integerList);
            // 对数据块进行聚合
            return AuditTool.getDataAggregation(challenges, dataList);
        });
        CompletableFuture<String> signAggregationFuture = CompletableFuture.supplyAsync(() -> {
            // 根据数据文件的逻辑id检索获得签名文件id，再根据签名文件id获取到签名文件
            OSSObject tagFileObject = ossService.getObj(fileService.getTagFileId(fileId), "gdbigdata");
            // 在输入流中按照行号数据进行数据读取，并按顺序存入List中
            ArrayList<String> signList = fileService.getListFromPointedLinesArrayInInputStream(tagFileObject.getObjectContent(), integerList);
            // 将签名进行聚合
            return AuditTool.getSignAggregation(challenges, signList);
        });
        CompletableFuture.allOf(dataAggregationFuture, signAggregationFuture);

        // 创建一个完整性证明的返回结果
        IntegrityProof proof = IntegrityProof.builder().signAggregation(signAggregationFuture.join()).dataAggregation(dataAggregationFuture.join()).build();
        return ResponseEntity.ok(proof);
    }
}
