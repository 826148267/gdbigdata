import edu.jnu.utils.MySecureTool;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月08日 11时23分
 * @功能描述: TODO
 */
public class Ceshi {
    public static void main(String[] args) throws IOException {
//        Element x = AuditTool.BP.getZr().newRandomElement();
//        Element u_x = AuditTool.u.duplicate().powZn(x);
//        Element y = AuditTool.BP.getZr().newRandomElement();
//        Element u_y = AuditTool.u.duplicate().powZn(y);
//        System.out.println(u_x.mul(u_y).isEqual(AuditTool.u.powZn(x.add(y))));

//        Element r = AuditTool.hashTwo("F||x");
//        Element r_ = AuditTool.hashTwo("F||y");
//        Element r__ = r_.sub(r);
//        Element dishu = AuditTool.hashOne("F||i");
//        System.out.println(dishu.powZn(r).mul(dishu.powZn(r__)).isEqual(dishu.powZn(r_)));

//        String m = "UEsDBBQABgAIAAAAIQDfpNJsWgEAACAFAAATAAgCW0NvbnRlbnRfVHlwZXNdLnhtbCCiBAIooAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC0lMtuwjAQRfeV+g+Rt1Vi6KKqKgKLPpYtUukHGHsCVv2Sx7z+vhMCUVUBkQpsIiUz994zVsaD0dqabAkRtXcl6xc9loGTXmk3K9nX5C1/ZBkm4ZQw3kHJNoBsNLy9GUw2ATAjtcOSzVMKT5yjnIMVWPgAjiqVj1Ykeo0zHoT8FjPg973eA5feJXApT7UHGw5eoBILk7LXNX1uSCIYZNlz01hnlUyEYLQUiep86dSflHyXUJBy";
//        Element mElment = AuditTool.hashTwo(m);
//        String fileAbstract = "sX3ro7/SIHLY05D6kLjOPQ==";
//
//        User user = new User();
//        User user_ = new User();
//        Element sk1 = AuditTool.hashTwo("test");
//        Element sk2 = AuditTool.hashTwo("chendi");
//        user.setSk(sk1);
//        user.setPk(AuditTool.g.duplicate().powZn(sk1));
//        user_.setSk(sk2);
//        user_.setPk(AuditTool.g.duplicate().powZn(sk2));
//
//        Element x = AuditTool.hashTwo("F||x");
//        Element x_ = AuditTool.hashTwo("F||x_");
//
//        Element r = user.getR(fileAbstract);
//        Element r_ = user_.getR(fileAbstract);
//        Element h = user.getH(r);
//        Element h_ = user_.getH(r_);
//        Element h1 = user.getH1(h);
//        Element h1_ = user_.getH1(h_);
//
//        List<Challenge> challenges = new ArrayList<>();
//        challenges.add(new Challenge(0, 3));
//        Element sign = user.sign(r, h, fileAbstract, 0, m.getBytes());
//        boolean verified = user.verify(user.getPk(), sign.duplicate().powZn(AuditTool.hashTwo(String.valueOf(challenges.get(0).getRandom()))), mElment.duplicate().mul(AuditTool.hashTwo(String.valueOf(challenges.get(0).getRandom()))), fileAbstract, h, h1, challenges);
//        System.out.println(verified);
//
//        Element R = r_.duplicate().sub(r);
//        Element aux = AuditTool.getZpZero()
//                .sub(
//                        AuditTool.getZpOne()
//                                .div(
//                                        user.getSk().duplicate().add(AuditTool.hashTwo(user.getFaConcatH(fileAbstract, h)))
//                                )
//                ).sub(
//                        x
//                );
//        Element v = AuditTool.u.duplicate().powZn(x);
//
//        Element aux_ = AuditTool.getZpOne()
//                                .div(
//                                        user_.getSk().duplicate().add(AuditTool.hashTwo(user_.getFaConcatH(fileAbstract, h_)))
//                                )
//                                .sub(x_)
//                                .add(aux);
//        Element v_ = AuditTool.u.duplicate().powZn(x_).mul(v);
//
//        Element sign_ = sign.duplicate()
//                .mul(
//                        AuditTool.hashOne(fileAbstract+0).powZn(R)
//                )
//                .mul(
//                        AuditTool.u.duplicate().powZn(AuditTool.hashTwo(m)).powZn(aux_)
//                )
//                .mul(
//                        v_.duplicate().powZn(AuditTool.hashTwo(m))
//                );
//
//        boolean verified1 = user_.verify(user_.getPk(), sign_.duplicate().powZn(AuditTool.hashTwo(String.valueOf(challenges.get(0).getRandom()))), mElment.duplicate().mul(AuditTool.hashTwo(String.valueOf(challenges.get(0).getRandom()))), fileAbstract, h_, h1_, challenges);
//        System.out.println(verified1);

//        // 0 - 1/(alpha+H(F||h))) - x
//        Element aux = AuditTool.getZpZero()
//                .sub(
//                        AuditTool.getZpOne().div(sk1.duplicate().add(AuditTool.hashTwo("F"+h.toString())))
//                ).sub(
//                        x
//                );
//        // 1/(alpha+H(F||h)))
//        Element tmp = AuditTool.getZpOne().div(sk1.duplicate().add(AuditTool.hashTwo("F"+h)));
//        // 1/(beta+H(F||h)))
//        Element tmp_ = AuditTool.getZpOne().div(sk2.duplicate().add(AuditTool.hashTwo("F"+h)));
//        // 1/(beta+H(F||h))) - x' + aux
//        Element aux_ = AuditTool.getZpOne().div(sk2.duplicate().add(AuditTool.hashTwo("F"+h))).sub(x_).add(aux);
//        // 标签
//        Element sign = AuditTool.u.duplicate().powZn(tmp);
//        Element sign_ = AuditTool.u.duplicate().powZn(tmp_);
//        System.out.println(
//                // u^(1/(alpha+H(F||h))))
//                sign
//                        .mul(
//                                // u^(aux_)
//                                AuditTool.u.duplicate().powZn(aux_)
//                        ).mul(
//                                // u^(x+x_)
//                                AuditTool.u.duplicate().powZn(x).mul(AuditTool.u.duplicate().powZn(x_))
//                        ).isEqual(
//                                // u^(1/(beta+H(F||h)))
//                                sign_
//                        )
//        );



//        Path path = Paths.get("D:\\广东省大数据项目代码\\gdbigdata\\gdbiddate-ldcia-server-v2\\src\\main\\resources\\a.properties");
//        String dedupKey = MySecureTool.getDedupKey(path.toFile());
//        System.out.println(dedupKey);
//
//        String data = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
//        System.out.println(data);
//        String cipherText = MySecureTool.encrypt(data, dedupKey);
//        System.out.println(cipherText);
//        String message = MySecureTool.decrypt(cipherText, dedupKey);
//        System.out.println(message);
//
//        String key = "fwerfrefe";
//        String dedupKeyCipher = MySecureTool.encryptDedupKey(dedupKey, key);
//        System.out.println(dedupKeyCipher);
//        String dedupKeyMessage = MySecureTool.decryptDedupKey(dedupKeyCipher, key);
//        System.out.println(dedupKeyMessage);

        /**************************************************分表创表脚本****************************************************/
//        String a =
//                    """
//                    create table table_user_info_+
//                    (
//                        user_id           bigint(20)  not null
//                            primary key,
//                        user_address      varchar(2500) null,
//                        user_file_nums    varchar(2500) null,
//                        user_name         varchar(2500) null,
//                        user_organization varchar(2500) null
//                    );
//                    """;
//        ArrayList<String> lines = new ArrayList<>();
//        for (int i = 1000; i < 1024; i++) {
//            System.out.print(a.replace("+", String.valueOf(i)));
//        }

//        String a =
//                """
//                alter table table_user_info_+ modify column user_id bigint(20) NOT NULL COMMENT '用户id';
//                """;
//        ArrayList<String> lines = new ArrayList<>();
//        for (int i = 0; i < 1000; i++) {
//            System.out.print(a.replace("+", String.valueOf(i)));
//        }

//
//                String a =
//                    """
//                    drop table table_user_info_+;
//                    """;
//        System.out.println(a);
//        ArrayList<String> lines = new ArrayList<>();
//        for (int i = 2; i < 1000; i++) {
//            System.out.print(a.replace("+", String.valueOf(i)));
//        }

//        String a =
//                """
//                analyze table table_user_info_+;
//                """;
//        ArrayList<String> lines = new ArrayList<>();
//        for (int i = 1; i < 1024; i++) {
//            System.out.print(a.replace("+", String.valueOf(i)));
//        }




//        String string1 = Files.readString(Paths.get("C:\\Users\\82614\\Desktop\\全栈工程师简历_v4 - 副本.pdf"), StandardCharsets.ISO_8859_1);
//        String string2 = Files.readString(Paths.get("C:\\Users\\82614\\Desktop\\全栈工程师简历_v4.pdf"), StandardCharsets.ISO_8859_1);
//        System.out.println(string1.equals(string2));


    }
}