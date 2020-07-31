package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IMenuService;
import org.cloud.core.redis.RedisUtil;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.utils.CollectionUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.JavaBeanResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MenuService implements IMenuService {

    @Autowired
    RedisUtil redisUtil;

    private final String baseMenuQuerySql = "select t_frame_menu.*,#{" + _MENU_CURR_LEVEL_KEY + ",jdbcType=INTEGER} AS current_Level from t_frame_menu where status=1";

    @Override
    public List<JavaBeanResultMap<Object>> listAllMenu(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public List<JavaBeanResultMap<Object>> listAllTreeMenu(Integer parentId, Integer maxLevel) throws Exception {
        DynamicSqlQueryParamsVO queryParamsVO = new DynamicSqlQueryParamsVO();
        queryParamsVO.setSorts("seq_no");
        String sqlContext;
        queryParamsVO.getParams().put("currentLevel", 1);
        if (parentId == null) {
            sqlContext = baseMenuQuerySql + " and parent_menu_id is null";
        } else {
            sqlContext = baseMenuQuerySql + " and menu_id = #{parentId,jdbcType=INTEGER}";
            queryParamsVO.getParams().put("parentId", parentId);
        }
        List<JavaBeanResultMap<Object>> result = DynamicSqlUtil.single().listDataBySqlContext(sqlContext, queryParamsVO);
        this.setChildMenu(result, maxLevel);

        if (parentId == null) {
            redisUtil.set(_ALL_MENUS_CACHE_KEY, result, 60 * 60 * 24L);   //缓存一天
        }
        return result;
    }

    @Override
    public List<JavaBeanResultMap<Object>> getAllSystemMenuFromCache() throws Exception {
        List<JavaBeanResultMap<Object>> result = redisUtil.get(_ALL_MENUS_CACHE_KEY);
        if (result == null) {
            return this.listAllTreeMenu(null, 10);
        }
        return result;
    }

    /**
     * 递归查找全部菜单
     *
     * @param parentMenus
     * @throws Exception
     */
    private void setChildMenu(List<JavaBeanResultMap<Object>> parentMenus, Integer maxLevel) throws Exception {
        DynamicSqlQueryParamsVO queryParamsVO = new DynamicSqlQueryParamsVO();
        queryParamsVO.setSorts("seq_no");
        for (JavaBeanResultMap<Object> menuItem : parentMenus) {
            Integer currentLevel = Integer.parseInt(menuItem.get(_MENU_CURR_LEVEL_KEY).toString());
            if (currentLevel.intValue() > maxLevel.intValue()) {
                continue;
            }
            queryParamsVO.getParams().put("parentId", menuItem.get("menuId"));
            queryParamsVO.getParams().put(_MENU_CURR_LEVEL_KEY, currentLevel + 1);
            String sqlContext = baseMenuQuerySql + " and parent_menu_id = #{parentId,jdbcType=INTEGER}";
            List<JavaBeanResultMap<Object>> childList = DynamicSqlUtil.single().listDataBySqlContext(sqlContext, queryParamsVO);
            if (CollectionUtil.single().isNotEmpty(childList)) {
                menuItem.put(IMenuService._MENU_CHILD_KEY, childList);
                this.setChildMenu(childList, maxLevel);
            }
        }
    }

}
