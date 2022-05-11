package edu.jnu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 提供持有证明的类
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月08日 10:36
 */
public class ProofApi {

    /**
     * 需要TPA提供文件路径、标签路径、质询集合Q、承诺打开处z.
     * 利用dpos的证明产生算法，产生证明
     * 返回证明.
     */
    @GetMapping("/dpos/proof")
    public ResponseEntity<?> dpos() {
        return null;
    }

    /**
     * 需要TPA提供文件路径、标签路径、质询集合Q.
     * 利用sepdp的证明产生算法，产生证明.
     * @return 返回持有证明
     */
    @GetMapping("/sepdp/proof")
    public ResponseEntity<?> sepdp() {
        return null;
    }
}
