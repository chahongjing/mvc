package com.zjy.baseframework.common;

import com.zjy.baseframework.interfaces.IHierarchyBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrgVO implements IHierarchyBase<OrgVO> {
    private Long id;
    private String name;
    private Long pid;
    private int seq;
    private List<OrgVO> children;

}
