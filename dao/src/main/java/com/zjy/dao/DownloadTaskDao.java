package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.entity.model.DownloadTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DownloadTaskDao extends BaseMapper<DownloadTask> {
    int insert(DownloadTask task);
    DownloadTask get(String id);
    int update(DownloadTask task);
    List<DownloadTask> queryList();
}
