package com.seig.rental.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seig.rental.model.entity.UserInfo;
import com.seig.rental.model.mapper.UserInfoMapper;
import com.seig.rental.model.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public IPage<UserInfo> pageQuery(IPage<UserInfo> page, String phone, String nickname, Integer status) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(phone)) {
            wrapper.like(UserInfo::getPhone, phone);
        }
        if (StringUtils.hasText(nickname)) {
            wrapper.like(UserInfo::getNickname, nickname);
        }
        if (status != null) {
            wrapper.eq(UserInfo::getStatus, status);
        }
        wrapper.orderByDesc(UserInfo::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        UserInfo user = new UserInfo();
        user.setId(id);
        user.setStatus(status);
        this.updateById(user);
    }

    @Override
    public UserInfo getByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, phone));
    }
}
