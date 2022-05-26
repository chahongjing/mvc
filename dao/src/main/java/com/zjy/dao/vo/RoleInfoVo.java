package com.zjy.dao.vo;

import com.zjy.entity.model.RoleInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleInfoVo extends RoleInfo {
    private Long userId;
    private boolean isSave;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
