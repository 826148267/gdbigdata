package edu.jnu.service;

import edu.jnu.DAO.KeyFileDAO;
import edu.jnu.PO.KeyFilePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:10
 */
@Service
public class KeyFileService {

    @Autowired
    private KeyFileDAO keyFileDAO;

    @Autowired
    private OSSService ossService;

    /**
     * 保存密钥文件.
     * @param fileDir 文件目录
     * @param fileName  文件名
     * @param keyFile   密钥文件
     * @return 返回文件id
     */
    public String save(String fileDir, String fileName, MultipartFile keyFile) {
        // 调用第三方接口存储到hdfs中


        // 文件记录入表
        KeyFilePO keyFilePO = new KeyFilePO();
        keyFilePO.setFileDir(fileDir);
        keyFilePO.setFileName(fileName);
        return keyFileDAO.save(keyFilePO).getId();
    }

    /**
     * 保存密钥文件到OSS中.
     * <pre>
     * 具体做法：
     *      1、根据文件目录，文件名将密钥文件存储到OSS中
     *      2、将文件信息记录到数据库表中
     * </pre>
     * @param fileDir   文件将要存储到的目录
     * @param keyFile   密钥文件（MultipartFile类型）
     * @return 返回被存储的密钥文件的文件id
     */
    public String save2OSS(String fileDir, MultipartFile keyFile) throws IOException {
        // 获取文件名
        String fileName = keyFile.getOriginalFilename();
        // 存储到OSS
        ossService.uploadObjAsPlainTxt2OSS(fileDir+">>"+fileName, "gdbigdata", keyFile.getInputStream());
        // 将文件记录到表中
        assert fileName != null;
        KeyFilePO newRecord = new KeyFilePO()
                .setFileDir(fileDir)
                .setFileName(fileName);
        keyFileDAO.save(newRecord);
        return newRecord.getId();
    }
}
