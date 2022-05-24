package com.zjy.service.common;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.zjy.common.shiro.ShiroRealmUtils;
import com.zjy.dao.common.BaseDao;
import com.zjy.entity.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 公共service
 *
 * @author chahongjing
 * @create 2016-12-10 13:38
 */

@Slf4j
public class BaseServiceImpl<Dao extends BaseDao<T>, T> implements BaseService<T> {

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
    public int insert(T entity) {
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
    public int delete(Long id) {
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
    public List<? extends T> queryListBase(T entity) {
        log.info("调用queryList方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        return dao.query(entity);
    }

    /**
     * 查询简单列表（分页）
     *
     * @param entity
     * @return
     */
    public PageBean<? extends T> queryPageListBase(PageInfomation pi, T entity) {
        log.info("调用queryPageList方法:{}: {}", entity.getClass().getName(), JSON.toJSONString(entity));
        PageHelper.startPage(pi.getPageNum(), pi.getPageSize()).setOrderBy(pi.getOrderBy());
        return new PageBean<>(this.queryListBase(entity));
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
