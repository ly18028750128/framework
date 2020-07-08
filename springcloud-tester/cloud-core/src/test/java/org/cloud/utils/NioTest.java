package org.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/17 17:53
 */
@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class NioTest {

    @Test
    public void nioReadFile() throws Exception {
        IntBuffer intBuffer = IntBuffer.allocate(20);
        List<Integer> readResult = new ArrayList<>();

        log.info("capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(), intBuffer.position());

        for (int i = 0; i < 10; i++) {
            intBuffer.put(i);
        }

        log.info("after put: capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(), intBuffer.position());


        intBuffer.flip();
        log.info("after flip to read: capacity={},limit={},position={},mark={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position(),
                intBuffer.mark());

        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            readResult.add(j);
        }
        log.info("通过get获取的三个值为{}", readResult);
        log.info("通过get方法从缓冲区获取三个值后: capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(), intBuffer.position());


        intBuffer.compact();
        log.info("通过 compact() 转找成 writer: capacity={},limit={},position={},mark={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position(),
                intBuffer.mark());

        intBuffer.flip();
        log.info("after flip to read: capacity={},limit={},position={},mark={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position(),
                intBuffer.mark());

        readResult = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            readResult.add(j);
        }
        log.info("通过get获取的三个值为{}", readResult);
        log.info("通过get方法从缓冲区获取三个值后: capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(), intBuffer.position());

        intBuffer.rewind();
        readResult = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                intBuffer.mark();
            }
            int j = intBuffer.get();
            readResult.add(j);
        }
        log.info("执行rewind()后,通过get获取的三个值为{}", readResult);
        log.info("执行rewind()后，通过get方法从缓冲区获取三个值后: capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position());

        intBuffer.reset();
        readResult = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int j = intBuffer.get();
            readResult.add(j);
        }
        log.info("执行reset()后,通过get获取的三个值为{}", readResult);
        log.info("执行reset()后，通过get方法从缓冲区获取三个值后: capacity={},limit={},position={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position());

        intBuffer.clear();
        log.info("通过 clear() 转找成 writer: capacity={},limit={},position={},mark={}", intBuffer.capacity(), intBuffer.limit(),
                intBuffer.position(),
                intBuffer.mark());

    }

    @Test
    public void noiChannelTest() throws IOException {





//        RandomAccessFile accessFile = new RandomAccessFile("E:\\backup\\软件\\idea2020\\jetbrains-agent\\ChangeLogs.txt","rw");
        int bufferSize=1024;
        ByteBuffer byteBuffer=ByteBuffer.allocate(bufferSize);
        int length = -1;
        FileInputStream fileInputStream = new FileInputStream("E:\\backup\\软件\\idea2020\\jetbrains-agent\\ChangeLogs.txt");
        FileChannel fileInChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("E:\\backup\\软件\\idea2020\\jetbrains-agent\\ChangeLogs1.txt");
        FileChannel fileOutChannel = fileOutputStream.getChannel();

        while((length=fileInChannel.read(byteBuffer))!=-1){
            byteBuffer.flip();
            int outLength = 0;
            while ((outLength=fileOutChannel.write(byteBuffer))!=0){
                log.info("写入【{}】个字节",outLength);
            }
//            log.info(new String(byteBuffer.array()));
            byteBuffer.clear();
        }

        fileOutChannel.force(true);
    }


}
