package com.unknow.first.article.manager.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unknow.first.article.manager.vo.ArticleResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    int updateBatch(List<Article> list);

    int batchInsert(@Param("list") List<Article> list);

    int insertOrUpdate(Article record);

    int insertOrUpdateSelective(Article record);

    List<ArticleResultVO> selectArticleListByParentCode(@Param("id") Integer id, @Param("code") String code, @Param("languageType") Integer languageType);
}