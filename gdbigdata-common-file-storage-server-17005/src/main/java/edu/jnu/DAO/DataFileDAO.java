package edu.jnu.DAO;

import edu.jnu.PO.AuditInfoPO;
import edu.jnu.PO.DataFilePO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:20
 */
public interface DataFileDAO extends JpaRepository<DataFilePO, Long> {

    /**
     * 判断表中记录是否含有hash值与hashValue一样的记录.
     * @param hashValue 待存储文件的hash值
     * @return 存在返回true,不存在返回false
     */
    boolean existsByHashValue(String hashValue);

    /**
     * 查找一条与目标hash值相同的记录.
     * @param hashValue 目标hash值
     * @return  返回fileHashValue字段的值与hash值相同是记录
     */
    DataFilePO findOneByHashValue(String hashValue);

    /**
     * 根据密钥文件的文件id获取数据文件的文件id
     * @param keyFileId 密钥文件的文件id
     * @return  数据文件的文件id
     */
    String findDataFileIdByKeyFileId(String keyFileId);
}
