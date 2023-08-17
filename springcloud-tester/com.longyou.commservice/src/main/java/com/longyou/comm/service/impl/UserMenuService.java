package com.longyou.comm.service.impl;

import com.longyou.comm.admin.service.IMenuService;
import com.longyou.comm.service.IUserMenuService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginTypeConstant.LoginTypeEnum;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.mybatisplus.vo.JavaBeanResultMap;
import org.cloud.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        // 只有类型为后台管理员的用户才可以访问菜单查询接口
        Assert.isTrue(LoginTypeEnum.LOGIN_BY_ADMIN_USER.userType.equals(loginUserDetails.getUserType()),"system.error.menu.only.adminUser");
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
        for (JavaBeanResultMap menu : menuItems) {
            List<JavaBeanResultMap> childMenuList = getChildMenuItem(menu);
            if (CollectionUtil.single().isNotEmpty(childMenuList)) {
                this.removeNoChildMenu(childMenuList);
            } else {
                final int showType = Integer.parseInt(menu.get("showType") == null ? "2" : menu.get("showType").toString());
                if (showType == 0) {
                    noChildMenuList.add(menu);
                }
            }
        }
        if (CollectionUtil.single().isNotEmpty(noChildMenuList)) {
            log.info("noChildMenuList=={}", noChildMenuList);
            menuItems.removeAll(noChildMenuList);
        }
    }

    private void getCurrentUserMenu(List<JavaBeanResultMap> menuItems, Set<String> userFunctions) {
        List<JavaBeanResultMap> noAuthMenuList = new ArrayList<>();
        for (JavaBeanResultMap menu : menuItems) {
            List<JavaBeanResultMap> childMenuList = getChildMenuItem(menu);
            final int showType = Integer.parseInt(menu.get("showType") == null ? "2" : menu.get("showType").toString());
            if (CollectionUtil.single().isNotEmpty(childMenuList)) {
                if (showType == 1 && !userFunctions.contains(menu.get("functionResourceCode"))) {
                    noAuthMenuList.add(menu);
                } else {
                    this.getCurrentUserMenu(childMenuList, userFunctions);
                }
            } else {
                // 0：有子菜单时显示 1：有权限时显示 2：任何时候都显示
                if (showType == 1 && !userFunctions.contains(menu.get("functionResourceCode"))) {
                    noAuthMenuList.add(menu);
                }
            }
        }
        if (CollectionUtil.single().isNotEmpty(noAuthMenuList)) {
            log.info("noAuthMenuList=={}", noAuthMenuList);
            menuItems.removeAll(noAuthMenuList);
        }
    }

    private static List<JavaBeanResultMap> getChildMenuItem(JavaBeanResultMap menu) {
        if (!menu.containsKey(IMenuService._MENU_CHILD_KEY) || CollectionUtil.single().isEmpty(menu.get(IMenuService._MENU_CHILD_KEY))) {
            return null;
        }
        return (List<JavaBeanResultMap>) menu.get(IMenuService._MENU_CHILD_KEY);
    }
}
