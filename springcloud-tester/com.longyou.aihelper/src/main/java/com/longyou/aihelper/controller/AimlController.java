package com.longyou.aihelper.controller;

import com.longyou.aihelper.aiml.service.impl.AskToAIML;
import com.longyou.aihelper.context.ChartManager;
import com.longyou.aihelper.lucene.AimlIndexService;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alice")
public class AimlController {

  @Autowired
  private AskToAIML askToAIML;

  @GetMapping("/askToAIML")
  public ResponseResult askToAIML(@RequestParam("keyWord") String keyWord) throws Exception {
    ResponseResult responseResult = ResponseResult.createSuccessResult();
    responseResult.addData(askToAIML.response(keyWord));
    return responseResult;
  }

  @Autowired
  AimlIndexService aimlIndexService;

  @GetMapping("/ask/{fieldName}")
  public ResponseResult askToLucene(@PathVariable("fieldName") String fieldName, @RequestParam("keyWord") String keyWord) throws Exception {
    ResponseResult responseResult = ResponseResult.createSuccessResult();
    responseResult.addData(aimlIndexService.queryList(fieldName, keyWord, "question", "replay","label"));
    return responseResult;
  }

  @GetMapping("/askBot")
  public ResponseResult askBot(@RequestParam("keyWord") String keyWord) throws Exception {
    ResponseResult responseResult = ResponseResult.createSuccessResult();
    responseResult.addData(ChartManager.getInstance().response(keyWord));
    return responseResult;
  }
}
