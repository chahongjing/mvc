package com.zjy.service.service;

import com.zjy.dao.UserRoleDao;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import com.zjy.service.common.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseService<UserRoleDao, UserRole> implements UserRoleService {

    public List<UserRoleVo> queryListByUserId(Serializable userId){
//        return dao.queryListByUserId(userId);
        throw new NotImplementedException();
    }
}
