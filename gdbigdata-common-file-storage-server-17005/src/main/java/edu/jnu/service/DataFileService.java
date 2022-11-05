package edu.jnu.service;

import edu.jnu.DAO.AuditInfoDAO;
import edu.jnu.DAO.DataFileDAO;
import edu.jnu.DTO.DataFileService.SaveDTO;
import edu.jnu.PO.AuditInfoPO;
import edu.jnu.PO.DataFilePO;
import edu.jnu.PO.KeyFilePO;
import edu.jnu.POJO.AuxiliaryTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:22
 */
@Service
public class DataFileService {

    @Autowired
    private DataFileDAO dataFileDAO;

    @Autowired
    private AuditInfoDAO auditInfoDAO;

    @Autowired
    private OSSService ossService;

    public boolean existsByHashValue(String hashValue) {
        return dataFileDAO.existsByHashValue(hashValue);
    }

    public String save(SaveDTO saveDTO) {
        // 调用第三方接口存储到hdfs中
        

        // 文件记录入表
        DataFilePO dataFilePO = new DataFilePO()
                .setActualFileDir(saveDTO.getActualFileDir())
                .setActualFileName(saveDTO.getActualFileName())
                .setLogicFileDir(saveDTO.getLogicFileDir())
                .setLogicFileName(saveDTO.getLogicFileName())
                .setKeyFileId(saveDTO.getKeyFileId())
                .setTagFileId(saveDTO.getTagFileId())
                .setHashValue(saveDTO.getHashValue())
                .setMimeType(saveDTO.getMimeType())
                .setUserId(saveDTO.getUserId());
        return dataFileDAO.save(dataFilePO).getId();
    }

    /**
     * 记录文件位置.
     * <pre>
     * 具体实现：
     *    1、根据hashValue查出一条重复文件的位置记录，获取实际存储目录，实际存储文件名；
     *    2、将实际存储目录、实际存储文件名、逻辑存储目录、逻辑存储文件名存入表中.
     * </pre>
     * @param fileDir 逻辑文件目录
     * @param filename  逻辑文件名
     * @param hashValue 文件hash值
     * @param mimeType 文件格式
     * @param storageTypeCode 存储介质代码
     * @param keyFileId 密钥文件id
     * @param userId 用户id
     * @return 数据文件的文件id
     */
    public String recordPosition(String fileDir, String filename, String hashValue,
                               String mimeType, Integer storageTypeCode,
                               String keyFileId, String userId) {
        DataFilePO result = dataFileDAO.findOneByHashValue(hashValue);

        DataFilePO newRecord = new DataFilePO()
                .setLogicFileDir(result.getActualFileDir())
                .setLogicFileName(result.getActualFileName())
                .setActualFileDir(fileDir)
                .setActualFileName(filename)
                .setMimeType(mimeType)
                .setStorageType(storageTypeCode)
                .setHashValue(hashValue)
                .setKeyFileId(keyFileId)
                .setUserId(userId);

        dataFileDAO.save(newRecord);
        return newRecord.getId();
    }


    /**
     * 根据密钥文件的文件id获取标签转化辅助材料.
     * <pre>
     * 具体做法：
     *      1、根据密钥文件的文件id获取数据文件的文件id
     *      2、根据数据文件的文件id获取标签转化辅助参数
     * </pre>
     * @param keyFileId 密钥文件的文件id
     * @return  返回封装后的标签转化辅助参数的对象
     */
    public AuxiliaryTranslator getAuxByKeyFileId(String keyFileId) {
        String dataFileId = dataFileDAO.findDataFileIdByKeyFileId(keyFileId);
        AuditInfoPO auditInfoPO = auditInfoDAO.findOneByDataFileId(dataFileId);
        return new AuxiliaryTranslator()
                .setAux(auditInfoPO.getAux())
                .setV(auditInfoPO.getV())
                .setW(auditInfoPO.getW());
    }

    public String save2OSS(String fileDir,
                           MultipartFile dataFile,
                           String userId,
                           String keyFileId,
                           String mimeType,
                           Integer storageTypeCode,
                           String hashValue) throws IOException {
        // 获取文件名
        String fileName = dataFile.getOriginalFilename();
        // 存储到OSS
        ossService.uploadObjAsPlainTxt2OSS(fileDir+">>"+fileName, "gdbigdata", dataFile.getInputStream());
        // 将文件记录到表中
        assert fileName != null;
        DataFilePO newRecord = new DataFilePO()
                .setUserId(userId)
                .setKeyFileId(keyFileId)
                .setMimeType(mimeType)
                .setStorageType(storageTypeCode)
                .setHashValue(hashValue)
                .setActualFileDir(fileDir)
                .setActualFileName(fileName)
                .setLogicFileDir(fileDir)
                .setLogicFileName(fileName);
        dataFileDAO.save(newRecord);
        return newRecord.getId();
    }
}
