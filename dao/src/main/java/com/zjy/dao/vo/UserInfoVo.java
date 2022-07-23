package com.zjy.dao.vo;

import com.zjy.common.common.ExcelRowData;
import com.zjy.entity.model.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserInfoVo extends UserInfo implements ExcelRowData {
    private String mingcheng;
    private String passwordAgain;
    private boolean isSave;
    private String orderBy;
    private Set<String> permissionList;
    private String interests;
    private Set<Integer> interestList;
    private List<String> errorMsg;

    public boolean getIsSave() {
        return isSave;
    }

    public void setIsSave(boolean isSave) {
        this.isSave = isSave;
    }

    @Override
    public void appendErrorMsg(String errorMsg) {
        if(this.errorMsg == null) this.errorMsg = new ArrayList<>();
        this.errorMsg.add(errorMsg);
    }
}
