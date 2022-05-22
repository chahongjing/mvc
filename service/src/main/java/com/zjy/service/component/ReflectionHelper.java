package com.zjy.service.component;

import com.zjy.baseframework.enums.YesNo;
import com.zjy.entity.enums.UserStatus;
import com.zjy.service.enums.RedisOpType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chahongjing on 2017/10/8.
 */
public class ReflectionHelper {
    static List<Class> allClassList = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(ReflectionHelper.class);

    public static Class getClass(Class clazz) {
        Type superClass = clazz.getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs != null && typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                clazz = (Class) typeArgs[0];
            }
        }

        // 获取方法，后面的参数为获取的方法的签名参数类型
        // Method method = clazz.getDeclaredMethod("方法名称", HttpServletRequest.class, HttpServletResponse.class);
        return clazz;
    }

    public static List<Class> getProjectClassList() {
        if (CollectionUtils.isEmpty(allClassList)) {
            List<String> enumPackages = Arrays.asList(YesNo.class.getPackage().getName(),
                    UserStatus.class.getPackage().getName(),
                    RedisOpType.class.getPackage().getName());
            for (String enumPackage : enumPackages) {
                for (String pack : enumPackage.split(",|;")) {
                    // 枚举所在的包
                    allClassList.addAll(ReflectionHelper.getClassFromPackage(pack.replace('.', '/')));
                }
            }
        }
        return allClassList;
    }

    /**
     * 返回路径下所有class
     *
     * @param packagePath 根路径
     * @return
     * @throws IOException
     */
    public static List<Class> getClassFromPackage(String packagePath) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(ReflectionHelper.class.getClassLoader());
        Resource[] resources = new Resource[0];
        try {
            resources = resourceResolver.getResources("classpath*:" + packagePath + "/**/*.class");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Class> classList = new ArrayList<>();
        String className;
        for (Resource resource : resources) {
            // 取得Class
            try {
                className = preserveSubpackageName(resource.getURI().toString());
                className = className.replace('/', '.').replaceAll("\\.class", StringUtils.EMPTY);
                if (className.indexOf("BaseTestCase") > -1) continue;
                Class<?> aClass = Class.forName(className, false, ReflectionHelper.class.getClassLoader());
                classList.add(aClass);
            } catch (Exception e) {
                logger.error("获取枚举类信息失败！", e);
            }
        }
        return classList;
    }

    public static String preserveSubpackageName(String a) {
        a = a.replace("\\", "/");
        int i = a.lastIndexOf("jar!");
        if (i > -1) {
            return a.substring(i + 5);
        } else {
            return a.substring(a.indexOf("classes") + 8);
        }
    }

    //得到泛型类T
    private Class getMyClass() {
        //返回表示此 Class 所表示的实体类的 直接父类 的 Type。注意，是直接父类
        //这里type结果是 com.dfsj.generic.GetInstanceUtil<com.dfsj.generic.User>
        Type type = getClass().getGenericSuperclass();

        // 判断 是否泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象的数组.
            // 当有多个泛型类时，数组的长度就不是1了
            Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
            return (Class) ptype[0];  //将第一个泛型T对应的类返回（这里只有一个）
        } else {
            return Object.class;//若没有给定泛型，则返回Object类
        }
    }

    public static Object getValue(Object target, String fieldName) {
        Class<?> clazz = target.getClass();
        String[] fs = fieldName.split("\\.");
        try {
            for (int i = 0; i < fs.length - 1; i++) {
                Field f = clazz.getDeclaredField(fs[i]);
                f.setAccessible(true);
                target = f.get(target);
                clazz = target.getClass();
            }
            Field f = clazz.getDeclaredField(fs[fs.length - 1]);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
