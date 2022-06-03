package com.zjy.common.shiro;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ShiroUserInfo implements IUserInfo, Serializable {
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
    /**
     * 姓名
     */
    private String name;
}
