package org.cloud.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LambdaTest {

    @Test
    void test() {
        MathOperation multiplication = (a, b) -> {
            return a * b;
        };

        List<String> strings = Arrays.asList("bc", "", "bc", "efg", "abcd", "", "jkl");

        List<String> stringsBack = strings.stream().filter(s -> !s.isEmpty()).sorted(Comparator.comparing(String::getBytes, Comparator.comparingInt(a -> a.length))).distinct().collect(Collectors.toList());

        log.info("{}", stringsBack);

    }
}
