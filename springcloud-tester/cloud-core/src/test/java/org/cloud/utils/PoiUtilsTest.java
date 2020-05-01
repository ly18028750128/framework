package org.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class PoiUtilsTest {
    @Test
    public void wordReadTest() throws Exception {
        InputStream is = new FileInputStream("E:\\work\\workspace\\git\\oschina\\heyigis\\doc\\需求文档\\书签版输出模板.docx");
        try {
            XWPFDocument document = new XWPFDocument(is);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            String text = extractor.getText();
            List<XWPFParagraph> paragraphList = document.getParagraphs();

            for (XWPFParagraph xwpfParagraph : paragraphList) {
                CTP ctp = xwpfParagraph.getCTP();
                if(ctp.sizeOfBookmarkEndArray()<1) continue;
                log.info("ctp.sizeOfBookmarkEndArray()={},{}", ctp.sizeOfBookmarkEndArray(),xwpfParagraph.getText());
                for (int dwI = 0; dwI < ctp.sizeOfBookmarkStartArray(); dwI++) {
                    CTBookmark bookmark = ctp.getBookmarkStartArray(dwI);
                    log.info("bookmark.getName()={}", bookmark.getName());
                    XWPFRun run = xwpfParagraph.createRun();  // 写入书签

//                    run.setText(dataMap.get(bookmark.getName()));

                }



//                log.info("ctp.getBookmarkStartList()={}", ctp.getBookmarkStartList());

            }

//            log.info("text={}", text);
            POIXMLProperties.CoreProperties coreProps = extractor.getCoreProperties();
            this.printCoreProperties(coreProps);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private void printCoreProperties(POIXMLProperties.CoreProperties coreProps) {
        System.out.println(coreProps.getCategory());   //分类
        System.out.println(coreProps.getCreator()); //创建者
        System.out.println(coreProps.getCreated()); //创建时间
        System.out.println(coreProps.getTitle());   //标题
    }
}
