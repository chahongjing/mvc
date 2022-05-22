package com.zjy.dao.vo;

import com.zjy.entity.model.RoleInfo;

public class RoleInfoVo extends RoleInfo {
    private String userId;
    private boolean isSave;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
