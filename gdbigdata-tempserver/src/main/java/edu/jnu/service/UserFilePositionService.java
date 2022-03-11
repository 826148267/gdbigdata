package edu.jnu.service;

import edu.jnu.dao.UserFilePositionDao;
import edu.jnu.domain.UserFilePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 获取文件相对位置.
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 21:19
 */
@Service
public class UserFilePositionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFilePositionService.class);

    @Autowired
    private UserFilePositionDao userFilePositionDao;

    /**
     * 无重复记录文件位置.
     * 先查询数据库中是否存在此文件，如果没有就插入，有就不插入
     * @param userFilePosition  用户文件存放在OSS中的位置
     */
    public void putFilePosition(UserFilePosition userFilePosition) {
        // 查询数据库中所有userId的文件位置
        List<UserFilePosition> positions = userFilePositionDao.findAllByUserIdIs(userFilePosition.getUserId());
        // 如果查询出来的集合中，有和属性filePath的值相同的，就不插入，
        String filePath = userFilePosition.getFilePath();
        List<UserFilePosition> positionList = positions.stream()
                .filter(position -> position.getFilePath().equals(filePath)).toList();
        // 否则插入
        if (positionList.size() == 0) {
            userFilePositionDao.save(userFilePosition);
        }
        LOGGER.info("文件已去重");
    }

    /**
     * 获取文件相对路径集合.
     * 根据用户id获取用户存放在OSS中的文件路径.
     * @param userId    用户id
     * @return  该用户所有文件的文件路径集合
     */
    public List<String> listFilePaths(int userId) {
        List<UserFilePosition> ufps = userFilePositionDao.findAllByUserIdIs(userId);
        return ufps.stream().map(UserFilePosition::getFilePath).toList();
    }
}
