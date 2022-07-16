package com.zjy.common.utils;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.enums.YesNo;
import com.zjy.baseframework.interfaces.IBaseEnum;
import com.zjy.common.common.EnumBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Description;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumUtils {

    private EnumUtils() {
    }

    private static List<Class<IBaseEnum>> serializeEnumList = new ArrayList<>();

    @SuppressWarnings("restriction")
    public static String getDescription(Enum<?> enu) throws NoSuchFieldException {
        Class<? extends Enum> sc = enu.getClass();

        Field field = sc.getField(enu.toString());
        // field.getAnnotations();
        // 是否有此注解
        // field.isAnnotationPresent(Description.class);
        Description description = field.getAnnotation(Description.class);
        return description.value();
    }

    public static void initAllSerializeEnum() {
        List<String> enumPackages = Arrays.asList(YesNo.class.getPackage().getName(),
                "com.zjy.entity.enums",
                "com.zjy.service.enums");
        List<Class> classList = ReflectionUtils.getProjectClassList(enumPackages);
        initAllSerializeEnum(classList);
    }

    public static void initAllSerializeEnum(List<Class> classList) {
        serializeEnumList = new ArrayList<>();
        for (Class aClass : classList) {
            if (aClass.isEnum() && IBaseEnum.class.isAssignableFrom(aClass) && aClass.isAnnotationPresent(SerializeEnum.class)) {
                serializeEnumList.add((Class<IBaseEnum>) aClass);
            }
        }
    }

    public static Map<String, Map<String, EnumBean>> getEnumBeanList() {
        Map<String, Map<String, EnumBean>> result = new LinkedHashMap<>();
        String keyName;
        for (Class<IBaseEnum> iBaseEnumClass : serializeEnumList) {
            SerializeEnum serializeEnum = iBaseEnumClass.getAnnotation(SerializeEnum.class);
            if (serializeEnum != null && StringUtils.isNotBlank(serializeEnum.value())) {
                keyName = serializeEnum.value();
            } else {
                keyName = iBaseEnumClass.getSimpleName();
            }
            Map<String, EnumBean> map = new LinkedHashMap<>();
            List<IBaseEnum> list = Arrays.stream(iBaseEnumClass.getEnumConstants()).sorted(Comparator.comparing(IBaseEnum::getOrder))
                    .collect(Collectors.toList());
            for (IBaseEnum item : list) {
                //add by jian.tang
                try {
                    if (item.getClass().getField(item.toString()).isAnnotationPresent(Deprecated.class)) continue;
//                    if(item.getClass().getField(item.toString()).isAnnotationPresent( NoShowEnumItem.class )) continue;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                map.put(item.toString(), iBaseEnumToBean(item));
            }
            result.put(keyName, map);
        }
        return result;
    }

    public static EnumBean iBaseEnumToBean(IBaseEnum en) {
        EnumBean bean = new EnumBean();
        bean.setKey(en.toString());
        bean.setValue(en.getValue());
        bean.setCode(en.getCode());
        bean.setName(en.getName());
        bean.setOrder(en.getOrder());
        return bean;
    }

    public static List<Class<IBaseEnum>> getEnumList() {
        if (serializeEnumList.size() == 0) {
            initAllSerializeEnum();
        }
        return serializeEnumList;
    }
}
