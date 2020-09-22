package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameUserRef;
import org.cloud.model.TFrameUser;
import org.cloud.vo.QueryParamVO;


public interface TFrameUserRefMapper {

    int create(TFrameUserRef userRef);

    Page<TFrameUserRef> select(QueryParamVO queryParamVO);

    int nameCount(String name);

    int update(TFrameUserRef userRef);

    int delete(Long id);

    TFrameUserRef selectUserRefList(String userId);
}
