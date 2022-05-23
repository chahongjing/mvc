package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpgradeLog {
    private Long id;
    private Date upgradeTime;
    private String title;
    private String content;
    private Date createTime;
}
