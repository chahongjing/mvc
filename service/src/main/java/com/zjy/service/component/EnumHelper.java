package com.zjy.service.component;

import com.zjy.baseframework.annotations.SerializeEnum;
import com.zjy.baseframework.interfaces.IBaseEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Description;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class EnumHelper {

    private EnumHelper() {}

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

    public static void initAllSerializeEnum(List<Class> classList) {
        for (Class aClass : classList) {
            if (aClass.isEnum() && IBaseEnum.class.isAssignableFrom(aClass) && aClass.isAnnotationPresent(SerializeEnum.class)) {
                serializeEnumList.add((Class<IBaseEnum>)aClass);
            }
        }
    }

    public static Map<String, Map<String, EnumBean>> getEnumBeanList() {
        Map<String, Map<String, EnumBean>> result = new LinkedHashMap<>();
        String keyName;
        for (Class<IBaseEnum> iBaseEnumClass : serializeEnumList) {
            SerializeEnum serializeEnum = iBaseEnumClass.getAnnotation(SerializeEnum.class);
            if(serializeEnum != null && StringUtils.isNotBlank(serializeEnum.value())) {
                keyName = serializeEnum.value();
            } else {
                keyName = iBaseEnumClass.getSimpleName();
            }
            Map<String, EnumBean> map = new LinkedHashMap<>();
            List<IBaseEnum> list = Arrays.stream(iBaseEnumClass.getEnumConstants()).sorted(Comparator.comparing(IBaseEnum::getOrder))
                    .collect(Collectors.toList());
            for (IBaseEnum item : list) {
                EnumBean bean = new EnumBean();
                bean.setKey(item.toString());
                bean.setValue(item.getValue());
                bean.setCode(item.getCode());
                bean.setName(item.getName());
                bean.setOrder(item.getOrder());
                map.put(item.toString(), bean);
            }
            result.put(keyName, map);
        }
        return result;
    }
}
