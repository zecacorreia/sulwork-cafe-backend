package com.mv.sulworkcafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mv.sulworkcafe")
public class SulworkCafeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SulworkCafeApplication.class, args);
    }
}