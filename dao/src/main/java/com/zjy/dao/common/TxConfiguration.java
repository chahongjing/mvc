package com.zjy.dao.common;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量对特定方法开启事务
 */
@Aspect
@Component
public class TxConfiguration {
    @Autowired
    private TransactionManager transactionManager;
    //指定事务处理范围
    private static final String POINTCUT_EXPRESSION = "execution(* com.zjy..service..*(..))";

    private final List<String> requiredList = Arrays.asList("insert*", "update*", "delete*", "add*", "save*");

    private final List<String> readOnlyList = Arrays.asList("get*", "select*", "query*", "list*", "find*", "count*");

    @Bean
    public TransactionInterceptor txAdvice() {
//        RuleBasedTransactionAttribute txRequired = new RuleBasedTransactionAttribute();
//        txRequired.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//        txRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        DefaultTransactionAttribute txRequired = new DefaultTransactionAttribute();
        txRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute txReadonly = new DefaultTransactionAttribute();
        txReadonly.setReadOnly(true);
        txReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        txReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Map<String, TransactionAttribute> nameMap = new LinkedHashMap<>();
        requiredList.forEach(r -> nameMap.put(r, txRequired));
        readOnlyList.forEach(r -> nameMap.put(r, txReadonly));
        source.setNameMap(nameMap);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor(@Autowired TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
