package edu.jnu.controller;

import edu.jnu.entity.*;
import edu.jnu.service.UserInfoService;
import edu.jnu.utils.SnowflakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Guo zifan
 * @date 2022年01月24日 19:02
 */
@RestController
@RequestMapping("/rs")
public class UserInfoApi {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping(value = "/users")
    public ResponseEntity<String> createNewUserInformation(@Validated @RequestBody AddUserVO addUserVO) {
        SnowflakeIdUtils snowFlake = new SnowflakeIdUtils(1, 1);
        UserInfo userInfo = UserInfo.builder()
                .userId(snowFlake.nextId())
                .userName(addUserVO.getUserName())
                .userFileNums(addUserVO.getUserFileNums())
                .userOrganization(addUserVO.getUserOrganization())
                .userAddress(addUserVO.getUserAddress())
                .build();
        String resultId = userInfoService.createUser(userInfo);
        return ResponseEntity.ok(resultId);
    }

    /*
    osu协议第一阶段操作
     */
    @PostMapping(value = "/osu-operation-first-phase")
    public ResponseEntity<?> getR(@Validated @RequestBody OsuFirstPhaseVO osuFirstPhaseVO) {
        ArrayList<Long> accessSet = osuFirstPhaseVO.getAccessSet();
        ArrayList<CipherText> taos = osuFirstPhaseVO.getTaoVector();
        String targetColumn = osuFirstPhaseVO.getTargetColumn();
        CipherText R = userInfoService.selectionPhase(accessSet, taos, targetColumn);
        if (R != null) {
            return ResponseEntity.ok(R);
        }
        return ResponseEntity.badRequest().build();
    }

    /*
    osu协议第二阶段操作
     */
    @PostMapping(value = "/osu-operation-second-phase")
    public ResponseEntity<?> updateTargetCipherTextSet(@Validated @RequestBody OsuSecondPhaseVO osuSecondPhaseVO) {
        ArrayList<Long> accessSet = osuSecondPhaseVO.getOsuFirstPhaseVO().getAccessSet();
        ArrayList<CipherText> taoVector = osuSecondPhaseVO.getOsuFirstPhaseVO().getTaoVector();
        String targetColumn = osuSecondPhaseVO.getOsuFirstPhaseVO().getTargetColumn();
        CipherText delta = osuSecondPhaseVO.getDelta();
        HashMap<Long, CipherText> indexAndTaoMap = new HashMap<>();
        for (int i = 0; i < accessSet.size(); i++) {
            indexAndTaoMap.put(accessSet.get(i), taoVector.get(i));
        }
        userInfoService.updatePhase(indexAndTaoMap, targetColumn, delta);
        return ResponseEntity.ok(null);
    }

}
