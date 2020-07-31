package org.cloud.utils;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.poi.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class PoiUtilsTest {

    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";


    @Test
    public void wordReadTest() throws Exception {
        WordBookmarkVO<WordTableVO> tableWordBookmarkVO = new WordBookmarkVO<>();
        // @todo 继续编写按类型生成word的逻辑
        WordTableVO wordTableVO = new WordTableVO();
        List<WordTableHeaderVO> headers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            WordTableHeaderVO headerData = new WordTableHeaderVO("字段_" + i, "3B98FF");
            if (i % 2 == 0) {
                headerData = new WordTableHeaderVO("字段_" + i);
                headerData.setStyleVO(null);
                headerData.setCellWidth("100");
            }
            headers.add(headerData);
        }
        wordTableVO.setHeaderList(headers);
        List<List<WordTableCellVO>> rowsData = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            List<WordTableCellVO> rowDataList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                WordTableCellVO rowData = new WordTableCellVO("value_" + j + "_" + i);
                if (i % 2 == 0) {
                    rowData.setStyleVO(new WordParagraphStyleVO(12, true, "F11B1B"));  //每间隔一列加粗,并变色
                }
                if (j % 2 == 0) {
                    rowData.setColor("FFFAE3");  //每一行换底色
                }
                rowDataList.add(rowData);
            }
            rowsData.add(rowDataList);
        }
        wordTableVO.setDataList(rowsData);

        tableWordBookmarkVO.setValue(wordTableVO);
        tableWordBookmarkVO.setName("现状情况");
        tableWordBookmarkVO.setType("table");

        List<WordBookmarkVO<?>> bookmarkVOList = new ArrayList<>();

        bookmarkVOList.add(tableWordBookmarkVO);

        WordBookmarkVO<WordTextVO> textWordBookmarkVO = new WordBookmarkVO<>();
        textWordBookmarkVO.setValue(new WordTextVO("这真的是个好项目"));
        textWordBookmarkVO.setName("项目名称");
        textWordBookmarkVO.setType("text");
        bookmarkVOList.add(textWordBookmarkVO);

        textWordBookmarkVO = new WordBookmarkVO<>();
        textWordBookmarkVO.setValue(new WordTextVO("合一城市投资集团"));
        textWordBookmarkVO.setName("生成人");
        textWordBookmarkVO.setType("text");
        bookmarkVOList.add(textWordBookmarkVO);

        textWordBookmarkVO = new WordBookmarkVO<>();
        textWordBookmarkVO.setValue(new WordTextVO("2020-05-14", new WordParagraphStyleVO(15, true, "77070B")));
        textWordBookmarkVO.setName("生成日期");
        textWordBookmarkVO.setType("text");
        bookmarkVOList.add(textWordBookmarkVO);

        WordBookmarkVO<WordPictureVO> picWordBookmarkVO = new WordBookmarkVO<>();
        picWordBookmarkVO.setName("现状图片");
        picWordBookmarkVO.setType("picture");
        picWordBookmarkVO.setValue(new WordPictureVO("E:\\backup\\temp\\shp\\shp21\\项目现状情况\\0.0\\shp21-1024.png"));
        bookmarkVOList.add(picWordBookmarkVO);


        Map<String, WordBookmarkVO<?>> wordBookmarkVOMap = bookmarkVOList.stream().collect(Collectors.toMap(WordBookmarkVO::getName,
                wordBookmarkVO -> wordBookmarkVO));
        InputStream is = new FileInputStream("E:\\work\\workspace\\git\\oschina\\heyigis\\doc\\需求文档\\书签版输出模板.docx");

        OutputStream outputStream = new FileOutputStream("E:\\work\\workspace\\git\\oschina\\heyigis\\doc\\需求文档\\书签版输出模板-1.docx");
        try {
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphList = document.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphList) {
                CTP ctp = xwpfParagraph.getCTP();
                if (ctp.sizeOfBookmarkEndArray() < 1)
                    continue;
                log.info("ctp.sizeOfBookmarkEndArray()={},{}", ctp.sizeOfBookmarkEndArray(), xwpfParagraph.getText());
                for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                    CTBookmark bookmark = ctp.getBookmarkStartArray(dwI);
                    WordBookmarkVO<?> wordBookmarkVO = wordBookmarkVOMap.get(bookmark.getName());
                    if (wordBookmarkVO == null)
                        continue;
                    if ("table".equals(wordBookmarkVO.getType())) {
                        WordUtils.getInstance().insertTable(xwpfParagraph, (WordTableVO) wordBookmarkVO.getValue());
                    } else if ("text".equals(wordBookmarkVO.getType())) {
                        WordUtils.getInstance().insertText(bookmark, xwpfParagraph, (WordTextVO) wordBookmarkVO.getValue());
                    } else if ("picture".equals(wordBookmarkVO.getType())) {
                        WordUtils.getInstance().insertPicture(bookmark, xwpfParagraph, (WordPictureVO) wordBookmarkVO.getValue());
                    }
                }
            }
            document.write(outputStream);
            document.close();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Test
    public void wordReadTestToPdf() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("E:\\work\\workspace\\git\\oschina\\heyigis\\doc\\需求文档\\书签版输出模板-1.docx");
        XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
        PdfOptions pdfOptions = PdfOptions.create();
        pdfOptions.fontEncoding("utf8");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\work\\workspace\\git\\oschina\\heyigis\\doc\\需求文档\\书签版输出模板-1.pdf");
        PdfConverter.getInstance().convert(xwpfDocument, fileOutputStream, pdfOptions);
        fileInputStream.close();
        fileOutputStream.close();
    }

    private void printCoreProperties(POIXMLProperties.CoreProperties coreProps) {
        System.out.println(coreProps.getCategory());   //分类
        System.out.println(coreProps.getCreator()); //创建者
        System.out.println(coreProps.getCreated()); //创建时间
        System.out.println(coreProps.getTitle());   //标题
    }

    @Test
    public void createXiaoxueShuxue() throws Exception {
        final int minSum = 60;  // 最小加法全
        final int maxSum = 99; // 是大加法数
        final int rowCount = 6;
        final int topicCount = rowCount * 23; // 题目的数量

        WordTableVO wordTableVO = new WordTableVO();
        List<WordTableCellVO> rowTopicList = new ArrayList<>();
        for (int i = 1; i <= topicCount; i++) {
            List<Integer> result = getNumbers(minSum, maxSum);
            WordTableCellVO wordTableCellVO = new WordTableCellVO();
            if (Math.random() < 0.5) {
                wordTableCellVO.setValue(String.format("%s + %s = %s", result.get(0), result.get(1), "_____"));
                log.info("{} + {} = {}", result.get(0), result.get(1), result.get(2));
            } else {
                wordTableCellVO.setValue(String.format("%s - %s = %s", result.get(2), result.get(0), "_____"));
                log.info("{} - {} = {}", result.get(2), result.get(0), result.get(1));
            }
            rowTopicList.add(wordTableCellVO);
            if (i % rowCount == 0 || i == topicCount) {
                wordTableVO.getDataList().add(rowTopicList);
                rowTopicList = new ArrayList<>();
            }
        }
        WordBookmarkVO<WordTableVO> tableWordBookmarkVO = new WordBookmarkVO<>();
        tableWordBookmarkVO.setValue(wordTableVO);
        InputStream is = new FileInputStream("E:\\龙言\\学习资料\\数学\\小学数学模板.docx");
        OutputStream outputStream =
                new FileOutputStream("E:\\龙言\\学习资料\\数学\\小学数学模板" + CoreConstant.DateTimeFormat.FULLDATE.getDateFormat().format(new Date()) +
                        ".docx");
        try {
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphList = document.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphList) {
                CTP ctp = xwpfParagraph.getCTP();
                if (ctp.sizeOfBookmarkEndArray() < 1)
                    continue;
                log.info("ctp.sizeOfBookmarkEndArray()={},{}", ctp.sizeOfBookmarkEndArray(), xwpfParagraph.getText());
                for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                    WordUtils.getInstance().insertTable(xwpfParagraph, wordTableVO);
                }
            }
            document.write(outputStream);
            document.close();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(outputStream);
        }

    }

    public List<Integer> getNumbers(final int minSum, final int maxSum) {
        final List<Integer> result = new ArrayList<>();
        final int answer = minSum + Double.valueOf(Math.random() * (maxSum - minSum)).intValue();
        final int number1 = Double.valueOf(answer * (Math.random())).intValue();
        result.add(number1);
        final int number2 = answer - number1;
        result.add(number2);


        result.add(answer);
        return result;
    }

    @Test
    public void createXiaoxueShuxueTree() throws Exception {
        final int minSum = 60;  // 最小加法全
        final int maxSum = 99; // 是大加法数
        final int rowCount = 4;
        final int topicCount = rowCount * 23; // 题目的数量

        WordTableVO wordTableVO = new WordTableVO();
        List<WordTableCellVO> rowTopicList = new ArrayList<>();
        for (int i = 1; i <= topicCount; i++) {
            List<Integer> result = getNumbersTree(minSum, maxSum);
            WordTableCellVO wordTableCellVO = new WordTableCellVO();
            if (Math.random() < 0.5) {
                wordTableCellVO.setValue(String.format("%s + %s + %s = %s", result.get(0), result.get(1), result.get(2), "______"));
                log.info("{} + {} + {} = {}", result.get(0), result.get(1), result.get(2), result.get(3));
            } else {
                wordTableCellVO.setValue(String.format("%s - %s - %s = %s", result.get(3), result.get(0), result.get(1), "______"));
                log.info("{} - {} - {} = {}", result.get(3), result.get(0), result.get(1), result.get(2));
            }
            rowTopicList.add(wordTableCellVO);
            if (i % rowCount == 0 || i == topicCount) {
                wordTableVO.getDataList().add(rowTopicList);
                rowTopicList = new ArrayList<>();
            }
        }
        WordBookmarkVO<WordTableVO> tableWordBookmarkVO = new WordBookmarkVO<>();
        tableWordBookmarkVO.setValue(wordTableVO);
        InputStream is = new FileInputStream("E:\\龙言\\学习资料\\数学\\小学数学模板.docx");
        OutputStream outputStream =
                new FileOutputStream("E:\\龙言\\学习资料\\数学\\小学数学模板" + CoreConstant.DateTimeFormat.FULLDATE.getDateFormat().format(new Date()) +
                        ".docx");
        try {
            XWPFDocument document = new XWPFDocument(is);
            List<XWPFParagraph> paragraphList = document.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphList) {
                CTP ctp = xwpfParagraph.getCTP();
                if (ctp.sizeOfBookmarkEndArray() < 1)
                    continue;
                log.info("ctp.sizeOfBookmarkEndArray()={},{}", ctp.sizeOfBookmarkEndArray(), xwpfParagraph.getText());
                for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                    WordUtils.getInstance().insertTable(xwpfParagraph, wordTableVO);
                }
            }
            document.write(outputStream);
            document.close();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(outputStream);
        }

    }

    public List<Integer> getNumbersTree(final int minSum, final int maxSum) {
        final List<Integer> result = new ArrayList<>();
        final int answer = minSum + Double.valueOf(Math.random() * (maxSum - minSum)).intValue();
        final int number1 = Double.valueOf(answer * (Math.random())).intValue();
        result.add(number1);
        final int number2 = Double.valueOf((answer - number1) * Math.random()).intValue();
        result.add(number2);
        final int number3 = answer - number1 - number2;
        result.add(number3);
        result.add(answer);
        return result;
    }
}
