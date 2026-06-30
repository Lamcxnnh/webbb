package com.seig.rental.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 移动端API - 启动类
 */
@SpringBootApplication(scanBasePackages = "com.seig.rental")
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
        System.out.println("""

                ============================================
                  Rental App Server 启动成功!
                  Knife4j 文档: http://localhost:8081/app/doc.html
                ============================================
                """);
    }
}
