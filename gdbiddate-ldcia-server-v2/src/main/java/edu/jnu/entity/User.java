package edu.jnu.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.jnu.utils.AuditTool;
import it.unisa.dia.gas.jpbc.Element;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月01日 21时52分
 * @功能描述: 用户信息（用于存储用户在软件使用期间的信息）
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private static User instance = null;

    private String userName;
    private String userAddress;
    private String userOrganization;
    private String userFileNums;
    // 私钥
    private Element sk;
    // 公钥
    private Element pk;

    public Element getR(String fa) {
        return AuditTool.hashTwo(this.sk.toString()+fa);
    }

    public Element getH(Element r) {
        return AuditTool.g.duplicate().powZn(r);
    }

    public Element getH1(Element h) {
        return h.duplicate().powZn(sk);
    }

    /**
     * 签名函数
     * @param r r=H2(α||F)
     * @param h h=g^r
     * @param fileAbstract 文件摘要
     * @param index 数据块的下标
     * @param mBytes byte[]形式的数据块的内容
     * @return 签名结果 σ= H1(F||i)^r * (u^H2(m))^(1/(α+H2(F||h)))
     */
    public Element sign(Element r, Element h, String fileAbstract, String index, byte[] mBytes) {
        // H1(F||i)^r
        Element left = AuditTool.hashOne(fileAbstract+index).powZn(r);
        // u^(H2(m))
        Element low = AuditTool.u.duplicate().powZn(AuditTool.hashTwo(mBytes));
        // 1/(α+H2(F||h))
        Element high = AuditTool.getZpOne().div(sk.duplicate().add(AuditTool.hashTwo(getFaConcatH(fileAbstract, h))));
        return left.mul(low.powZn(high));
    }

    /**
     * 签名函数
     * @param r r=H2(α||F)
     * @param h h=g^r
     * @param fileAbstract 文件摘要
     * @param index 数据块的下标
     * @param mBytes byte[]形式的数据块的内容
     * @return 签名结果 σ= H1(F||i)^r * (u^H2(m))^(1/(α+H2(F||h)))
     */
    public Element sign(Element r, Element h, String fileAbstract, Integer index, byte[] mBytes) {
        return sign(r, h, fileAbstract, index.toString(), mBytes);
    }

    public boolean verify(Element pk, Element sign, Element data, String fileAbstract, Element h, Element h1, List<Challenge> challenges) {
        String fileAbstractAndH = getFaConcatH(fileAbstract, h);
        Element value = getMulti(fileAbstract, challenges);
        Element left = AuditTool.BP.pairing(sign, pk.duplicate().mul(AuditTool.g.duplicate().powZn(AuditTool.hashTwo(fileAbstractAndH))));
        Element right1 = AuditTool.BP.pairing(value, h1.mul(h.powZn(AuditTool.hashTwo(fileAbstractAndH))));
        Element right2 = AuditTool.BP.pairing(AuditTool.u.duplicate().powZn(data), AuditTool.g);
        Element right = right1.mul(right2);
        return left.equals(right);
    }
    public boolean verify(AuditParams auditParams, IntegrityProof proof, List<Challenge> challenges) {
        return this.verify(AuditTool.str2G1Element(proof.getSignAggregation()), AuditTool.str2ZpElement(proof.getDataAggregation()), auditParams, challenges);
    }
    public boolean verify(Element sign, Element data, AuditParams auditParams, List<Challenge> challenges) {
        return this.verify(AuditTool.g.duplicate().powZn(this.sk), sign, data, auditParams.getFa(), AuditTool.str2G1Element(auditParams.getH()), AuditTool.str2G1Element(auditParams.getH1()), challenges);
    }

    private Element getMulti(String fileAbstract, List<Challenge> challenges) {
        return challenges.stream()
                .map(challenge -> AuditTool.hashOne(fileAbstract+ challenge.getIndex()).powZn(AuditTool.hashTwo(String.valueOf(challenge.getRandom()))))
                .reduce(AuditTool.getG1One(), Element::mul);
    }


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userOrganization='" + userOrganization + '\'' +
                ", userFileNums='" + userFileNums + '\'' +
                ", sk=" + AuditTool.g1Element2Str(sk) +
                ", pk=" + AuditTool.zpElement2Str(pk) +
                '}';
    }


    /**
     * 根据文件数据list加载需要上传的参数
     * 需要上传的参数有：<br/>
     * 用户名、文件名、审计此数据的参数auditParams、根据旧标签所有权转移参数产生的transParams
     * @param dataList 文件内容
     * @param oldTransParams 旧标签所有权转移参数
     * @param fileName 文件名
     * @return 返回加载后的参数，包括用户名、文件名、审计此数据的参数auditParams、根据旧标签所有权转移参数产生的transParams
     */
    public HashMap<String, String> loadParams(List<String> dataList, TransParams oldTransParams, String fileName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", this.getUserName());
        params.put("fileName", fileName);

        // 根据数据文件的list转化为一条完全干净的只含文件内容的字符串xxxxxxxxxxxx，并利用此字符串计算文件内容摘要
        String fileAbstract = AuditTool.getFileAbstract(dataList.toString().substring(1, dataList.toString().length() - 1).replace(", ", ""));
        // r = H2(β||F)
        Element r_ = this.getR(fileAbstract);
        // h = g^r
        Element h_ = this.getH(r_);
        // h1 = h^β
        Element h1_ = this.getH1(h_);
        // R = r' - r
        Element R = r_.duplicate().sub(AuditTool.str2ZpElement(oldTransParams.getR()));

        TransParams transParams = new TransParams();

        transParams.setR(AuditTool.zpElement2Str(R));
        // x_ ∈ Z_p 群上随机选择的一个元素
        Element x_ = AuditTool.getRandomZpElement();
        // aux = 1/(β+H2(F||h)) - x' + aux
        Element aux_ = AuditTool.getZpOne()
                .div(
                        this.sk.duplicate().add(AuditTool.hashTwo(getFaConcatH(fileAbstract, h_)))
                ).sub(
                        x_
                ).add(
                        AuditTool.str2ZpElement(oldTransParams.getAux())
                );
        transParams.setAux(AuditTool.zpElement2Str(aux_));

        // V = v * u^{x'}
        Element v = AuditTool.str2G1Element(oldTransParams.getV()).mul(AuditTool.u.duplicate().powZn(x_));
        transParams.setV(AuditTool.g1Element2Str(v));
        params.put("transParams", JSONObject.toJSONString(transParams));


        // 插入审计材料
        AuditParams auditParams = AuditParams.builder()
                .h(AuditTool.g1Element2Str(h_))
                .h1(AuditTool.g1Element2Str(h1_))
                .fa(fileAbstract)
                .n(dataList.size())
                .build();
        params.put("auditParams", JSONObject.toJSONString(auditParams));

        return params;
    }

    public String getFaConcatH(String fileAbstract, Element h) {
        return fileAbstract+h.toString();
    }

    /**
     * 根据文件数据list加载需要上传的参数
     * 需要上传的参数有：<br/>
     * 用户名、文件名、签名list、审计此数据的参数auditParams、转移此数据所有权的参数transParams
     * @param dataList 文件数据list
     * @param fileName 文件名
     * @return 需要上传的参数
     */
    public HashMap<String, String> loadParams(List<String> dataList, String fileName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", this.getUserName());
        params.put("fileName", fileName);

        // 从数据list中解析出文件内容的Base64字符串的文件摘要
        String fileAbstract =AuditTool.getFileAbstract(dataList.toString().substring(1, dataList.toString().length() - 1).replace(", ", ""));
        // 计算 r = H2（α||F）
        Element r = this.getR(fileAbstract);
        // 计算 h = g^r
        Element h = this.getH(r);
        // 为并行处理做准备
        HashMap<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            hashMap.put(i, dataList.get(i));
        }
        // 并行进行签名
        List<String> signList = new ArrayList<>(
                hashMap.entrySet().stream()
                        .parallel()
                        // 将Element形式的签名结果转化成byte[]形式的字符串（为了能够转回Element），再将此字符串编码成base64形式的字符串
                        .map(entry -> Base64.getEncoder().encodeToString(AuditTool.g1Element2Str(sign(r, h, fileAbstract, entry.getKey(), entry.getValue().getBytes())).getBytes()))
                        .toList());
        // h1 = h^α = g^(αr)
        Element h1 = this.getH1(h);
        // 将base64形式的签名list转化为一条字符串，去除中括号，将", "替换成”\n“，载入参数
        params.put("signs", signList.toString().substring(1, signList.toString().length()-1).replace(", ", "\n"));


        // 插入审计材料
        AuditParams auditParams = AuditParams.builder()
                .h(AuditTool.g1Element2Str(h))
                .h1(AuditTool.g1Element2Str(h1))
                .fa(fileAbstract)
                .n(signList.size())
                .build();
        params.put("auditParams", JSONObject.toJSONString(auditParams));

        TransParams transParams = new TransParams();
        // x ∈ Z_p，是随机选择的一个元素
        Element x = AuditTool.getRandomZpElement();
        // R = r = H2（α||F）
        transParams.setR(AuditTool.zpElement2Str(getR(fileAbstract)));
        // v = u ^ x
        transParams.setV(AuditTool.g1Element2Str(AuditTool.u.duplicate().powZn(x)));
        // aux = 0-1/(α+H2(F||h))-x
        Element aux = AuditTool.getZpZero()
                .sub(
                        AuditTool.getZpOne().div(
                                this.sk.duplicate().add(AuditTool.hashTwo(getFaConcatH(fileAbstract, h)))
                        )
                ).sub(
                        x
                );
        transParams.setAux(AuditTool.zpElement2Str(aux));
        params.put("transParams", JSONObject.toJSONString(transParams));
        return params;
    }

    /**
     * 获取挑战
     * @param auditParams 审计参数
     * @return 获取挑战
     */
    public List<Challenge> getChallenges(AuditParams auditParams) {
        return AuditTool.yieldChallenges(460, auditParams.getN());
    }
}
