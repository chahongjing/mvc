package com.zjy.common.shiro;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiroUserInfo implements IUserInfo {
    /**
     * 用户Guid
     */
    private Long id;
    /**
     * 代码
     */
    private String code;
    /**
     * 密码
     */
    private String password;
}
