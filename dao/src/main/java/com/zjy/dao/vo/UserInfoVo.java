package com.zjy.dao.vo;

import com.zjy.entity.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class UserInfoVo extends UserInfo {
    private String mingcheng;
    private String passwordAgain;
    private boolean isSave;
    private String orderBy;
    private Set<String> permissionList;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
