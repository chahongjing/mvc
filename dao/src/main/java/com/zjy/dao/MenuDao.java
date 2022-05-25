package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.MenuVo;
import com.zjy.entity.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface MenuDao extends BaseDao<Menu> {
    List<MenuVo> query(Menu menu);
    Map<String, BigDecimal> queryRepeatCount(@Param("id") Long id, @Param("code") String code);
    List<MenuVo> queryParentList();
    List<MenuVo> queryPageMenuList();
}
