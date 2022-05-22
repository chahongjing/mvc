package com.zjy.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjy.baseframework.enums.YesNo;
import com.zjy.entity.enums.Sex;
import com.zjy.entity.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@TableName("user_info")
public class UserInfo implements Serializable {
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
    /**
     * 用户Guid
     */
    @TableId
    private Long id;
    /**
     * 代码
     */
//    @JSONField(name="user_code")
    @TableField("code")
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;

    @TableField("sex")
    private Sex sex;
    /**
     * 是否系统级
     */
    private YesNo isSystem;

    /**
     * 是否禁用
     */
    private UserStatus status;
    /**
     * 出生日期
     */
    private Date birthday;
}
