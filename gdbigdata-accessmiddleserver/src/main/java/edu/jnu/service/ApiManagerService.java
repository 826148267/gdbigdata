package edu.jnu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.jnu.domain.CipherText;
import edu.jnu.dto.GetUserInfoPhase1Dto;
import edu.jnu.dto.GetUserInfoPhase2Dto;
import edu.jnu.dto.UserDto;
import edu.jnu.exception.ConditionException;
import edu.jnu.response.service.SrvResEnum;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年02月15日 13:27
 */
@Service
public class ApiManagerService {

    @Value("${real.server.url.add.userinfo}")
    private String addUserUrl;

    public CipherText osuGetR(GetUserInfoPhase1Dto dto, String url) {
        JSONObject result = this.simplePost(dto, url);

        if (!"200".equals(result.get("code").toString())) {
            throw new ConditionException(SrvResEnum.EXCEPTION);
        }

        JSONObject rObj = result.getJSONObject("data");
        JSONArray sJArr = rObj.getJSONArray("s");
        String sJAStr = JSONObject.toJSONString(sJArr);
        ArrayList<Integer> S = (ArrayList<Integer>) JSONObject.parseArray(sJAStr, Integer.class);
        BigInteger cp = rObj.getObject("cp", BigInteger.class);
        int l = (int) rObj.get("l");
        return new CipherText(cp, l, S);
    }

    public void osuUpdate(GetUserInfoPhase2Dto dto, String url) {
        JSONObject result = this.simplePost(dto, url);

        if (!"200".equals(result.get("code").toString())) {
            throw new ConditionException(SrvResEnum.EXCEPTION);
        }
    }

    public boolean createUser(UserDto userDto) {
        JSONObject result = this.simplePost(userDto, addUserUrl);
        int code = Integer.parseInt(result.get("code").toString());
        return code == 200;
    }

    private JSONObject simplePost(Object obj, String url) {
        String userDtoStr = JSONObject.toJSONString(obj, true);
        String resStr;
        try {
            resStr = EntityUtils.toString(
                    Request.Post(url)
                            .bodyString(userDtoStr, ContentType.APPLICATION_JSON)
                            .execute().returnResponse().getEntity(),
                    "UTF-8"
            );
        } catch (IOException e) {
            throw new ConditionException(SrvResEnum.EXCEPTION);
        }
        return JSON.parseObject(resStr);
    }
}
