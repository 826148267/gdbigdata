package edu.jnu.service;

import edu.jnu.DAO.TagFileDAO;
import edu.jnu.PO.TagFilePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:34
 */
@Service
public class TagFileService {

    @Autowired
    private TagFileDAO tagFileDAO;

    /**
     * 保存标签文件.
     * @param fileDir 文件目录
     * @param fileName  文件名
     * @param tagFile   标签文件
     * @return 返回文件id
     */
    public String save(String fileDir, String fileName, MultipartFile tagFile) {
        // 调用第三方接口存储到hdfs中

        // 文件记录入表
        TagFilePO tagFilePO = new TagFilePO();
        tagFilePO.setFileDir(fileDir);
        tagFilePO.setFileName(fileName);
        return tagFileDAO.save(tagFilePO).getId();
    }
}
