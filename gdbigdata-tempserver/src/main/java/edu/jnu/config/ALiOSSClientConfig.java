package edu.jnu.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置OSS客户端
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 18:49
 */
@Configuration
public class ALiOSSClientConfig {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access.key.id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access.key.secret}")
    private String accessKeySecret;

    @Bean
    public OSS createOssClient() {
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
//        conf.setDefaultHeaders(); 可以通过conf进行设置
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);
        return oss;
    }
}
