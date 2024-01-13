package edu.jnu.service;

import edu.jnu.config.OSU.OsuProtocolParams;
import edu.jnu.entity.*;
import edu.jnu.service.thirdpart.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

/**
 * @author Guo zifan
 * @date 2022年01月30日 20:12
 */
@Service
@Slf4j
public class UserService {

    @Autowired(required = false)
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OsuService osuService;

    public boolean isExist(String userName) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(userName));
    }

    public void countUserInfoNum() {
        redisTemplate.opsForValue().increment("userInfoRecordNum");
    }

    public void saveUserNameAndId(Optional<String> userName, String userId) {
        if (userName.isPresent()) {
            redisTemplate.opsForValue().set(userName.get(), userId);
            redisTemplate.persist(userName.get());
        }
    }

    public boolean addUser(UserDTO userDto) {
        Optional<String> userName = Optional.ofNullable(userDto.getUserName());
        try {
            if (userName.isPresent()) {
                userDto.encryptAllFields2SPlus2(OsuProtocolParams.PK, OsuProtocolParams.s);
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("用户"+userName + "的信息加密失败，存储失败");
            return false;
        }

        // 将用户信息入库，返回用户id
        Optional<String> recordId = Optional.ofNullable(userInfoService.addUser(userDto));
        // 元素非空判断式
        Predicate<String> sqlOperatorIsSuccess = Objects::nonNull;
        // 如果用户id非空，则向redis中插入数据
        if (recordId.filter(sqlOperatorIsSuccess).isPresent()) {
            this.saveUserNameAndId(userName, recordId.get());
            this.countUserInfoNum();
            return true;
        } else {
            return false;
        }
    }

    @Cacheable("userFileNumsByUserName")
    public String getUserFileNums(String userName) {
        String id = this.getUserIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userFileNums", recordNum);
        return osuService.getOneFieldOfTable(osuParam);
    }

    @Cacheable("userAddressByUserName")
    public String getUserAddress(String userName) {
        String id = this.getUserIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userAddress", recordNum);
        return osuService.getOneFieldOfTable(osuParam);
    }

    @Cacheable("userOrganizationByUserName")
    public String getUserOrganization(String userName) {
        String id = this.getUserIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userOrganization", recordNum);
        return osuService.getOneFieldOfTable(osuParam);
    }

    public UserVO getUser(String userName) {
        String userId = this.getUserIdByUserName(userName);
        CompletableFuture<String> userOrganizationFuture = CompletableFuture.supplyAsync(() -> this.getUserOrganization(userName));
        CompletableFuture<String> userAddressFuture = CompletableFuture.supplyAsync(() -> this.getUserAddress(userName));
        CompletableFuture<String> userFileNumsFuture = CompletableFuture.supplyAsync(() -> this.getUserFileNums(userName));
        CompletableFuture.allOf(userOrganizationFuture, userAddressFuture, userFileNumsFuture);
        return UserVO.builder()
                .userId(userId)
                .userName(userName)
                .userAddress(userAddressFuture.join())
                .userOrganization(userOrganizationFuture.join())
                .userFileNums(userFileNumsFuture.join())
                .build();
    }

    public String getUserIdByUserName(String userName) {
        return Objects.requireNonNull(redisTemplate.opsForValue().get(userName)).toString();
    }

    public String getNowRecordNum() {
        return Objects.requireNonNull(redisTemplate.opsForValue().get("userInfoRecordNum")).toString();
    }

    public String updateUserOrganization(UpdateOrganizationDTO user) {
        String id = this.getUserIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userOrganization", recordNum);
        return osuService.setOneFieldOfTable(osuParam, user.getUserOrganization());
    }

    public String updateUserAddress(UpdateAddressDTO user) {
        String id = this.getUserIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userAddress", recordNum);
        return osuService.setOneFieldOfTable(osuParam, user.getUserAddress());
    }

    public String updateUserFileNums(UpdateFileNumsDTO user) {
        String id = this.getUserIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userFileNums", recordNum);
        return osuService.setOneFieldOfTable(osuParam, user.getUserFileNums());
    }

    public UserDTO updateUserInfo(UserDTO user) {
        UpdateAddressDTO updateAddressDTO = UpdateAddressDTO.builder()
                .userName(user.getUserName())
                .userAddress(user.getUserAddress())
                .build();
        UpdateOrganizationDTO updateOrganizationDTO = UpdateOrganizationDTO.builder()
                .userName(user.getUserName())
                .userOrganization(user.getUserOrganization())
                .build();
        UpdateFileNumsDTO updateFileNumsDTO = UpdateFileNumsDTO.builder()
                .userName(user.getUserName())
                .userFileNums(user.getUserFileNums())
                .build();
//        CompletableFuture<String> userOrganizationFuture = CompletableFuture.supplyAsync(() -> this.updateUserOrganization(updateOrganizationDTO));
//        CompletableFuture<String> userAddressFuture = CompletableFuture.supplyAsync(() -> this.updateUserAddress(updateAddressDTO));
//        CompletableFuture<String> userFileNumsFuture = CompletableFuture.supplyAsync(() -> this.updateUserFileNums(updateFileNumsDTO));
//        CompletableFuture.allOf(userOrganizationFuture, userAddressFuture, userFileNumsFuture);
//        return UserDTO.builder()
//                .userName(user.getUserName())
//                .userAddress(userAddressFuture.join())
//                .userOrganization(userOrganizationFuture.join())
//                .userFileNums(userFileNumsFuture.join())
//                .build();
        return UserDTO.builder()
                .userName(user.getUserName())
                .userAddress(this.updateUserOrganization(updateOrganizationDTO))
                .userOrganization(this.updateUserAddress(updateAddressDTO))
                .userFileNums(this.updateUserFileNums(updateFileNumsDTO))
                .build();
    }
}