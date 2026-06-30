package com.seig.rental.model.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.SystemUser;

/**
 * 后台管理员服务
 */
public interface SystemUserService extends IService<SystemUser> {

    /**
     * 根据用户名查询管理员
     */
    SystemUser getByUsername(String username);
}
