package com.longyou.comm.service;

import org.cloud.mybatisplus.vo.JavaBeanResultMap;

import java.util.List;

/**
 * 用户菜单服务
 *
 * @author longy
 * @since 2020-05-05
 */
public interface IUserMenuService {
    List<JavaBeanResultMap> listUserTreeMenu() throws Exception;
}
