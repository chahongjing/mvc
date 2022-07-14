package com.zjy.service.component;

import com.zjy.baseframework.enums.YesNo;
import com.zjy.common.SpringContextHolder;
import com.zjy.common.shiro.IUserService;
import com.zjy.common.shiro.MyAuthorizingRealm;
import com.zjy.entity.enums.UserStatus;
import com.zjy.common.utils.EnumUtils;
import com.zjy.common.utils.ReflectionHelper;
import com.zjy.service.enums.RedisOpType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
@Slf4j
@Component
public class InitEventListener implements ApplicationListener<ApplicationEvent> {

    /**
     * spring容器加载完成后事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //root application context 没有parent，他就是老大.
        if (event instanceof ContextRefreshedEvent && ((ContextRefreshedEvent) event).getApplicationContext().getParent() != null)
            return;
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        if (event instanceof ContextClosedEvent) {
            log.debug(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextRefreshedEvent) {
            // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
            // 获取所有类型
            List<String> enumPackages = Arrays.asList(YesNo.class.getPackage().getName(),
                    UserStatus.class.getPackage().getName(),
                    RedisOpType.class.getPackage().getName());
            List<Class> classList = ReflectionHelper.getProjectClassList(enumPackages);
            // 初始化要序列化的枚举
            EnumUtils.initAllSerializeEnum(classList);
            MyAuthorizingRealm realm = SpringContextHolder.getBean(MyAuthorizingRealm.class);
            realm.setIUserService(SpringContextHolder.getBean(IUserService.class));
            log.debug(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextStartedEvent) {
            log.debug(event.getClass().getSimpleName() + " 事件已发生！");
        } else if (event instanceof ContextStoppedEvent) {
            log.debug(event.getClass().getSimpleName() + " 事件已发生！");
        } else {
            log.debug("有其它事件发生:" + event.getClass().getName());
        }
    }
}
