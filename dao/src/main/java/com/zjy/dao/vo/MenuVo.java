package com.zjy.dao.vo;

import com.zjy.entity.model.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuVo extends Menu {
    private boolean isSave;
    private String parentName;
    private List<Menu> children;

    public MenuVo() {
        children = new ArrayList<>();
    }

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
