package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.SystemUser;
import com.seig.rental.model.mapper.SystemUserMapper;
import com.seig.rental.model.service.SystemUserService;
import org.springframework.stereotype.Service;

/**
 * 后台管理员服务实现
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {

    @Override
    public SystemUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SystemUser>()
                .eq(SystemUser::getUsername, username));
    }
}
