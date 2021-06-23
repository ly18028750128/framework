package com.longyou.aihelper.lucene;

import com.longyou.aihelper.AiHelperApplication;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AiHelperApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class AimlIndexServiceTest {

  @Autowired
  AimlIndexService aimlIndexService;

  @Test
  public void testQuery() throws IOException, ParseException {
//    aimlIndexService.createIndex();

//    try {
//      Thread.sleep(30000L);
//    }catch (Exception e){
//
//    }

    aimlIndexService.query("copyfield","物料");
    aimlIndexService.query("copyfield","支付");
    aimlIndexService.query("id","1");


  }

}