package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年02月17日 16:28
 */
public class PatchUserInfoDto<T> {
    @JSONField(name = "op")
    @NotBlank(message = "操作不得为空或空串，可以为update、get、remove")
    private String op;
    @JSONField(name = "path")
    @NotBlank(message = "资源的属性值不得为空或空串，格式如/attribute")
    private String path;
    @JSONField(name = "value")
    @NotNull(message = "更新值不得为空")
    private T value;

    public PatchUserInfoDto() {
    }

    public PatchUserInfoDto(String op, String path, T value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
