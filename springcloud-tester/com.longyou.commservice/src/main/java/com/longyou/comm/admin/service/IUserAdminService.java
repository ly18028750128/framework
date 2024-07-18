package com.longyou.comm.admin.service;

import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.model.TFrameUser;
import org.springframework.transaction.annotation.Transactional;

public interface IUserAdminService {

    @AuthLog(bizType = "user.admin.saveOrUpdate", desc = "修改用户信息", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    @Transactional(rollbackFor = Exception.class)
    Long saveOrUpdate(TFrameUser tFrameUser) throws Exception;

    @AuthLog(bizType = "user.admin.resetPassword", desc = "重置用户密码", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    @Transactional(rollbackFor = Exception.class)
    Long resetPassword(Long userId) throws Exception;
}
