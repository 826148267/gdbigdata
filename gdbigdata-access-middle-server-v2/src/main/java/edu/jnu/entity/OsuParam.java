package edu.jnu.entity;

import edu.jnu.config.OSU.OsuProtocolParams;
import edu.jnu.utils.Action;
import edu.jnu.utils.Tools;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年02月16日 20:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OsuParam {
    private String userId;
    private String targetColumn;
    private String tableRecordNum;

    public GetUserInfoPhase1DTO createDto1ForSingleField(RedisTemplate redisTemplate) {
        // 获取当前表中的记录数
        int recordNum = Integer.parseInt(tableRecordNum);

        // 获取随机访问下标集合的大小
        int randomSequenceSize = OsuProtocolParams.randomSequenceSize;
        while (recordNum < randomSequenceSize) {
            randomSequenceSize /= 2;
        }
        if (randomSequenceSize == 0) {
            return null;
        }

        // 获取长度为randomSequenceSize的随机选择的userId的List
        ArrayList<String> userIdSet = new ArrayList<>();
        for (int i = 1; i < randomSequenceSize; i++) {
            String randomKey;
            do {
                randomKey = (String) redisTemplate.randomKey();
            } while ("userInfoRecordNum".equals(randomKey));

            String userId = null;
            if (randomKey != null) {
                userId = (String) redisTemplate.opsForValue().get(randomKey);
            }
            if (!userIdSet.contains(userId) && !userId.equals(this.userId)) {
                userIdSet.add(userId);
            } else {
                -- i;
            }
        }
        // 随机选择一个位置，将目标位置的userId置为目标userId
        int targetIndex = Tools.getRandom(randomSequenceSize, 0);
        userIdSet.add(targetIndex, userId);

        CipherText[] taos = Action.createTaoVector(OsuProtocolParams.PK,
                OsuProtocolParams.s, randomSequenceSize, targetIndex);

        // 将数组taos转换为ArrayList<CipherText>
        ArrayList<CipherText> taoVector = new ArrayList<>(Arrays.asList(taos));

        return new GetUserInfoPhase1DTO(taoVector, targetColumn, userIdSet);
    }

    public GetUserInfoPhase2DTO createDto2(GetUserInfoPhase1DTO dto1, CipherText v, String freshValue) {
        CipherText delta = Action.createDelta(OsuProtocolParams.PK, OsuProtocolParams.s, v, freshValue);
        return new GetUserInfoPhase2DTO(dto1, delta);
    }

    public boolean validParams() {
        if (this.userId == null) {
            return false;
        }
        if (this.tableRecordNum == null) {
            return false;
        }
        return this.targetColumn != null;
    }
}
