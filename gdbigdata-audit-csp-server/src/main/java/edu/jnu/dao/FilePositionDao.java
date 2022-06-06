package edu.jnu.dao;

import edu.jnu.entity.FilePosition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 19:58
 */
public interface FilePositionDao extends JpaRepository<FilePosition, Integer> {
}
