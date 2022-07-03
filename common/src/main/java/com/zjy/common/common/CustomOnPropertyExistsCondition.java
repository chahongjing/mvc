package com.zjy.common.common;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * 自定义condition
 */
public class CustomOnPropertyExistsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(CustomConditionalOnPropertyExists.class.getName());
        String propName = (String) annotationAttributes.get("propName");
        boolean exists = (boolean) annotationAttributes.get("exists");
        boolean configExists = conditionContext.getEnvironment().containsProperty(propName);
        return exists == configExists;
    }
}
