package com.article.manager.mapper;


import com.article.manager.vo.ArticleResultVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends BaseMapper<Article> {
    int updateBatch(List<Article> list);

    int batchInsert(@Param("list") List<Article> list);

    int insertOrUpdate(Article record);

    int insertOrUpdateSelective(Article record);

    List<ArticleResultVO> selectArticleListByParentCode(@Param("code") String code, @Param("languageType") Integer languageType);
}