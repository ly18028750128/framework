package com.longyou.comm.admin.service;

import org.cloud.model.TFrameUser;

public interface IUserAdminService {
    public long saveOrUpdate(TFrameUser tFrameUser) throws Exception;
}
