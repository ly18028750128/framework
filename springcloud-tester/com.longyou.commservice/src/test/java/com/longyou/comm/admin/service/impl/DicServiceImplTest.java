package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IDicService;
import com.longyou.comm.starter.CommonServiceApplication;
import org.cloud.utils.SystemDicUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DicServiceImplTest {

    @Autowired
    IDicService dicServiceImpl;

    @Test
    void refreshCache() throws Exception {
        dicServiceImpl.refreshCache();
        Assert.assertTrue(!SystemDicUtil.single().getDicItemList("commStatus").isEmpty());
        Assert.assertTrue(SystemDicUtil.single().getDicItemList("orderStatus").isEmpty());
        Assert.assertTrue(!SystemDicUtil.single().getDicItemList("XGSIXTEEN", "orderStatus").isEmpty());

        Assert.assertNull(SystemDicUtil.single().getDic("orderStatus"));

        Assert.assertNotNull(SystemDicUtil.single().getDic("XGSIXTEEN", "orderStatus"));

    }
}