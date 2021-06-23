package com.longyou.aihelper.lucene;

import com.alibaba.fastjson.JSON;
import com.longyou.aihelper.mapper.ResponsetableMapper;
import com.longyou.aihelper.model.Responsetable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.RedissonUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AimlIndexService {

  @Autowired
  ResponsetableMapper responsetableMapper;

  @Autowired
  private IndexWriter indexWriter;

  @Autowired
  SearcherManager searcherManager;

  private static String __LASTUPDATETIME_KEY = "system:config:lucene:aiml:lastUpdateTime";

  public void createOrUpdateIndex() throws IOException {

    RLock locker = RedissonUtil.getRedissonClient().getLock("locker.system.config.lucene.aiml.lastUpdateTime");

    try {
      if (!locker.tryLock(3, 60, TimeUnit.MINUTES)) {  // 等待3秒，上锁10分钟后自动解锁
        return;
      }
      Date lastModifyTime = RedissonUtil.single().getValue(__LASTUPDATETIME_KEY);
      final Date indexStartDate = new Date();
      OpenMode openMode = OpenMode.CREATE;
      final Map<String, Object> params = new HashMap<>();
      if (CollectionUtil.single().isNotEmpty(lastModifyTime)) {
        params.put("lastModifyTimeStart", lastModifyTime);
        openMode = OpenMode.APPEND;
      }
      RedissonUtil.single().setValue(__LASTUPDATETIME_KEY, indexStartDate);
      this.createIndex(responsetableMapper.selectByCondition(params), openMode);
    } catch (InterruptedException e) {
      log.info("{}", e);
    } finally {
      if (locker != null && locker.isLocked()) {
        locker.unlock();
      }
    }

  }

  private void createIndex(List<Responsetable> responsetableList, OpenMode openMode) throws IOException {
    for (Responsetable responsetable : responsetableList) {
      Document doc = new Document();
      doc.add(new StringField("id", responsetable.getId() + "", Field.Store.YES));
      doc.add(new TextField("copyfield", responsetable.getCopyfield(), Field.Store.YES));
      doc.add(new TextField("replay", responsetable.getReplay(), Field.Store.YES));
      if (CollectionUtil.single().isNotEmpty(responsetable.getLabel())) {
        doc.add(new TextField("label", responsetable.getLabel(), Field.Store.YES));
      }
      doc.add(new TextField("question", responsetable.getQuestion(), Field.Store.YES));
      this.indexDoc(doc, openMode);
    }
    indexWriter.commit();
  }

  public void indexDoc(Document doc, OpenMode openMode) {
    try {
      // 更新索引，他的工作原理就是先将索引文件中该id的数据删除掉，然后在将数据库中该id的数据索引一遍
      if (openMode == OpenMode.APPEND) {
        indexWriter.updateDocument(new Term("id", doc.get("id")), doc);
      } else if (openMode == OpenMode.CREATE) {// 创建索引
        indexWriter.addDocument(doc);
      }
    } catch (CorruptIndexException e) {
      log.error("{}", e);
    } catch (IOException e) {
      log.error("{}", e);
    }
  }

  public List<Document> query(final String fieldName, final String keyword) throws IOException, ParseException {
    IndexSearcher indexSearcher = searcherManager.acquire();
//    TermQuery query = new TermQuery(new Term(fieldName, keyword));
    Analyzer analyzer = new SmartChineseAnalyzer();
    QueryParser queryParser = new QueryParser("name", analyzer);
    Query query = queryParser.parse(fieldName+":"+keyword);
    TopDocs topDocs = indexSearcher.search(query, 10);
    log.info("{},{} totalHits = {}", fieldName, keyword, topDocs.totalHits);
    List<Document> docs = new ArrayList<>();
    for (ScoreDoc scoreDocs : topDocs.scoreDocs) {
      Document targetDoc = indexSearcher.doc(scoreDocs.doc);
      docs.add(targetDoc);
    }
    return docs;
  }

  public List<String> queryReplayList(String fieldName, String keyword) throws IOException, ParseException {
    List<Document> docs = query(fieldName, keyword);
    List<String> results = new ArrayList<>();

    for (Document document : docs) {
      results.addAll(Arrays.asList(document.getValues("replay")));
    }

    return results;
  }

}
