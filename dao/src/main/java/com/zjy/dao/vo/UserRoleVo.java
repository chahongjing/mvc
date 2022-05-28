package com.zjy.dao.vo;

import com.zjy.entity.model.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRoleVo extends UserRole {
    private String roleCode;
    private List<Long> userIdList;
}
