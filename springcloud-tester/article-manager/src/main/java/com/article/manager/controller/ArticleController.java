package com.article.manager.controller;

import com.article.manager.service.ArticleService;
import com.article.manager.vo.ArticleResultVO;
import com.juna.ruiqi.api.CommonPage;
import com.juna.ruiqi.api.CommonParam;
import com.juna.ruiqi.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "文章管理", tags = "ArticleController")
@Validated
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "根据code获取文章列表", notes = "code列举：{公告:NOTICE,项目专场:PROJECT,新手指南:HELP_FAQ}")
    @ApiImplicitParam(value = "分类编码", name = "code", required = true, dataType = "String", paramType = "query")
    @RequestMapping(method = RequestMethod.GET, value = "/getArticleList")
    public CommonResult<CommonPage<ArticleResultVO>> getArticleList(@NotNull String code,
                                                                    @NotNull Integer languageType,
                                                                    @Validated CommonParam param) {
        CommonPage<ArticleResultVO> page = articleService.getArticleListByParentCode(code, languageType, param);
        return CommonResult.success(page);
    }

}
