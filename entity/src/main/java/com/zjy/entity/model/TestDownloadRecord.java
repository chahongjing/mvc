package com.zjy.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TestDownloadRecord {
    private String userId;
    /**
     * 代码
     */
    private String userCode;
    /**
     * 名称
     */
    private String userName;

    private Date createdOn;

    private Float money;

    private Long num;

    private String link;
}
