package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameUserRef;
import org.apache.ibatis.annotations.Param;
import org.cloud.vo.QueryParamVO;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface TFrameUserRefMapper {
    int create(TFrameUserRef userRef);

    Page<TFrameUserRef> select(QueryParamVO queryParamVO);

    int update(TFrameUserRef userRef);

    int delete(Long id);

    List<TFrameUserRef> selectUserRefList(Long userId);

    TFrameUserRef selectUserRefByAttributeName(@NotNull @Param("userId") Long userId, @NotNull @Param("attributeName") String AttributeName);
}
