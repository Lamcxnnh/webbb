package com.seig.rental.model.config;

import cn.hutool.crypto.digest.BCrypt;
import com.seig.rental.model.entity.SystemUser;
import com.seig.rental.model.service.SystemUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化默认管理员账号
 */
@Slf4j
@Component
public class InitDataRunner implements ApplicationRunner {

    @Resource
    private SystemUserService systemUserService;

    /** init.sql 中的占位符 hash（非真实密码） */
    private static final String PLACEHOLDER_HASH =
            "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh";

    @Override
    public void run(ApplicationArguments args) {
        SystemUser exist = systemUserService.getByUsername("admin");
        if (exist != null) {
            String pwd = exist.getPassword();

            // 占位符hash → 强制更新为真实密码
            if (PLACEHOLDER_HASH.equals(pwd)) {
                exist.setPassword(BCrypt.hashpw("admin123"));
                systemUserService.updateById(exist);
                log.info("管理员占位符密码已替换为: admin123");
                return;
            }

            // 明文密码 → 加密
            if (!pwd.startsWith("$2a$")) {
                exist.setPassword(BCrypt.hashpw(pwd));
                systemUserService.updateById(exist);
                log.info("管理员密码已加密");
            } else {
                log.info("管理员账号已就绪: admin");
            }
            return;
        }

        // 创建默认超级管理员
        SystemUser admin = new SystemUser();
        admin.setUsername("admin");
        admin.setPassword(BCrypt.hashpw("admin123"));
        admin.setName("超级管理员");
        admin.setType("super");
        admin.setStatus(1);
        systemUserService.save(admin);

        log.info("默认管理员账号已创建: admin / admin123");
    }
}
