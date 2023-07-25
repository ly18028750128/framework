package com.longyou.comm.service.impl;

import com.alibaba.fastjson.JSON;
import com.longyou.comm.admin.service.IMenuService;
import com.longyou.comm.service.IUserMenuService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;
import org.cloud.utils.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMenuService implements IUserMenuService {

    @Autowired
    IMenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<JavaBeanResultMap> listUserTreeMenu() throws Exception {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        if (loginUserDetails == null) {
            return new ArrayList<>();
        }
        List<JavaBeanResultMap> allMenu = menuService.getAllSystemMenuFromCache();
        Set<String> userFunctions = redisUtil.hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
            CoreConstant.UserCacheKey.FUNCTION.value());
        if (userFunctions == null) {
            userFunctions = new HashSet<>();
        }
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
    private void removeNoChildMenu(List<JavaBeanResultMap> menuItems) {
        List<JavaBeanResultMap> noChildMenuList = new ArrayList<>();
        for (JavaBeanResultMap menu : noChildMenuList) {
            List<JavaBeanResultMap> childMenuList = getJavaBeanResultMaps(menu);
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

    private void getCurrentUserMenu(List<JavaBeanResultMap> menuItems, Set<String> userFunctions) {
        List<JavaBeanResultMap> noAuthMenuList = new ArrayList<>();
        for (JavaBeanResultMap menu : menuItems) {
            List<JavaBeanResultMap> childMenuList = getJavaBeanResultMaps(menu);
            if (CollectionUtil.single().isNotEmpty(childMenuList)) {
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

    private static List<JavaBeanResultMap> getJavaBeanResultMaps(JavaBeanResultMap menu) {
        if (!menu.containsKey(IMenuService._MENU_CHILD_KEY) || CollectionUtil.single().isEmpty(menu.get(IMenuService._MENU_CHILD_KEY))) {
            return null;
        }
        String childStr = JSON.toJSONString(menu.get(IMenuService._MENU_CHILD_KEY));
        return JSON.parseArray(childStr, JavaBeanResultMap.class);
    }
}
