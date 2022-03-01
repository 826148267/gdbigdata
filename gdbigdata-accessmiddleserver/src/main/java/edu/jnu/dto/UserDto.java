package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;
import edu.jnu.valid.NotContainSpace;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年02月11日 16:10
 */
public class UserDto {
    @JSONField(name = "username")
    @NotBlank(message = "用户名不得为空")
    @NotContainSpace(message = "用户名不可以含有空格")
    @Length(min = 1, max = 25, message = "用户名的字符串长度为1~25")
    private String userName;
    @JSONField(name = "userAddress")
    private String userAddress = "无固定住址";
    @JSONField(name = "userOrganization")
    private String userOrganization = "无组织";
    @JSONField(name = "userFileNums")
    private String userFileNums = "0";

    public UserDto() {
    }

    public UserDto(String userName, String userAddress, String userOrganization, String userFileNums) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userOrganization = userOrganization;
        this.userFileNums = userFileNums;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserOrganization() {
        return userOrganization;
    }

    public void setUserOrganization(String userOrganization) {
        this.userOrganization = userOrganization;
    }

    public String getUserFileNums() {
        return userFileNums;
    }

    public void setUserFileNums(String userFileNums) {
        this.userFileNums = userFileNums;
    }
}
