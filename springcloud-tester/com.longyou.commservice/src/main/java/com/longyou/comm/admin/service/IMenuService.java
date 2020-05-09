package com.longyou.comm.admin.service;

import org.cloud.vo.JavaBeanResultMap;

import java.util.List;
import java.util.Map;

public interface IMenuService {

    public final static String _MENU_CHILD_KEY = "child";
    String _MENU_CURR_LEVEL_KEY = "currentLevel";
    public final static String _ALL_MENUS_CACHE_KEY = "system:admin:allMenu";

    /**
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<JavaBeanResultMap<Object>> listAllMenu(Map<String,Object> params) throws Exception;

    /**
     *
     * @param parentId 父级ID，如果为null，那么查询全部的
     * @param maxLevel 最大层次，防止死循环
     * @return
     * @throws Exception
     */
    public List<JavaBeanResultMap<Object>> listAllTreeMenu(Integer parentId,Integer maxLevel) throws Exception;

    public List<JavaBeanResultMap<Object>> getAllSystemMenuFromCache() throws Exception;
}
