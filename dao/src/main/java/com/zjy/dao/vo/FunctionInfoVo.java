package com.zjy.dao.vo;

import com.zjy.entity.model.FunctionInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunctionInfoVo extends FunctionInfo {
    private boolean isSave;
    private String menuName;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }
}
