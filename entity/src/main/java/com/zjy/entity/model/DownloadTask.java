package com.zjy.entity.model;

import com.zjy.entity.enums.DownTaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DownloadTask {
    private Long id;
    private Long createdBy;
    private String createdName;
    private Date createdDate;
    private Date updatedDate;
    private String fileUrl;
    private String fileName;
    private int progress;
    private DownTaskStatus status;
    private String message;
    private String stackTrace;
}
