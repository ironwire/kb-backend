package org.yiqixue.kbbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories(basePackages = "org.yiqixue.kbbackend.repository")
@EnableElasticsearchRepositories(basePackages = "org.yiqixue.kbbackend.repository")
@EnableAsync
public class KbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KbBackendApplication.class, args);
    }

}
