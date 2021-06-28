package com.longyou.aihelper.aiml.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.cloud.exception.BusinessException;
import org.cloud.utils.RedissonUtil;
import org.redisson.api.RLock;

@Slf4j
public class GossipLoad {

  private InputStream inputStream = null;
  private BufferedReader bufReader = null;
  private OutputStream outputStream = null;

  /**
   * 构造一个xml
   *
   * @throws IOException
   */
  public void load(final String gossipPath, final String destination) throws IOException, BusinessException {

    RLock locker = RedissonUtil.getRedissonClient().getLock("locker.system.config.aiml.indexUpdate");

    try {
      if (!locker.tryLock(3, 60, TimeUnit.MINUTES)) {  // 等待3秒，上锁10分钟后自动解锁
        return;
      }

      String line;
      init(gossipPath, destination);
      StringBuffer stringBuf = new StringBuffer();
      stringBuf
          .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<aiml>\n");
      // 读取gossip.txt文件内容
      while ((line = bufReader.readLine()) != null) {
        stringBuf.append(analyzing(line) + "\n");
      }
      stringBuf.append("</aiml>");
      outputStream.write(stringBuf.toString().getBytes());
    } catch (InterruptedException e) {
      log.info("{}", e);
    } finally {
      if (locker != null && locker.isLocked()) {
        locker.unlock();
      }
    }
  }

  /**
   * 构造一个category
   *
   * @param line
   * @return
   */
  private String analyzing(String line) {
    String[] pattern = line.split(":");
    return "<category><pattern>" + pattern[0] + "</pattern><template>"
        + pattern[1] + "</template></category>";
  }

  private void init(final String gossipPath, final String destination) throws BusinessException {
    log.info(gossipPath);
    log.info(destination);
    File gossipFile = new File(gossipPath);
    try {
      inputStream(new FileInputStream(gossipFile));
      bufferedReader(this.inputStream);
      outputStream(new FileOutputStream(destination));
    } catch (FileNotFoundException e) {
      throw new BusinessException("[ExceptionInfo]./gossip.txt文件不存在。", e);
    }
  }

  private void outputStream(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  private void bufferedReader(InputStream inputStream) {
    this.bufReader = new BufferedReader(new InputStreamReader(inputStream));
  }

  private void inputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public void clean() { // 这里应该对每个关闭单独使用try catch块，保证关闭资源的引发的异常不会影响其他资源的关闭。

    if (outputStream != null) {
      try {
        outputStream.close();
      } catch (IOException e) {
        log.error("{}", e);
      }
    }
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        log.error("{}", e);
      }
    }
    if (bufReader != null) {
      try {
        bufReader.close();
      } catch (IOException e) {
        log.error("{}", e);
      }
    }
  }
}
