package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IMenuService;
import com.longyou.comm.mapper.TFrameMenuDao;
import java.util.List;
import java.util.Map;
import org.cloud.core.redis.RedisUtil;
import org.cloud.model.TFrameMenu;
import org.cloud.mybatisplus.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.mybatisplus.vo.DynamicSqlQueryParamsVO;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;
import org.cloud.utils.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuService implements IMenuService {

    @Autowired
    TFrameMenuDao tFrameMenuDao;


    private final String baseMenuQuerySql = "select t_frame_menu.*,#{" + _MENU_CURR_LEVEL_KEY + ",jdbcType=INTEGER} AS current_Level from t_frame_menu ";

    @Override
    public List<JavaBeanResultMap> listAllMenu(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public List<JavaBeanResultMap> listAllTreeMenu(Integer parentId, Integer maxLevel, Boolean isAll) throws Exception {
        DynamicSqlQueryParamsVO queryParamsVO = new DynamicSqlQueryParamsVO();
        queryParamsVO.setSorts("status desc,seq_no");
        String sqlContext;
        queryParamsVO.getParams().put(_MENU_CURR_LEVEL_KEY, 1);
        if (parentId == null) {
            sqlContext = getBaseQuerySQL(isAll) + " and parent_menu_id is null";
        } else {
            sqlContext = getBaseQuerySQL(isAll) + " and menu_id = #{parentId,jdbcType=INTEGER}";
            queryParamsVO.getParams().put("parentId", parentId);
        }
        List<JavaBeanResultMap> result = DynamicSqlUtil.single().listDataBySqlContext(sqlContext, queryParamsVO);
        this.setChildMenu(result, maxLevel, isAll);
        return result;
    }

    @NotNull
    private String getBaseQuerySQL(Boolean isAll) {
        String baseMenuQuerySql = "";
        if (isAll) {
            baseMenuQuerySql = this.baseMenuQuerySql + "where 1=1 ";
        } else {
            baseMenuQuerySql = this.baseMenuQuerySql + "where status=1 ";
        }
        return baseMenuQuerySql;
    }

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<JavaBeanResultMap> getAllSystemMenuFromCache() throws Exception {
        List<JavaBeanResultMap> result = redisUtil.get(_ALL_MENUS_CACHE_KEY);
        if (result == null) {
            result = this.listAllTreeMenu(null, 5, false);
            redisUtil.set(_ALL_MENUS_CACHE_KEY, result, 0L);
        }
        return result;
    }

    @Override
    public int updateMenuById(TFrameMenu tFrameMenu) throws Exception {
        assert tFrameMenu.getMenuId() != null;
        return tFrameMenuDao.updateByPrimaryKeySelective(tFrameMenu);
    }

    @Override
    public int insertMenu(TFrameMenu tFrameMenu) throws Exception {
        assert tFrameMenu.getMenuId() == null;
        return tFrameMenuDao.insertSelective(tFrameMenu);
    }

    /**
     * 递归查找全部菜单
     *
     * @param parentMenus 上线菜单
     * @throws Exception 抛出异常
     */
    private void setChildMenu(List<JavaBeanResultMap> parentMenus, Integer maxLevel, Boolean isAll) throws Exception {
        DynamicSqlQueryParamsVO queryParamsVO = new DynamicSqlQueryParamsVO();
        queryParamsVO.setSorts("seq_no");
        for (JavaBeanResultMap menuItem : parentMenus) {
            int currentLevel = Integer.parseInt(menuItem.get(_MENU_CURR_LEVEL_KEY).toString());
            if (currentLevel > maxLevel) {
                continue;
            }
            queryParamsVO.getParams().put("parentId", menuItem.get("menuId"));
            queryParamsVO.getParams().put(_MENU_CURR_LEVEL_KEY, currentLevel + 1);
            String sqlContext = getBaseQuerySQL(isAll) + " and parent_menu_id = #{parentId,jdbcType=INTEGER}";
            List<JavaBeanResultMap> childList = DynamicSqlUtil.single().listDataBySqlContext(sqlContext, queryParamsVO);
            if (CollectionUtil.single().isNotEmpty(childList)) {
                menuItem.put(IMenuService._MENU_CHILD_KEY, childList);
                this.setChildMenu(childList, maxLevel, isAll);
            }
        }
    }


}
