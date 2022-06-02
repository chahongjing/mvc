package com.zjy.entity.model;

import com.zjy.entity.enums.Sex;
import com.zjy.entity.enums.UserStatus;
import com.zjy.entity.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserInfo implements Serializable {
    /**
     * 用户Guid
     */
    private Long id;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;

    private Sex sex;
    /**
     * 是否系统级
     */
    private UserTypeEnum type;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 是否禁用
     */
    private UserStatus status;
    /**
     * 创建人
     */
    private Long createdBy;
    /**
     * 创建日期
     */
    private Date createdOn;
    /**
     * 修改人
     */
    private Long modifiedBy;
    /**
     * 修改日期
     */
    private Date modifiedOn;
}
