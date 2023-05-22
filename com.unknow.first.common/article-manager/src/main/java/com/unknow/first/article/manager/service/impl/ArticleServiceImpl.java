package com.unknow.first.article.manager.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.api.common.CommonResult;
import com.unknow.first.article.manager.constants.ArticleConstants;
import com.unknow.first.article.manager.mapper.Article;
import com.unknow.first.article.manager.mapper.ArticleMapper;
import com.unknow.first.article.manager.service.ArticleService;
import com.unknow.first.article.manager.vo.ArticleParamVO;
import com.unknow.first.article.manager.vo.ArticleResultVO;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public CommonPage<ArticleResultVO> getArticleListByParentCode(Integer id, String code, Integer languageType, CommonParam param) {
        PageHelper.startPage(param.getPage(), param.getLimit());
        List<ArticleResultVO> list = getBaseMapper().selectArticleListByParentCode(id, code, languageType);
        return CommonPage.restPage(list);
    }

    @Override
    public CommonResult addArticle(ArticleParamVO vo) throws BusinessException {
        if (vo.getStatus() != 1
                && vo.getStatus() != 0) {
            throw new BusinessException("状态不合法");
        }
        Article article = new Article();
        if (vo.getNodeType() == ArticleConstants.ArticleNodeTypeEnum.CLASS.getType()) {
            if (StringUtils.isEmpty(vo.getClassCode())) {
                throw new BusinessException("分类编码不能为空");
            }
            article.setLanguageType(vo.getLanguageType());
            article.setClassCode(vo.getClassCode());
        } else if (vo.getNodeType() == ArticleConstants.ArticleNodeTypeEnum.ARTICLE.getType()) {
            vo.setClassCode(null);
            if (vo.getParentId() <= 0) {
                throw new BusinessException("分类id不能小于等于0");
            }
            Article parentArticle = getBaseMapper().selectById(vo.getParentId());
            if (parentArticle == null || parentArticle.getNodeType() != ArticleConstants.ArticleNodeTypeEnum.CLASS.getType()) {
                throw new BusinessException("分类不存在");
            }
            article.setParentId(vo.getParentId());
            article.setLanguageType(parentArticle.getLanguageType());
            if (StringUtils.isEmpty(vo.getSubTitle())) {
                throw new BusinessException("副标题不能为空");
            }
            article.setSubTitle(vo.getSubTitle());
            if (StringUtils.isEmpty(vo.getArticleContent())) {
                throw new BusinessException("文章内容不能为空");
            }
            article.setArticleContent(vo.getArticleContent());
            article.setImage(vo.getImage());
            article.setArticleAuthor(vo.getArticleAuthor());

        } else {
            throw new BusinessException("类型不合法");
        }
        article.setStatus(vo.getStatus());
        article.setNodeType(vo.getNodeType());
        article.setArticleSort(vo.getArticleSort());
        article.setTitle(vo.getTitle());
        Date date = new Date();
        article.setCreateTime(date);
        article.setUpdateTime(date);
        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        article.setCreateBy(userDetails.getUsername());
        article.setUpdateBy(userDetails.getUsername());
        if (getBaseMapper().insert(article) > 0) {
            return CommonResult.success();
        }
        throw new BusinessException("添加失败");
    }

    @Override
    public CommonResult updateArticle(ArticleParamVO vo) throws BusinessException {
        if (vo.getId() == null || vo.getId() <= 0) {
            throw new BusinessException("id不能为空");
        }
        Article tArticle = getBaseMapper().selectById(vo.getId());
        if (tArticle == null) {
            throw new BusinessException("文章不存在");
        }
        if (vo.getStatus() != ArticleConstants.StatusEnum.NORMAL.getStatus()
                && vo.getStatus() != 0) {
            throw new BusinessException("状态不合法");
        }
        Article article = new Article();
        if (tArticle.getNodeType() == ArticleConstants.ArticleNodeTypeEnum.CLASS.getType()) {

        } else if (tArticle.getNodeType() == ArticleConstants.ArticleNodeTypeEnum.ARTICLE.getType()) {
            if (vo.getParentId() <= 0) {
                throw new BusinessException("分类id不能小于等于0");
            }
            Article parentArticle = getBaseMapper().selectById(vo.getParentId());
            if (parentArticle == null || parentArticle.getNodeType() != ArticleConstants.ArticleNodeTypeEnum.CLASS.getType()) {
                throw new BusinessException("分类不存在");
            }
            if (StringUtils.isEmpty(vo.getSubTitle())) {
                throw new BusinessException("副标题不能为空");
            }
            article.setSubTitle(vo.getSubTitle());
            if (StringUtils.isEmpty(vo.getArticleContent())) {
                throw new BusinessException("文章内容不能为空");
            }
            article.setArticleContent(vo.getArticleContent());
            article.setImage(vo.getImage());
            article.setArticleAuthor(vo.getArticleAuthor());
        }
        article.setId(vo.getId());
        article.setStatus(vo.getStatus());
        article.setArticleSort(vo.getArticleSort());
        article.setTitle(vo.getTitle());
        Date date = new Date();
        article.setUpdateTime(date);
        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        article.setUpdateBy(userDetails.getUsername());
        if (getBaseMapper().updateById(article) > 0) {
            return CommonResult.success();
        }
        throw new BusinessException("修改失败");
    }
}
