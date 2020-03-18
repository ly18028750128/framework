package com.longyou.comm.admin.service;

import org.cloud.model.TFrameUser;

public interface IUserAdminService {
    public int saveOrUpdate(TFrameUser tFrameUser) throws Exception;
}
