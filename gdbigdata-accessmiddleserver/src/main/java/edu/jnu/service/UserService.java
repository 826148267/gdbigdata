package edu.jnu.service;

import edu.jnu.config.OsuProtocolParams;
import edu.jnu.domain.CipherText;
import edu.jnu.domain.OsuParam;
import edu.jnu.domain.User;
import edu.jnu.dto.UserDto;
import edu.jnu.exception.ConditionException;
import edu.jnu.response.service.SrvResEnum;
import edu.jnu.utils.Action;
import edu.jnu.utils.JsonResponse;
import edu.jnu.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Guo zifan
 * @date 2022年01月30日 20:12
 */
@Service
public class UserService {

    @Value("${real.server.url.osu.update}")
    private String osuUpdateUrl;

    @Value("${real.server.url.osu.getR}")
    private String getRUrl;

    @Autowired
    private ApiManagerService apiManagerService;

    @Autowired
    private OsuService osuService;

    @Autowired
    private RedisTemplate redisTemplate;

    /*
    创建新用户
     */
    public boolean createNewUser(User user) {
        User userEnc;
        try {
            userEnc = encryptAllFields(user);
        } catch (Exception e) {
            throw new ConditionException(SrvResEnum.CREATE_USER_FAIL_BAD_ENCRYPT);
        }

        UserDto userDto = new UserDto(
                userEnc.getUserName(),
                userEnc.getUserAddress(),
                userEnc.getUserSchool(),
                userEnc.getUserFileNums()
        );

        // 将user实例以POST的形式发送给到real server
        boolean flag = apiManagerService.createUser(userDto);
        if (flag) {
            this.countUserInfoNum();
            this.saveUserNameAndId(user.getUserName(), this.getNowRecordNum());
            return true;
        }
        return false;
    }

    /**
     * 对User实例进行加密.
     * 加密方式为利用OSU的公钥和baseLayer先加密到s+1层，后加密到s+2层.
     * @param user  需要对字段加密的User实例
     * @return  返回对应的user加密后的User实例
     */
    private User encryptAllFields(User user) {
        User userEnc = new User(Tools.encode(user.getUserName()),
                Tools.encode(user.getUserAddress()),
                Tools.encode(user.getUserSchool()),
                Tools.encode(user.getUserFileNums()));
        // 解析出接受到的实例
        // 对userDto实例中的每个字段都加密
        try {
            CipherText cUserName = Action.Enc2SPlus2(OsuProtocolParams.PK,
                    OsuProtocolParams.s,
                    userEnc.getUserName());
            userEnc.setUserName(cUserName.getCp().toString());

            CipherText cUserAddress = Action.Enc2SPlus2(OsuProtocolParams.PK,
                    OsuProtocolParams.s,
                    userEnc.getUserAddress());
            userEnc.setUserAddress(cUserAddress.getCp().toString());

            CipherText cUserSchool = Action.Enc2SPlus2(OsuProtocolParams.PK,
                    OsuProtocolParams.s,
                    userEnc.getUserSchool());
            userEnc.setUserSchool(cUserSchool.getCp().toString());

            CipherText cUserFileNums = Action.Enc2SPlus2(OsuProtocolParams.PK,
                    OsuProtocolParams.s,
                    userEnc.getUserFileNums());
            userEnc.setUserFileNums(cUserFileNums.getCp().toString());
        } catch (Exception e) {
            throw new ConditionException(SrvResEnum.ENC_USERINFO_FAIL);
        }
        return userEnc;
    }

    public String getUserFileNums(String userName) {
        int id = this.getIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userFileNums", recordNum, getRUrl, osuUpdateUrl);
        return osuService.getOneFieldOfTable(osuParam);
    }

    public String getUserAddress(String userName) {
        int id = this.getIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userAddress", recordNum, getRUrl, osuUpdateUrl);
        return osuService.getOneFieldOfTable(osuParam);
    }

    public String getUserSchool(String userName) {
        int id = this.getIdByUserName(userName);
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userSchool", recordNum, getRUrl, osuUpdateUrl);
        return osuService.getOneFieldOfTable(osuParam);
    }

    public User getUser(String userName) {
        int id = this.getIdByUserName(userName);
        String userSchool = getUserSchool(userName);
        String userAddress = getUserAddress(userName);
        String userFileNums = getUserFileNums(userName);
        return new User(String.valueOf(id), userName, userAddress, userSchool, userFileNums);
    }

    public String getNowRecordNum() {
        return Objects.requireNonNull(redisTemplate.opsForValue().get("userInfoRecordNum")).toString();
    }

    public boolean isExist(String userName) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(userName));
    }

    public void countUserInfoNum() {
        redisTemplate.opsForValue().increment("userInfoRecordNum");
    }

    public void saveUserNameAndId(String userName, String nowRecordNum) {
        redisTemplate.opsForValue().set(userName, nowRecordNum);
        redisTemplate.persist(userName);
    }

    public int getIdByUserName(String userName) {
        return Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(userName)).toString());
    }

    public String updateUserSchool(User user) {
        int id = this.getIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userSchool", recordNum, getRUrl, osuUpdateUrl);
        return osuService.setOneFieldOfTable(osuParam, user.getUserSchool());
    }

    public String updateUserAddress(User user) {
        int id = this.getIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userAddress", recordNum, getRUrl, osuUpdateUrl);
        return osuService.setOneFieldOfTable(osuParam, user.getUserAddress());
    }

    public String updateUserFileNums(User user) {
        int id = this.getIdByUserName(user.getUserName());
        String recordNum = this.getNowRecordNum();
        OsuParam osuParam = new OsuParam(id, "userFileNums", recordNum, getRUrl, osuUpdateUrl);
        return osuService.setOneFieldOfTable(osuParam, user.getUserFileNums());
    }

    public User updateUserInfo(User user) {
        return new User(
                String.valueOf(this.getIdByUserName(user.getUserName())),
                user.getUserName(),
                this.updateUserAddress(user),
                this.updateUserSchool(user),
                this.updateUserFileNums(user)
        );
    }
}