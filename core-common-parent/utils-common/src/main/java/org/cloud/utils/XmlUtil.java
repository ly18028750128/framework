package org.cloud.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class XmlUtil {

    private XmlUtil() {
    }

    ;

    private final static XmlUtil instance = new XmlUtil();

    private static final ObjectMapper OBJECT_MAPPER = new XmlMapper();

    public static XmlUtil single() {
        return instance;
    }

    /**
     * 将Object转换为XML字符串
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public String Object2XmlString(@NotNull Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * 将XML字符串转换为JavaBean对象，ObjectMapper还提供了很多重载方法，详情查看源码，这里不一一列举
     *
     * @param content
     * @param tClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> T xmlString2Object(@NotNull String content, @NotNull Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(content, tClass);
    }
}
