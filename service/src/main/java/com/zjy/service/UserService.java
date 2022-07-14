package com.zjy.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjy.dao.UserInfoDao;
import com.zjy.dao.common.multiDataSource.DataSourceKey;
import com.zjy.entity.model.UserInfo;
import com.zjy.dao.common.multiDataSource.DBSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserInfoDao userInfoDao;

    @DBSource
    public UserInfo getFromRandom() {
        PageHelper.startPage(1, 1);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoDao.getList("45b90fb3-d794-4f8f-b0f6-f4744cb9a704"));
        UserInfo userInfo = null;
        if (pageInfo.getTotal() > 0) {
            userInfo = pageInfo.getList().get(0);
            log.info("user random: {}", userInfo.getName());
        }
        return userInfo;
    }

    @DBSource(DataSourceKey.SLAVE)
    public UserInfo getFromSlave() {
        UserInfo userInfo = userInfoDao.selectById("2");
        log.info("user slave: {}", userInfo == null ? null : userInfo.getName());
        return userInfo;
    }
}
