package com.zjy.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.List;
import java.util.Set;

public class MyAuthorizingRealm extends AuthorizingRealm {

    protected IUserService iUserService;

    public void setIUserService(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    /**
     * 获取当前登录用户数据库信息
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        IUserInfo user = iUserService.getByCode(token.getUsername());
        if (user == null) return null;
//        UserInfo user = new UserInfo();
//        user.setCode("zjy");
//        if(token.getCredentials() == null) {
//            return null;
//        }
//        user.setPassword(this.getMd5Hash(new String((char[])token.getCredentials()), token.getUsername()));
        if (null != user) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getCode()), getName());
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户被授予角色和权限
     * 经测试:本例中该方法的调用时机为需授权资源被访问时
     * 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
     * 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
     * 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        IUserInfo user = (IUserInfo) principals.getPrimaryPrincipal();
        List<String> roles = iUserService.queryRoleCodeListByUserId(user.getId());
        List<String> permissions = iUserService.getPermissionListByUserId(user.getId());

        permissions.add("myPer");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //给当前用户设置角色
        info.addRoles(roles);
        //给当前用户设置权限
        info.addStringPermissions(permissions);
        return info;
    }

    public String getMd5Hash(String password, String salt) {
        HashedCredentialsMatcher credentialsMatcher = (HashedCredentialsMatcher) this.getCredentialsMatcher();
        SimpleHash simpleHash = new SimpleHash(credentialsMatcher.getHashAlgorithmName(), password, ByteSource.Util.bytes(salt),
                credentialsMatcher.getHashIterations());
//        String newCredentials = new Md5Hash(password, salt, credentialsMatcher.getHashIterations()).toBase64();
//        Sha512Hash sha512Hash = new Sha512Hash(password, salt, credentialsMatcher.getHashIterations());
//        return simpleHash.toBase64();
        return simpleHash.toString();
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Set<String> getPermissions() {
        AuthorizationInfo authorizationInfo = getAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
        return (Set<String>) authorizationInfo.getStringPermissions();
    }
}
