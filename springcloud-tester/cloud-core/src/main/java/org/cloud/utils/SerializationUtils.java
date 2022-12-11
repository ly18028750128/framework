package org.cloud.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * 序列化工具类
 *
 * @author Logan
 * @version 1.0.0
 */
public final class SerializationUtils {

    private SerializationUtils() {

    }

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @return 序列化后的字节数组
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        if (null == obj) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        try {
            out.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(byteArrayOutputStream);
        }

    }

    /**
     * 反序列化
     *
     * @param bytes 对象序列化后的字节数组
     * @return 反序列化后的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        if (null == bytes) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        try {
            return in.readObject();
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 反序列化成指定类型的对象
     *
     * @param bytes 对象序列化后的字节数组
     * @param c     反序列化后的对象类型
     * @return 指定类型的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(byte[] bytes, Class<T> c) throws ClassNotFoundException, IOException {
        return c.cast(deserialize(bytes));
    }

}