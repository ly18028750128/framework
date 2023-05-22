package com.unknow.first.article.manager.controller;

import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.api.common.CommonResult;
import com.unknow.first.article.manager.service.ArticleService;
import com.unknow.first.article.manager.vo.ArticleResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Api(description = "文章管理", tags = "文章管理")
@Validated
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "根据code获取文章列表", notes = "code列举：{公告:NOTICE,项目专场:PROJECT,新手指南:HELP_FAQ}")
    @ApiImplicitParam(value = "分类编码", name = "code", required = true, dataType = "String", paramType = "query")
    @RequestMapping(method = RequestMethod.GET, value = "/getArticleList")
    public CommonResult<CommonPage<ArticleResultVO>> getArticleList(
            Integer id,
            @NotNull String code,
            @NotNull Integer languageType,
            @Validated CommonParam param) {
        CommonPage<ArticleResultVO> page = articleService.getArticleListByParentCode(id, code, languageType, param);
        return CommonResult.success(page);
    }

}
