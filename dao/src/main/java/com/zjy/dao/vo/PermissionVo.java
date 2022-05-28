package com.zjy.dao.vo;

import com.zjy.entity.enums.PsermissionIncludeType;
import com.zjy.entity.model.Permission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionVo extends Permission {
    private boolean isSave;
    private Long userId;
    private Long roleId;
    private String functionName;
    private PsermissionIncludeType includeType;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
