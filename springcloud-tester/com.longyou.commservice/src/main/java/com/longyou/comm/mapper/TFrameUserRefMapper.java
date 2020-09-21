package com.longyou.comm.mapper;

import com.longyou.comm.model.TFrameUserRef;
import org.cloud.model.TFrameUser;



public interface TFrameUserRefMapper {

    int create(TFrameUserRef userRef);

    TFrameUserRef select(Long userId);

    int nameCount(String name);

    int update(TFrameUserRef userRef);

    int delete(Long id);

    TFrameUser selectUserRefList(Long userId);
}
