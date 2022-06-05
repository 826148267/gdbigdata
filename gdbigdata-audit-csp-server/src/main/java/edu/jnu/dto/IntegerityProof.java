package edu.jnu.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigInteger;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 21:02
 */
public class IntegerityProof {
    @JSONField(name = "alpha")
    private BigInteger alpha;
    @JSONField(name = "gamma")
    private BigInteger gamma;
    @JSONField(name = "R")
    private BigInteger R;

    public IntegerityProof() {
    }

    public IntegerityProof(BigInteger alpha, BigInteger gamma, BigInteger R) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.R = R;
    }

    public BigInteger getAlpha() {
        return alpha;
    }

    public void setAlpha(BigInteger alpha) {
        this.alpha = alpha;
    }

    public BigInteger getGamma() {
        return gamma;
    }

    public void setGamma(BigInteger gamma) {
        this.gamma = gamma;
    }

    public BigInteger getR() {
        return R;
    }

    public void setR(BigInteger r) {
        R = r;
    }
}
