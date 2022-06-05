package edu.jnu.service;

import edu.jnu.dao.FilePositionDao;
import edu.jnu.entity.FilePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 19:40
 */
@Service
public class FilePositionService {

    @Autowired
    private FilePositionDao filePositionDao;

    /**
     * 记录文件存储在OSS中的位置.
     * @param fileStoragePath
     * @param originalFilename
     * @return
     */
    public boolean recordPosition(String fileStoragePath, String originalFilename) {
        FilePosition fp = new FilePosition(fileStoragePath, originalFilename);
        try {
            filePositionDao.save(fp);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}