package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.entity.model.DownloadTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DownloadTaskDao extends BaseDao<DownloadTask> {
    int update(DownloadTask task);
    List<DownloadTask> queryList();
}
