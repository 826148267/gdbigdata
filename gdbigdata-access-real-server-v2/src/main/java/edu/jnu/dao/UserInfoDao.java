package edu.jnu.dao;

import edu.jnu.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * table_user_info表操作接口
 * @author Guo zifan
 * @date 2022年01月24日 16:45
 */
public interface UserInfoDao extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUserId(Long userId);
}
