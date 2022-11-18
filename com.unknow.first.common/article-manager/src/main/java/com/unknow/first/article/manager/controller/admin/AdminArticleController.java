package com.unknow.first.article.manager.controller.admin;


import com.unknow.first.article.manager.service.ArticleService;
import com.unknow.first.article.manager.vo.ArticleParamVO;
import com.unknow.first.article.manager.mapper.Article;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.juna.ruiqi.api.CommonPage;
import com.juna.ruiqi.api.CommonParam;
import com.juna.ruiqi.api.CommonResult;
import com.juna.ruiqi.constants.OperationLogConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.AuthLog;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "后台管理-文章管理", tags = "admin:文档管理")
@Validated
@RestController
@RequestMapping("/admin/article")
@SystemResource(path = "/admin/article")
@Slf4j
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "管理员获取文章或分类列表", notes = "支持模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "为空或0查询分类，大于0查询对应的文章", name = "parentId", paramType = "query",example = "0"),
            @ApiImplicitParam(value = "0禁用；1启用", name = "status", paramType = "query",example = "0")
    })
    @SystemResource(value = "/admin-articleList", description = "管理员获取文章或分类列表", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/articleList")
    public CommonResult<CommonPage<Article>> articleList(Integer nodeType, Long parentId, String classCode, String title, String articleContent, Integer languageType, Integer status, @Validated CommonParam param) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(nodeType != null, Article::getNodeType, nodeType)
                .eq(!StringUtils.isEmpty(classCode), Article::getClassCode, classCode)
                .eq(parentId != null, Article::getParentId, parentId)
                .eq(!StringUtils.isEmpty(title), Article::getTitle, title)
                .eq(!StringUtils.isEmpty(articleContent), Article::getArticleContent, articleContent)
                .eq(languageType != null, Article::getLanguageType, languageType)
                .eq(status != null, Article::getStatus, status);
        PageHelper.startPage(param.getPage(), param.getLimit());
        List<Article> list = articleService.list(queryWrapper);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation(value = "根据id获取文章或列表详细信息")
    @SystemResource(value = "/admin-get", description = "根据id获取文章或列表详细信息", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/get")
    public CommonResult<Article> get(Integer id) {
        return CommonResult.success(articleService.getById(id));
    }

    @AuthLog(bizType = OperationLogConstants.OPERATE_LOG_BIZ_TYPE_SYSTEM, desc = "添加文章或分类")
    @ApiOperation(value = "添加文章或分类")
    @SystemResource(value = "/admin-addArticle", description = "添加文章或分类", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/addArticle")
    public CommonResult addArticle(@Validated @RequestBody ArticleParamVO vo) throws BusinessException {
        return articleService.addArticle(vo);
    }

    @AuthLog(bizType = OperationLogConstants.OPERATE_LOG_BIZ_TYPE_SYSTEM, desc = "修改文章或分类")
    @ApiOperation(value = "修改文章或分类")
    @SystemResource(value = "/admin-updateArticle", description = "修改文章或分类", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updateArticle")
    public CommonResult updateArticle(@Validated @RequestBody ArticleParamVO vo) throws BusinessException {
        return articleService.updateArticle(vo);
    }


}
