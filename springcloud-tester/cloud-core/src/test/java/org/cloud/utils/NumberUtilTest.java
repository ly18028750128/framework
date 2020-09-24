package org.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.stax2.ri.typed.NumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
class NumberUtilTest {

    @Test
    void randomNumberByWeight() {
        for (int randomSize = 1; randomSize < 5; randomSize++) {
            for (int start = 0; start < 5; start++) {
                final int startFinal = start;
                for (int i = 0; i < 1000; i++) {
                    Set<Integer> result = NumericUtil.single().randomNumberByWeight(randomSize, startFinal, 9);
                    Assert.isTrue(result.size() == randomSize, "随机数的数量不对");
                    result.forEach((value) -> {
                        Assert.isTrue(value >= startFinal && value <= 9, "范围不对");
                    });
                }
            }
        }
    }

    @Test
    void randomNumberWithWeight() {

        Map<Integer, Integer> weightMap = new LinkedHashMap<>();

        weightMap.put(0, 1200);
        weightMap.put(1, 1100);
        weightMap.put(2, 100);
        weightMap.put(3, 100);
        weightMap.put(4, 100);
        weightMap.put(5, 1);
        weightMap.put(6, 1000);
        weightMap.put(7, 178);
        weightMap.put(8, 0);
        weightMap.put(9, 1);


        final int end = 9;
        final int defaultWeight = 100;
        final int execTimes = 10000;

        for (int start = 0; start < 1; start++) {
            for (int randomSize = 2; randomSize < 3; randomSize++) {
                Map<Integer, Integer> totalMap = new LinkedHashMap<>();
                final int startFinal = start;
                for (int i = 0; i < execTimes; i++) {
                    Set<Integer> result = NumericUtil.single().randomNumberByWeight(randomSize, startFinal, end, weightMap, defaultWeight);
                    Assert.isTrue(result.size() == randomSize, "随机数的数量不对");
                    result.forEach((value) -> {
                        Assert.isTrue(value >= startFinal && value <= end, "范围不对");
//                        Assert.isTrue(value != 9, "范围不对");
                        Assert.isTrue(value != 8, "范围不对");

                        if (totalMap.get(value) == null) {
                            totalMap.put(value, 1);
                        } else {
                            totalMap.put(value, totalMap.get(value) + 1);
                        }
                    });
                }
                log.info("默认权重：{}，取数开始值：{}，取数结束：{}，取数个数：{}，执行{}次后的统计结果：{}", defaultWeight, startFinal, end, randomSize, execTimes, CollectionUtil.single().sortByValue(totalMap));
            }

        }

    }
}