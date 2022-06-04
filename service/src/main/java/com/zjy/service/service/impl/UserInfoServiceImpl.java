package com.zjy.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zjy.baseframework.annotations.LogMethod;
import com.zjy.baseframework.common.ServiceException;
import com.zjy.baseframework.enums.BaseResult;
import com.zjy.baseframework.enums.ResultStatus;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.common.shiro.ShiroUserInfo;
import com.zjy.dao.UserInfoDao;
import com.zjy.dao.vo.*;
import com.zjy.entity.enums.Sex;
import com.zjy.entity.enums.UserStatus;
import com.zjy.entity.enums.UserTypeEnum;
import com.zjy.entity.model.Permission;
import com.zjy.entity.model.UserInfo;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.request.UserInfoRequest;
import com.zjy.service.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chahongjing
 * @create 2016-12-05 22:16
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    protected UserRoleService userRoleSrv;

    @Autowired
    protected RolePermissionService rolePermissionSrv;

    @Autowired
    protected UserPermissionService userPermissionSrv;

    @Autowired
    protected PermissionService permissionService;

    /**
     * 添加用户
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public int insert(UserInfo entity) {
        entity.setCreatedBy(getCurrentUser().getId());
        entity.setCreatedOn(new Date());
        return super.insert(entity);
    }

    /**
     * 修改用户
     *
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public int update(UserInfo entity) {
        entity.setModifiedBy(getCurrentUser().getId());
        entity.setModifiedOn(new Date());
        return super.update(entity);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int delete(Long id) {
        return super.delete(id);
    }

    /**
     * 保存用户
     *
     * @param vo
     */
    @Override
    @Transactional
    public void save(UserInfoVo vo) {
        UserInfoVo voDb = getVo(vo.getId());
        beforeCheck(vo);
        // 处理密码
        if (voDb.getIsSave()) {
            update(vo);
        } else {
            vo.setPassword(ShiroRealmUtils.getMd5Hash(vo.getPassword(), vo.getCode()));
            insert(vo);
        }
    }

    @Override
    public UserInfoVo getVo(Long id) {
        UserInfo userInfo = get(id);
        UserInfoVo vo = new UserInfoVo();
        if (userInfo == null) {
            vo = new UserInfoVo();
            vo.setId(id);
            vo.setSex(Sex.MALE);
            vo.setIsSave(false);
            vo.setType(UserTypeEnum.NORMAL);
            vo.setStatus(UserStatus.NORMAL);
            vo.setCreatedOn(new Date());
            vo.setCreatedBy(getCurrentUser().getId());
        } else {
            vo.setId(userInfo.getId());
            vo.setName(userInfo.getName());
            vo.setCode(userInfo.getCode());
            vo.setSex(userInfo.getSex());
            vo.setType(userInfo.getType());
            vo.setBirthday(userInfo.getBirthday());
            vo.setStatus(userInfo.getStatus());
            vo.setCreatedOn(userInfo.getCreatedOn());
            vo.setCreatedBy(userInfo.getCreatedBy());
            vo.setModifiedOn(userInfo.getModifiedOn());
            vo.setModifiedBy(userInfo.getModifiedBy());
            vo.setIsSave(true);
        }
        return vo;
    }

    @LogMethod
    @Override
    public PageBean<? extends UserInfo> queryPageList(UserInfoRequest request) {
        UserInfoVo user = new UserInfoVo();
        user.setName(request.getName());
        user.setCode(request.getName());
        user.setSex(request.getSex());
        List<String> orderBy = new ArrayList<>();
        if (request.getNameOrderBy() != null) {
            orderBy.add("user.name " + request.getNameOrderBy().toString());
        }
        if (request.getCodeOrderBy() != null) {
            orderBy.add("user.code " + request.getCodeOrderBy().toString());
        }
        if (request.getCreatedOnOrderBy() != null) {
            orderBy.add("user.created_on " + request.getCreatedOnOrderBy().toString());
        }
        request.setOrderBy(String.join(", ", orderBy));
        return super.queryPageListBase(request, user);
    }

    /**
     * 登录
     *
     * @param user
     * @return
     */
    @Override
    public BaseResult<UserInfoVo> login(UserInfo user) {
        BaseResult<UserInfoVo> result = new BaseResult<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getCode(), user.getPassword());
        try {
            // 登录
            subject.login(token);
        } catch (AuthenticationException ex) {
            result.setStatus(ResultStatus.NO);
            result.setMsg("用户名或密码错误！");
            return result;
        } catch (Exception ex) {
            log.error("登录异常！", ex);
            result.setStatus(ResultStatus.NO);
            result.setMsg("登录异常！");
            return result;
        }
        // 登录成功
        UserInfoVo userInfo = getVoByCode(user.getCode());
        userInfo.setPermissionList(ShiroRealmUtils.getPermissions());
        userInfo.setPassword(null);
        result.setStatus(ResultStatus.OK);
        result.setValue(userInfo);
        return result;
    }

    @Override
    public BaseResult<String> logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
            // session 会销毁，在SessionListener监听session销毁，清理权限缓存
        }

        return BaseResult.ok();
    }

    @Override
    public List<UserInfo> query(UserInfo entity) {
//        return (List<UserInfo>) super.queryList(entity);
        throw new NotImplementedException();
    }

    @Override
    public UserInfoVo getVoByCode(String userCode) {
        return dao.getByCode(userCode);
    }

    private void beforeCheck(UserInfoVo vo) {
        if (StringUtils.isBlank(vo.getCode())) {
            throw new ServiceException("请输入用户编号！");
        }
        Long id = vo.getId() == null ? 0L : vo.getId();
        Map<String, Integer> map = dao.queryRepeatCount(id, vo.getCode());
        if (map != null && map.containsKey("codeCount") && map.get("codeCount").intValue() > 0) {
            throw new ServiceException("编号重复！");
        }
        if (StringUtils.isBlank(vo.getName())) {
            throw new ServiceException("请输入用户名称！");
        }
        if (!vo.getIsSave() && StringUtils.isBlank(vo.getPassword())) {
            throw new ServiceException("请输入密码！");
        }
        if (!vo.getIsSave() && !vo.getPassword().equals(vo.getPasswordAgain())) {
            throw new ServiceException("两次密码不一致！");
        }
    }

    @Override
    public void changePassword(String code, String oldPassword, String newPassword) {
        UserInfo currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new ServiceException("用户未登录！");
        }
        String userCodeCur = currentUser.getCode();
        if (!userCodeCur.equals(code)) {
            throw new ServiceException("参数错误！");
        }
        UserInfo user = this.getVoByCode(userCodeCur);
        if (user == null) {
            throw new ServiceException("用户不存在！");
        }
        if (StringUtils.isBlank(oldPassword)) {
            throw new ServiceException("请输入原密码！");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new ServiceException("请输入新密码！");
        }
        String oldPasswordEnc = ShiroRealmUtils.getMd5Hash(oldPassword, userCodeCur);
        if (!oldPasswordEnc.equals(user.getPassword())) {
            throw new ServiceException("原密码错误！");
        }
        user.setPassword(ShiroRealmUtils.getMd5Hash(newPassword, userCodeCur));
        dao.updateUserPassword(user.getId(), user.getPassword());
    }

    @Override
    public void resetPassword(String code, String password) {
        UserInfo user = this.getVoByCode(code);
        if (user == null) {
            throw new ServiceException("用户不存在！");
        }
        if (StringUtils.isBlank(password)) {
            throw new ServiceException("密码不能为空！");
        }
        user.setPassword(ShiroRealmUtils.getMd5Hash(password, code));
        dao.updateUserPassword(user.getId(), user.getPassword());
    }

    @Override
    public List<String> queryRoleCodeListByUserId(Long userId) {
        List<UserRoleVo> userRoleVos = userRoleSrv.queryListByUserId(userId);
        return userRoleVos.stream().map(UserRoleVo::getRoleCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getPermissionListByUserId(Long userId) {
        List<PermissionVo> permissionVos = userPermissionSrv.queryUserCombinePermission(userId);
        return permissionVos.stream().map(Permission::getCode).collect(Collectors.toList());
    }

    @Override
    public ShiroUserInfo getByCode(String code) {
        UserInfoVo user = this.getVoByCode(code);
        ShiroUserInfo shiroUser = new ShiroUserInfo();
        shiroUser.setId(user.getId());
        shiroUser.setCode(user.getCode());
        shiroUser.setPassword(user.getPassword());
        return shiroUser;
    }

    public void test() {
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInfo::getId, 12);
        wrapper.set(UserInfo::getName, null);
        wrapper.set(UserInfo::getCode, "newcode");
        dao.update(null, wrapper);
    }
}
