package edu.jnu.DAO;

import edu.jnu.PO.AuditInfoPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月23日 22:37
 */
public interface AuditInfoDAO extends JpaRepository<AuditInfoPO, Long> {
    /**
     * 通过数据文件的文件id获取一条记录
     * @param dataFileId    数据文件的文件id
     * @return  对应数据文件id的一条记录
     */
    AuditInfoPO findOneByDataFileId(String dataFileId);
}
