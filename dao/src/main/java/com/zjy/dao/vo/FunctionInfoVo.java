package com.zjy.dao.vo;

import com.zjy.entity.model.FunctionInfo;

public class FunctionInfoVo extends FunctionInfo {
    private boolean isSave;
    private String menuName;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
