package edu.jnu.service;

import edu.jnu.dao.FilePositionDao;
import edu.jnu.entity.FilePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * @param fileStoragePath   文件存储路径
     * @param originalFilename  原始文件名
     * @return  是否上传成功，成功返回true，失败返回false
     */
    public boolean recordPosition(String fileStoragePath, String originalFilename, Integer blockNum) {
        FilePosition fp = new FilePosition(fileStoragePath, originalFilename, blockNum);
        try {
            filePositionDao.save(fp);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 分页查询文件位置
     * @param pageOffset    当前页数
     * @param size  页大小
     * @return  文件路径列表
     */
    public List<FilePosition> getAllFilePosition(Integer pageOffset, Integer size) {
        PageRequest pageRequest = PageRequest.of(pageOffset, size);
        Page<FilePosition> fps = filePositionDao.findAll(pageRequest);
        return fps.stream().filter(element -> {
            element.setDataFilePath(element.getDataFilePath().split("data/")[1]);
            element.setTagFilePath(element.getTagFilePath().split("tag/")[1]);
            return true;
        }).toList();
    }

    /**
     * 统计表中记录数.
     * @return 返回总记录数
     */
    public Integer getTotals() {
        return Math.toIntExact(filePositionDao.count());
    }
}
