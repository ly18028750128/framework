package org.cloud.utils;

import com.alibaba.fastjson.JSON;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CollectionUtilTest {

    private Logger logger = LoggerFactory.getLogger(CollectionUtilTest.class);

    @Test
    public void spitList() {
//        Integer maxLength = 2;
//        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
//        List<List<Integer>> result = CollectionUtil.single().spitList(list, maxLength);
//        System.out.println(JSON.toJSONString(result));
//        assertEquals(result.size(), Double.valueOf(Math.ceil(list.size() / (maxLength * 1.00))).intValue());
//
//        maxLength = 5;
//
//        List<String> listString = new ArrayList<String>();
//        for (int i = 0; i < 10000; i++) {
//            listString.add(Double.toString(Math.random()*1000) );
//        }
//        List<List<String>> resultStringList = CollectionUtil.single().spitList(listString, maxLength);
//        System.out.println(JSON.toJSONString(resultStringList));
//        assertEquals(resultStringList.size(), Double.valueOf(Math.ceil(listString.size() / (maxLength * 1.00))).intValue());
    }

    @Test
    public void converListToOtherType(){
        List<String> ids = Arrays.asList("5e61f1631fb2f2184ac32d18","5e61f22e1fb2f2184ac32d32","5e620ae3fdaee45eb33f6b19");
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());
        logger.info(JSON.toJSONString(objectIds));
    }
}