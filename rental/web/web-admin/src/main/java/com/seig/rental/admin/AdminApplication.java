package com.seig.rental.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 后台管理系统 - 启动类
 */
@SpringBootApplication(scanBasePackages = "com.seig.rental")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("""

                ============================================
                  Rental Admin Server 启动成功!
                  Knife4j 文档: http://localhost:8080/admin/doc.html
                ============================================
                """);
    }
}
