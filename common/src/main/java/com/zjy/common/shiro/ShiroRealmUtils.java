package com.zjy.common.shiro;

import com.zjy.common.common.SpringContextHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

public class ShiroRealmUtils {
    private ShiroRealmUtils() {
    }

    private static MyAuthorizingRealm realm = SpringContextHolder.getBean("myShiroRealm");

    public static IUserInfo getCurrentUser() {
        try {
            return (IUserInfo) SecurityUtils.getSubject().getPrincipal();
        } catch (UnavailableSecurityManagerException ignored) {

        }
        return null;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static boolean isPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    /**
     * 获取md5 hash+盐值加密后的值
     *
     * @param password 密码
     * @param salt     盐值
     * @return
     */
    public static String getMd5Hash(String password, String salt) {
        if (realm == null) throw new UnsupportedOperationException("无法获取加密信息！");
        HashedCredentialsMatcher credentialsMatcher = (HashedCredentialsMatcher) realm.getCredentialsMatcher();
        SimpleHash simpleHash = new SimpleHash(credentialsMatcher.getHashAlgorithmName(), password, ByteSource.Util.bytes(salt),
                credentialsMatcher.getHashIterations());
//        String newCredentials = new Md5Hash(password, salt, credentialsMatcher.getHashIterations()).toBase64();
//        Sha512Hash sha512Hash = new Sha512Hash(password, salt, credentialsMatcher.getHashIterations());
//        return simpleHash.toBase64();
        return simpleHash.toString();
    }

    public static String getSSOPassword(String password, String salt) {
        ConfigurableHashService hashService = new DefaultHashService();
        // 静态盐值
        hashService.setPrivateSalt(ByteSource.Util.bytes("."));
        // md5hash
        hashService.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
        // 加密2次
        hashService.setHashIterations(2);
        HashRequest request = new HashRequest.Builder()
                .setSalt(salt)
                .setSource(password)
                .build();
        String res = hashService.computeHash(request).toHex();
        System.out.println(res);
        return res;
    }

    public static Set<String> getPermissions() {
        return realm == null ? new HashSet<>() : realm.getPermissions();
    }

    // 刷新权限
    public static void reloadAuthorizing(Object principal) {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyAuthorizingRealm myShiroRealm = (MyAuthorizingRealm) rsm.getRealms().iterator().next();

        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, realmName);
        subject.runAs(principals);
        if (myShiroRealm.isAuthenticationCachingEnabled()) {
            myShiroRealm.getAuthenticationCache().remove(principals);
        }
        if (myShiroRealm.isAuthorizationCachingEnabled()) {
            // 删除指定用户shiro权限
            myShiroRealm.getAuthorizationCache().remove(principals);
        }
        // 刷新权限
        subject.releaseRunAs();
    }
}
