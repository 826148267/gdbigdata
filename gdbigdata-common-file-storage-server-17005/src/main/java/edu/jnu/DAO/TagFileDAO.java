package edu.jnu.DAO;

import edu.jnu.PO.TagFilePO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:36
 */
public interface TagFileDAO extends JpaRepository<TagFilePO, Long> {

}
