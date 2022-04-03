package com.zjy.entity.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjy.entity.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@TableName("UserInfo")
public class UserInfo implements Serializable {
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建日期
     */
    private Date createdOn;
    /**
     * 修改人
     */
    private String modifiedBy;
    /**
     * 修改日期
     */
    private Date modifiedOn;
    /**
     * 用户Guid
     */
    @TableId
    private String userId;
    /**
     * 代码
     */
//    @JSONField(name="user_code")
    @TableField("userCode")
    private String userCode;
    /**
     * 名称
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    @TableField("sex")
    private Sex sex;
}
