package com.zjy.dao.common.multiDataSource;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.*;

import java.util.*;

/**
 * 批量对特定方法开启事务
 */
@Aspect
@Configuration
public class TxConfiguration {
    @Autowired
    private TransactionManager transactionManager;
    //指定事务处理范围
    private static final String POINTCUT_EXPRESSION = "execution(* com.zjy..service..*(..))";

    private final List<String> requiredList = Arrays.asList("insert*", "update*", "delete*", "add*", "save*", "remove*");

    private final List<String> readOnlyList = Arrays.asList("get*", "select*", "query*", "list*", "find*", "count*");

    @Bean
    public TransactionInterceptor txAdvice() {
        RuleBasedTransactionAttribute txRequired = new RuleBasedTransactionAttribute();
        txRequired.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        txRequired.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txRequired.setTimeout(5);

        DefaultTransactionAttribute txReadonly = new DefaultTransactionAttribute();
        txReadonly.setReadOnly(true);
        txReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        Map<String, TransactionAttribute> nameMap = new LinkedHashMap<>();
        requiredList.forEach(r -> nameMap.put(r, txRequired));
        readOnlyList.forEach(r -> nameMap.put(r, txReadonly));
        source.setNameMap(nameMap);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor(TransactionInterceptor txAdvice) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice);
    }
}
