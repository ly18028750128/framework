package com.longyou.comm.admin.service;

import org.cloud.model.TFrameMenu;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;

import java.util.List;
import java.util.Map;

public interface IMenuService {

    String _MENU_CHILD_KEY = "child";
    String _MENU_CURR_LEVEL_KEY = "currentLevel";
    String _ALL_MENUS_CACHE_KEY = "system:admin:allMenu";

    /**
     * @param params
     * @return
     * @throws Exception
     */
    List<JavaBeanResultMap> listAllMenu(Map<String, Object> params) throws Exception;

    /**
     * @param parentId 父级ID，如果为null，那么查询全部的
     * @param maxLevel 最大层次，防止死循环
     * @param isAll 是否查询全部，如果否那么只查询有效的
     * @return
     * @throws Exception
     */
    List<JavaBeanResultMap> listAllTreeMenu(Integer parentId, Integer maxLevel,Boolean isAll) throws Exception;


    List<JavaBeanResultMap> getAllSystemMenuFromCache() throws Exception;

    /**
     * 根据id更新菜单信息
     *
     * @param tFrameMenu 菜单信息列表，id必传
     * @return 更新的条数
     * @throws Exception 异常将被捕获
     */
    int updateMenuById(TFrameMenu tFrameMenu) throws Exception;

    /**
     * 根据id更新菜单信息
     *
     * @param tFrameMenu 菜单信息列表，id必传
     * @return 更新的条数
     * @throws Exception 异常将被捕获
     */
    int insertMenu(TFrameMenu tFrameMenu) throws Exception;
}
