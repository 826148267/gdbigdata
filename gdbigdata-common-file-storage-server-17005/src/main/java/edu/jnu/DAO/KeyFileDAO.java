package edu.jnu.DAO;

import edu.jnu.PO.KeyFilePO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 对应表：tb_key_file
 * @author Guo zifan
 * @version 1.0
 * @date 2022年09月02日 15:11
 */
public interface KeyFileDAO extends JpaRepository<KeyFilePO, Long> {
}
