package com.zjy.service.service.impl;

import com.zjy.dao.UserRoleDao;
import com.zjy.dao.vo.UserRoleVo;
import com.zjy.entity.model.UserRole;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    public List<UserRoleVo> queryListByUserId(Long userId){
        UserRoleVo urv = new UserRoleVo();
        urv.setUserId(userId);
        return dao.queryListByUserId(urv);
    }
}
