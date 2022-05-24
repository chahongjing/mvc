package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.dao.vo.MenuVo;
import com.zjy.entity.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MenuDao extends BaseDao<Menu> {
    List<MenuVo> query(Menu menu);
}
