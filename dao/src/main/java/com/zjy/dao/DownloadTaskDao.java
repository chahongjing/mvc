package com.zjy.dao;

import com.zjy.entity.model.DownloadTask;

import java.util.List;

public interface DownloadTaskDao {
    int insert(DownloadTask task);
    DownloadTask get(String id);
    int update(DownloadTask task);
    List<DownloadTask> queryList();
}
