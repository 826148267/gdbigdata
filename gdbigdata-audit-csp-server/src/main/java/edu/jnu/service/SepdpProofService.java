package edu.jnu.service;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年06月04日 21:05
 */
@Service
public class SepdpProofService {

    public ArrayList<BigInteger> getMList(String mStr, ArrayList<Integer> iList) {
        return getBigIntListInString(mStr, iList);
    }

    public ArrayList<BigInteger> getSList(String sStr, ArrayList<Integer> iList) {
        ArrayList<BigInteger> results = getBigIntListInString(sStr, iList);
        String[] tmps = sStr.split(",");
        results.add(new BigInteger(tmps[tmps.length-1]));
        return results;
    }

    /**
     * 从"str1,str2,str3"类型的字符串中分割出大整数类型的数组
     * 然后根据下标数组选出新数组
     * @param originStr 源字符串
     * @param iList 下标数组
     * @return  返回新数组
     */
    private ArrayList<BigInteger> getBigIntListInString(String originStr, ArrayList<Integer> iList) {
        // 以.分割字符串，然后将所以字符串都转化为大整数类型
        String[] tmps = originStr.split(",");
        ArrayList<BigInteger> results = new ArrayList<>();
        for (int i = 0; i < iList.size(); i++) {
            results.add(i, new BigInteger(tmps[iList.get(i)]));
        }
        return results;
    }

    /**
     * γ = g^(R^(v_1*s_1+...+v_s*s_s)).
     * @param vList 随机数数组
     * @param mList 数据m数组
     * @return  返回gamma，用于完整性证明
     */
    public BigInteger getGamma(ArrayList<BigInteger> vList, ArrayList<BigInteger> mList) {
        BigInteger ma = getMultiplyAccumulate(vList, mList);
        BigInteger q = new BigInteger("107156293229077729250091877527205559797152387746091257873901795127714478829355151268192987540660538345541980278221919721227729188704880233489986953992401708029233425152676955790084204861352363073698986713212153973898552321306534386482266485959243979005986389570173096319404511570103341154639099929588845360061");
        BigInteger g = new BigInteger("67480145160642245730610966908890759954458566680508543923062777286992616368154012155930669347704235940523150168175801535785992220816518281617906551691534103230122389269615659745926725080666152793292320623358342609897553412452664898493878617859058425576319814485389864487490094559757162781025083913236570043411");
        return g.modPow(ma, q);
    }

    /**
     * 计算aList、bList内大整数的乘积累加.
     * @param aList a数组
     * @param bList b数组
     * @return  大整数乘积累加的值（大整数类型）
     */
    private BigInteger getMultiplyAccumulate(ArrayList<BigInteger> aList, ArrayList<BigInteger> bList) {
        int size = Math.min(aList.size(), bList.size());
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < size; i++) {
            result = result.add(aList.get(i).multiply(bList.get(i)));
        }
        return result;
    }

    /**
     * α = R^(v_1*m_1+...+v_s*m_s)
     * @param R 标签数组的最后一个大整数
     * @param vList 随机数数组
     * @param mList 数据数组
     * @return  返回α
     */
    public BigInteger getAlpha(BigInteger R, ArrayList<BigInteger> vList, ArrayList<BigInteger> mList) {
        BigInteger ma = getMultiplyAccumulate(vList, mList);
        BigInteger q = new BigInteger("107156293229077729250091877527205559797152387746091257873901795127714478829355151268192987540660538345541980278221919721227729188704880233489986953992401708029233425152676955790084204861352363073698986713212153973898552321306534386482266485959243979005986389570173096319404511570103341154639099929588845360061");
        return R.modPow(ma, q);
    }
}
