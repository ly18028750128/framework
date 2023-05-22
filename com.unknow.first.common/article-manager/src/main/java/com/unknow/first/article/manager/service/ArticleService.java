package com.unknow.first.article.manager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.api.common.CommonResult;
import com.unknow.first.article.manager.mapper.Article;
import com.unknow.first.article.manager.vo.ArticleParamVO;
import com.unknow.first.article.manager.vo.ArticleResultVO;
import org.cloud.exception.BusinessException;

public interface ArticleService extends IService<Article> {

//    /**
//     * 获取所有分类
//     *
//     * @return
//     */
//    public List<ArticleResultVO> getClassify();
//
//    /**
//     * 根据语言获取所有分类
//     *
//     * @param languageType
//     * @return
//     */
//    public List<ArticleResultVO> getClassify(int languageType);

    /**
     * 根据分类code获取文章列表
     *
     * @return
     */
    public CommonPage<ArticleResultVO> getArticleListByParentCode(/**CommonConstants.ArticleClassCodeEnum classCodeEnum**/Integer id, String code, Integer languageType, CommonParam param);

//    /**
//     * 根据parentid获取文章列表
//     *
//     * @param parentId
//     * @return
//     */
//    public List<ArticleResultVO> getArticleListByParentId(Integer parentId, CommonParam param);

    /**
     * 添加文章
     *
     * @param vo
     * @return
     */
    public CommonResult addArticle(ArticleParamVO vo) throws BusinessException;

    /**
     * 修改文章
     *
     * @param vo
     * @return
     */
    public CommonResult updateArticle(ArticleParamVO vo) throws BusinessException;
}
