package com.zjy.dao.vo;

import com.zjy.entity.model.Menu;

public class MenuVo extends Menu {
    private boolean isSave;
    private String pName;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
