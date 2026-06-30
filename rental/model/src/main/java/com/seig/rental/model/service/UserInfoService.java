package com.seig.rental.model.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seig.rental.model.entity.UserInfo;

public interface UserInfoService extends IService<UserInfo> {

    /** 条件分页查询用户 */
    IPage<UserInfo> pageQuery(IPage<UserInfo> page, String phone, String nickname, Integer status);

    /** 更新用户状态 */
    void updateStatus(Long id, Integer status);

    /** 根据手机号查询用户 */
    UserInfo getByPhone(String phone);
}
