package com.valor.manager;

import com.mfc.config.ConfigTools3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class EasyWebApplication {

    public static void main(String[] args) {
        ConfigTools3.load("config");
        SpringApplication.run(EasyWebApplication.class, args);
    }
}
