package edu.jnu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年05月15日 19:17
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Gateway17001 {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(Gateway17001.class, args);
    }
}
