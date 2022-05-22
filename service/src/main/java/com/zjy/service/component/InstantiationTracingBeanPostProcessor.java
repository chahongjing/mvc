package com.zjy.service.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ApplicationEvent> {

    /**
     * spring容器加载完成后事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //root application context 没有parent，他就是老大.
        if (((ContextRefreshedEvent) event).getApplicationContext().getParent() != null) return;
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        if (event instanceof ContextClosedEvent) {
            log.info(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextRefreshedEvent) {
            // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            // 获取所有类型
            List<Class> classList = ReflectionHelper.getProjectClassList();
            // 初始化要序列化的枚举
            EnumHelper.initAllSerializeEnum(classList);
            log.info(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextStartedEvent) {
            log.info(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextStoppedEvent) {
            log.info(event.getClass().getSimpleName() + " 事件已发生！");
        } else {
            log.info("有其它事件发生:" + event.getClass().getName());
        }
    }
}
