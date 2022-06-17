package com.unknow.first.article.manager.service;

import com.unknow.first.article.manager.vo.ArticleParamVO;
import com.unknow.first.article.manager.vo.ArticleResultVO;
import com.unknow.first.article.manager.mapper.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juna.ruiqi.api.CommonPage;
import com.juna.ruiqi.api.CommonParam;
import com.juna.ruiqi.api.CommonResult;
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
    public CommonPage<ArticleResultVO> getArticleListByParentCode(/**CommonConstants.ArticleClassCodeEnum classCodeEnum**/String code, Integer languageType, CommonParam param);

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
