package org.cloud.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cloud.exception.BusinessException;

import java.security.SecureRandom;
import java.util.*;

@Slf4j
public class NumericUtil {

    private NumericUtil() {
    }

    private static class Handler {
        private Handler() {
        }

        private static NumericUtil handler = new NumericUtil();
    }

    public static NumericUtil single() {
        return Handler.handler;
    }

    public Integer countStep(Integer size, Integer stepSize) {
        return Double.valueOf(Math.ceil((size * 1.00) / (stepSize * 1.00))).intValue();
    }

    /**
     * 根据权重取随机值
     *
     * @param randomSize    随机数个数
     * @param start         开始值
     * @param end           结束值
     * @param weightMap     权重列表，key必须为start和end区间，如果不在，那么将被忽略。
     * @param defaultWeight 默认权重，当没有配置的时候的权重值
     * @return
     */
    @SneakyThrows
    public Set<Integer> randomNumberByWeight(int randomSize, int start, int end, Map<Integer, Integer> weightMap, int defaultWeight) {
        Random random = new SecureRandom();
        Set<Integer> result = new HashSet<>();

        if (randomSize < 1) {
            throw new BusinessException("取值数量必须大于0！");
        }

        if (start >= end) {
            throw new BusinessException("开始值应该小于结束值！");
        }

        if (defaultWeight < 3) {
            throw new BusinessException("默认权重最小应该为2！");
        }
        if (randomSize > (end - start + 1)) {
            throw new BusinessException("取数数量大于数据范围，请检查参数！");
        }

        if (randomSize == (end - start + 1)) {
            for (int startLoop = start; startLoop <= end; startLoop++) {
                result.add(startLoop);
            }
            return result;
        }

        if (CollectionUtil.single().isNotEmpty(weightMap)) {
            Map<Integer, int[]> mapWeightRange = new LinkedHashMap<>();  // 权重分布
            int weightAccumulate = 0; // 累加所有权重
            int weightLargerZero = 0;  // 统计大于零的权重数量，如果小于randomSize抛出异常
            for (int startLoop = start; startLoop <= end; startLoop++) {
                final Integer weight = (weightMap.get(startLoop) == null ? defaultWeight : weightMap.get(startLoop));
                if (weight > 0) {
                    mapWeightRange.put(startLoop, new int[]{weightAccumulate, weightAccumulate + weight - 1});
                    weightAccumulate += weight;
                    weightLargerZero++;
                } else if (weight < 0) {
                    throw new BusinessException("权重不能小于零！");
                }
            }
            if (randomSize > weightLargerZero) {
                throw new BusinessException("权重设置有问题，导致取数数量大于数据范围，请检查参数！");
            }
//            log.info("weightAccumulate={},mapWeightRange={}", weightAccumulate, JSON.toJSONString(mapWeightRange));
            while (result.size() != randomSize) {
                int weightRandom = random.nextInt(weightAccumulate);
                for (Integer key : mapWeightRange.keySet()) {
                    int[] range = mapWeightRange.get(key);
                    if (weightRandom >= range[0] && weightRandom <= range[1]) {
                        result.add(key);
                    }
                }
            }

            return result;
        } else {
            // 进行无权重计算
            while (result.size() != randomSize) {
                result.add(random.nextInt(end - start + 1) + start);
            }
        }
        return result;
    }

    public Set<Integer> randomNumberByWeight(int randomSize, int start, int end, Map<Integer, Integer> weightMap) {
        return this.randomNumberByWeight(randomSize, start, end, weightMap, 100);
    }

    public Set<Integer> randomNumberByWeight(int randomSize, int end, Map<Integer, Integer> weightMap) {
        return this.randomNumberByWeight(randomSize, 0, end, weightMap);
    }

    public Set<Integer> randomNumberByWeight(int randomSize, int start, int end) {
        return this.randomNumberByWeight(randomSize, start, end, null);
    }

    public Set<Integer> randomNumberByWeight(int randomSize, int end) {
        return this.randomNumberByWeight(randomSize, 0, end);
    }
}
