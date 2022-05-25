package com.zjy.dao.vo;

import com.zjy.entity.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RelateCheckVo {
    private Long id;
    private String name;
    private Long relativeId;
    private boolean isCheck;
    private boolean singleCheck;
    private PermissionType type;
    private boolean showDetail;
    List<RelateCheckVo> subList;

    public RelateCheckVo() {
        subList = new ArrayList<>();
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
}
