package com.zjy.common.shiro;

import com.zjy.common.SpringContextHolder;
import com.zjy.entity.model.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.*;
import org.apache.shiro.util.ByteSource;

import java.util.HashSet;
import java.util.Set;

public class ShiroRealmUtils {
    private ShiroRealmUtils() {
    }

    private static MyAuthorizingRealm realm = SpringContextHolder.getBean("myShiroRealm");

    public static UserInfo getCurrentUser() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }

    public static String getCurrentUserId() {
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
        String res =  hashService.computeHash(request).toHex();
        System.out.println(res);
        return res;
    }

    public static Set<String> getPermissions() {
        return realm == null ? new HashSet<>() : realm.getPermissions();
    }
}
