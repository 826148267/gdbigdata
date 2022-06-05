package edu.jnu.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import edu.jnu.dto.IntegerityProof;
import edu.jnu.dto.ProofIntegrityDto;
import edu.jnu.service.OSSService;
import edu.jnu.service.SepdpProofService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月28日 10:47
 */
@RestController
public class ProofApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProofApi.class);

    @Autowired
    private OSSService ossService;

    @Autowired
    private SepdpProofService sepdpProofService;

    /**
     *
     * @param proofIntegrityDto
     * @return
     */
    @GetMapping("/sepdp/proofs/")
    public ResponseEntity<IntegerityProof> proofIntegrity(ProofIntegrityDto proofIntegrityDto) {
        // 通过文件路径获取文件
        String dataFullPath = "audit/data/"+proofIntegrityDto.getFilePath();    //  获取文件全路径
        String tagFullPath = "audit/tag/"+proofIntegrityDto.getFilePath();    //  获取文件全路径
        String mStr = null;
        String sStr = null;
        try {
            mStr = ossService.getStringInOssObject(dataFullPath, proofIntegrityDto.getBucketName());
            sStr = ossService.getStringInOssObject(tagFullPath, proofIntegrityDto.getBucketName());
        } catch (IOException e) {
            LOGGER.error("文件读取失败");
        }
        // 读取数据文件，根据下标集合读取mi集合，计算beta
        // 读取标签文件，根据下标集合读取si集合，计算alpha
        ArrayList<BigInteger> mList = sepdpProofService.getMList(mStr, proofIntegrityDto.getiList());
        ArrayList<BigInteger> sList = sepdpProofService.getSList(sStr, proofIntegrityDto.getiList());   //最后一个元素是R
        // 获取R
        if (mList.size() + 1 != sList.size()) {
            LOGGER.error("标签数量与数据块数量关系错误");
            return ResponseEntity.internalServerError().body(null);
        }
        BigInteger R = sList.get(mList.size());
        // 通过beta计算gamma
        BigInteger gamma = sepdpProofService.getGamma(proofIntegrityDto.getvList(), mList);
        BigInteger alpha = sepdpProofService.getAlpha(R, proofIntegrityDto.getvList(), sList);
        // 将gamma和alpha封装返回
        LOGGER.info("成功返回证明,其中:");
        LOGGER.info("alpha:"+String.valueOf(alpha));
        LOGGER.info("gamma:"+String.valueOf(gamma));
        LOGGER.info("R:"+String.valueOf(R));
        return ResponseEntity.ok(new IntegerityProof(alpha, gamma, R));
    }
}
