package org.cloud.utils.image;

import com.baomidou.dynamic.datasource.toolkit.Base64;
import lombok.Data;
import org.cloud.utils.math.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

public final class ValidateCodeUtil {

    public static int defaultWidth = 80;     //图片宽度
    public static int defaultHeight = 34;    //图片高度
    public static int defaultStringNum = 4;  //字符的数量
    public static int defaultLineSize = 20;  //干扰线数量

    private String randString = "0123456789abcdefghijkmnpqrtyABCDEFGHIJLMNQRTY";//随机生成字符串的取值范围
    private Random random = new Random();              //随机类，用于生成随机参数


    private ValidateCodeUtil() {
    }

    private final static ValidateCodeUtil instance = new ValidateCodeUtil();

    public static ValidateCodeUtil getInstance() {
        return instance;
    }

    /**
     * 获取随机字符,并返回字符的String格式
     *
     * @param index (指定位置)
     * @return
     */
    private String getRandomChar(int index) {
        //获取指定位置index的字符，并转换成字符串表示形式
        return String.valueOf(randString.charAt(index));
    }

    /**
     * 获取随机指定区间的随机数
     *
     * @param min (指定最小数)
     * @param max (指定最大数)
     * @return
     */
    private int getRandomNum(int min, int max) {
        return RandomUtils.getInstance().getInt(min, max);
    }

    /**
     * 获得字体
     *
     * @return
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 25);  //名称、样式、磅值
    }

    /**
     * 获得颜色
     *
     * @param backColor
     * @param frontColor
     * @return
     */
    private Color getRandColor(int frontColor, int backColor) {
        if (frontColor > 255)
            frontColor = 255;
        if (backColor > 255)
            backColor = 255;

        int red = frontColor + random.nextInt(backColor - frontColor - 16);
        int green = frontColor + random.nextInt(backColor - frontColor - 14);
        int blue = frontColor + random.nextInt(backColor - frontColor - 18);
        return new Color(red, green, blue);
    }

    /**
     * 绘制字符串,返回绘制的字符串
     *
     * @param g
     * @param randomString
     * @param i
     * @return
     */
    private String drawString(Graphics g, String randomString, int i) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(getFont());   //设置字体
        g2d.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));//设置颜色
        String randChar = String.valueOf(getRandomChar(random.nextInt(randString.length())));
        randomString += randChar;   //组装
        int rot = getRandomNum(5, 10);
        g2d.translate(random.nextInt(3), random.nextInt(3));
        g2d.rotate(rot * Math.PI / 180);
        g2d.drawString(randChar, 13 * i, 20);
        g2d.rotate(-rot * Math.PI / 180);
        return randomString;
    }

    /**
     * 绘制干扰线
     *
     * @param g
     */
    private void drawLine(Graphics g, final int width, final int height) {
        //起点(x,y)  偏移量x1、y1
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * @return String 返回base64
     * @MethodName: getRandomCode
     * @Description: 生成Base64图片验证码
     */
    public ValidateCodeVO getRandomCode(final int width, final int height, final int lineSize, final int StringNum) {
        ValidateCodeVO validate = new ValidateCodeVO();

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();// 获得BufferedImage对象的Graphics对象
        g.fillRect(0, 0, width, height);//填充矩形
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));//设置字体
        g.setColor(getRandColor(110, 133));//设置颜色
        //绘制干扰线
        for (int i = 0; i <= lineSize; i++) {
            drawLine(g, width, height);
        }
        //绘制字符
        String randomString = "";
        for (int i = 1; i <= StringNum; i++) {
            randomString = drawString(g, randomString, i);
            validate.setValue(randomString);
        }

        g.dispose();//释放绘图资源
        ByteArrayOutputStream bs = null;
        try {
            bs = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bs);//将绘制得图片输出到流
            String imgsrc = Base64.byteArrayToBase64(bs.toByteArray());
            validate.setBase64ImageUrl("data:image/png;base64," + imgsrc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bs = null;
            }
        }
        return validate;
    }

    public ValidateCodeVO getRandomCode() {
        return getRandomCode(defaultWidth, defaultHeight, defaultLineSize, defaultStringNum);
    }

    /**
     * @author chenhx
     * @ClassName: Validate
     * @Description: 验证码类
     * @date 2017年11月14日 上午11:35:34
     */
    @Data
    public class ValidateCodeVO implements Serializable {
        private static final long serialVersionUID = -8847813271787345354L;
        private String redisKey;
        private String base64ImageUrl;        //Base64 值
        private String value;            //验证码值
    }

}
