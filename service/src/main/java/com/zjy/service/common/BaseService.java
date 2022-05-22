package com.zjy.service.common;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageHelper;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.entity.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * 公共service
 *
 * @author chahongjing
 * @create 2016-12-10 13:38
 */

@Slf4j
public class BaseService<Dao extends BaseMapper<T>, T> {

    /**
     * 公共dao
     */
    @Autowired
    protected Dao dao;

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    public int add(T entity) {
        log.info("调用add   方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        return dao.insert(entity);
    }

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    public int update(T entity) {
        log.info("调用update   方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        return dao.updateById(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public int delete(String id) {
        log.info("调用delete方法:id: {}", id);
        return dao.deleteById(id);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public T get(Long id) {
        log.info("调用get方法:id: {}", id);
        return dao.selectById(id);
    }

    /**
     * 查询简单列表
     *
     * @param entity
     * @return
     */
    public List<? extends T> queryList(Wrapper<T> entity) {
        log.info("调用queryList方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        return dao.selectList(entity);
    }

    /**
     * 查询简单列表（分页）
     *
     * @param entity
     * @return
     */
    public PageBean<? extends T> queryPageList(PageInfomation pi, Wrapper<T> entity) {
        log.info("调用queryPageList方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        PageHelper.startPage(pi.getPageNum(), pi.getPageSize()).setOrderBy(pi.getOrderBy());
        return new PageBean<>(this.queryList(entity));
    }

    /**
     * 查询复杂列表
     *
     * @param query
     * @return
     */
    public List<? extends T> queryListByMapFilter(Map<String, Object> query) {
        log.info("调用queryListByMapFilter方法:query: {}", JSON.toJSONString(query));
        throw new NotImplementedException();
//        return dao.queryByMapFilter(query);
    }

    /**
     * 查询复杂列表（分页）
     *
     * @param pi
     * @param query
     * @return
     */
    public PageBean<? extends T> queryPageListByMapFilter(PageInfomation pi, Map<String, Object> query) {
        log.info("调用queryPageListByMapFilter方法:PageInfomation: {}\tquery: {}", JSON.toJSONString(pi), JSON.toJSONString(query));
        PageHelper.startPage(pi.getPageNum(), pi.getPageSize()).setOrderBy(pi.getOrderBy());
        return new PageBean<>(this.queryListByMapFilter(query));
    }


    protected static UserInfo getCurrentUser() {
        return ShiroRealmUtils.getCurrentUser();
    }
}
