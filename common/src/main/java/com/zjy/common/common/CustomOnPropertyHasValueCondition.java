package com.zjy.common.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 自定义condition
 */
public class CustomOnPropertyHasValueCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(CustomConditionalOnPropertyHasValue.class.getName());
        String propName = (String) annotationAttributes.get("propName");
        boolean hasValue = (boolean) annotationAttributes.get("hasValue");
        String configValue = conditionContext.getEnvironment().getProperty(propName);
        return hasValue == StringUtils.isNotBlank(configValue);
    }
}
