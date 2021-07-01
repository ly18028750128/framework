package com.longyou.comm.service.impl;

import com.longyou.comm.admin.service.IMenuService;
import com.longyou.comm.service.IUserMenuService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.vo.JavaBeanResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class UserMenuService implements IUserMenuService {

    @Autowired
    IMenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<JavaBeanResultMap<Object>> listUserTreeMenu() throws Exception {

        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();

        if (loginUserDetails == null) {
            return new ArrayList<>();
        }
        List<JavaBeanResultMap<Object>> allMenu = menuService.getAllSystemMenuFromCache();

        log.info("allMenu=={}", allMenu);

        Set<String> userFunctions = redisUtil
            .hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), CoreConstant.UserCacheKey.FUNCTION.value());
        getCurrentUserMenu(allMenu, userFunctions);

        log.info("userFunctions=={}", userFunctions);

        removeNoChildMenu(allMenu);

        return allMenu;
    }

    /**
     * 重新遍历一次去掉没有子菜单的情况
     *
     * @param menuItems
     */
    private void removeNoChildMenu(List<JavaBeanResultMap<Object>> menuItems) {

        List<JavaBeanResultMap<Object>> noChildMenuList = new ArrayList<>();
        for (JavaBeanResultMap<Object> menu : noChildMenuList) {
            List<JavaBeanResultMap<Object>> childMenuList = (List<JavaBeanResultMap<Object>>) menu.get(IMenuService._MENU_CHILD_KEY);
            if (CollectionUtil.single().isNotEmpty(childMenuList)) {
                this.removeNoChildMenu(childMenuList);
            } else {
                String showType = menu.get("showType") == null ? "2" : menu.get("showType").toString();
                if (Integer.parseInt(showType) == 0) {
                    noChildMenuList.add(menu);
                }
            }
        }
        log.info("noChildMenuList=={}", noChildMenuList);
        menuItems.removeAll(noChildMenuList);
    }

    private void getCurrentUserMenu(List<JavaBeanResultMap<Object>> menuItems, Set<String> userFunctions) {
        List<JavaBeanResultMap<Object>> noAuthMenuList = new ArrayList<>();
        for (JavaBeanResultMap<Object> menu : menuItems) {
            if (menu.containsKey(IMenuService._MENU_CHILD_KEY)) {
                List<JavaBeanResultMap<Object>> childMenuList = (List<JavaBeanResultMap<Object>>) menu.get(IMenuService._MENU_CHILD_KEY);
                this.getCurrentUserMenu(childMenuList, userFunctions);
            } else {
                String showType = menu.get("showType") == null ? "2" : menu.get("showType").toString();
                // 0：有子菜单时显示 1：有权限时显示 2：任何时候都显示
                if (Integer.parseInt(showType) == 1 && !userFunctions.contains(menu.get("functionResourceCode"))) {
                    noAuthMenuList.add(menu);
                }
            }
        }
        log.info("noAuthMenuList=={}", noAuthMenuList);
        menuItems.removeAll(noAuthMenuList);
    }
}
