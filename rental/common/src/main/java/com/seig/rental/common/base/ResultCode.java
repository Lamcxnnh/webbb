package com.seig.rental.common.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一返回状态码
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(400, "操作失败"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(422, "参数校验失败"),
    SERVER_ERROR(500, "服务器内部错误"),

    // 业务错误码 (1xxxx)
    USER_NOT_EXIST(10001, "用户不存在"),
    USER_DISABLED(10002, "用户已被禁用"),
    USER_PHONE_EXIST(10003, "手机号已注册"),
    VERIFY_CODE_ERROR(10004, "验证码错误"),
    VERIFY_CODE_EXPIRED(10005, "验证码已过期"),
    USERNAME_OR_PASSWORD_ERROR(10006, "用户名或密码错误"),
    CAPTCHA_ERROR(10007, "图形验证码错误"),
    CAPTCHA_EXPIRED(10008, "图形验证码已过期"),

    APARTMENT_NOT_EXIST(10101, "公寓不存在"),
    ROOM_NOT_EXIST(10102, "房间不存在"),

    APPOINTMENT_NOT_EXIST(10201, "预约不存在"),
    APPOINTMENT_STATUS_ERROR(10202, "预约状态不允许此操作"),

    LEASE_NOT_EXIST(10301, "租约不存在"),
    LEASE_STATUS_ERROR(10302, "租约状态不允许此操作"),

    FILE_UPLOAD_FAIL(10401, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORT(10402, "不支持的文件类型");

    private final int code;
    private final String msg;
}
