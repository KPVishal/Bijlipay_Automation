package com.bijlipay;

import com.bijlipay.Utils.PGPDecryption;
import com.bijlipay.properties.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static com.bijlipay.Utils.Constants.LOCALFILEPATH_AXIS;


@EnableAsync
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class BijlipayAutomationApplication {

    public static void main(String[] args) {
        SpringApplication.run(BijlipayAutomationApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}

