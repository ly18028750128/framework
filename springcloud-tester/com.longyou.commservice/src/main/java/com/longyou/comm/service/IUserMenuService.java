package com.longyou.comm.service;

import org.cloud.vo.JavaBeanResultMap;

import java.util.List;

/**
 * 用户菜单服务
 *
 * @author longy
 * @since 2020-05-05
 */
public interface IUserMenuService {
    public List<JavaBeanResultMap<Object>> listUserTreeMenu() throws Exception;
}
