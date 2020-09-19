package com.longyou.comm.mapper;

import com.longyou.comm.model.TFrameUserRef;
import org.cloud.vo.ResponseResult;


public interface TFrameUserRefMapper {
    int create(TFrameUserRef userRef);

    TFrameUserRef select(Long id);

    int nameCount(String name);

    int update(TFrameUserRef userRef);

    int delete(Long id);
}
