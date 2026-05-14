package com.example.lease.common.exception;

import com.example.lease.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * 推荐自定义异常：继承RuntimeException
 * 因为：
 * 业务层抛运行期异常，事务会自动回滚。而编译期异常事务不回滚的。
 */
@Data
public class LeaseException extends RuntimeException {

    private Integer code; //异常状态码

    public LeaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public LeaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "LeaseException{" +
                "code=" + code +
                "message=" + super.getMessage() +
                '}';
    }
}
