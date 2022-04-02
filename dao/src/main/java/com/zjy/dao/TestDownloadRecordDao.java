package com.zjy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.entity.model.TestDownloadRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDownloadRecordDao extends BaseMapper<TestDownloadRecord> {
    int insert(TestDownloadRecord record);
    List<TestDownloadRecord> queryList();
}
