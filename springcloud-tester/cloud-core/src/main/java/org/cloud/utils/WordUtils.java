package org.cloud.utils;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.cloud.vo.poi.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
public final class WordUtils {

    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "w:bookmarkStart";
    public static final String BOOKMARK_END_TAG = "w:bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";

    private WordUtils() {

    }

    @Getter
    private final static WordUtils instance = new WordUtils();

    /**
     * 增加图片
     *
     * @param bookmark
     * @param xwpfParagraph
     * @param wordPictureVO
     * @throws Exception
     */
    public void insertPicture(final CTBookmark bookmark, final XWPFParagraph xwpfParagraph, WordPictureVO wordPictureVO) throws Exception {
        final CTP ctp = xwpfParagraph.getCTP();
        log.info("bookmark.getName()={}", bookmark.getName());
        XWPFRun run = xwpfParagraph.createRun();  // 写入书签
        File picFile = new File(wordPictureVO.getPicPath());
        InputStream inputStreamPic = new FileInputStream(picFile);
        BufferedImage image = ImageIO.read(picFile);
        try {
            run.addPicture(inputStreamPic, Document.PICTURE_TYPE_PNG, "", Units.toEMU(wordPictureVO.getWidth()), Units.toEMU(wordPictureVO.getHeight()));
            Node firstNode = bookmark.getDomNode();
            Node nextNode = getBookmarkNextNode(firstNode, ctp);
            if (nextNode == null) {
                // 始终找不到结束标识的，就在书签前面添加
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), firstNode);
            } else {
                // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
                ctp.getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
            }
        } finally {
            IOUtils.closeQuietly(inputStreamPic);
        }
    }

    /**
     * @param bookmark
     * @param xwpfParagraph
     * @param wordTextVO
     * @throws Exception
     */
    public void insertText(final CTBookmark bookmark, final XWPFParagraph xwpfParagraph, final WordTextVO wordTextVO) throws Exception {
        final CTP ctp = xwpfParagraph.getCTP();
        XWPFRun xwpfRun = xwpfParagraph.createRun();
        xwpfRun.setText(wordTextVO.getValue());
        Node firstNode = bookmark.getDomNode();
        Node nextNode = getBookmarkNextNode(firstNode, ctp);
        if (wordTextVO.getStyleVO() != null) {
            setParagraphStyle(xwpfRun, wordTextVO.getStyleVO());
        }
        if (nextNode == null) {
            // 始终找不到结束标识的，就在书签前面添加
            ctp.getDomNode().insertBefore(xwpfRun.getCTR().getDomNode(), firstNode);
        } else {
            // 找到结束符，将新内容添加到结束符之前，即内容写入bookmark中间
            ctp.getDomNode().insertBefore(xwpfRun.getCTR().getDomNode(), nextNode);
        }
    }

    public void insertTable(final XWPFParagraph xwpfParagraph, WordTableVO wordTableVO) {
        XmlCursor cursor = xwpfParagraph.getCTP().newCursor();
        XWPFTable table = xwpfParagraph.getDocument().insertNewTbl(cursor);

        final boolean isHasHead = wordTableVO.getHeaderList() != null && wordTableVO.getHeaderList().size() > 0;

        XWPFTableRow row = table.getRow(0);
        row.setHeight(wordTableVO.getHeaderHeight());
        for (int i = 0; i < wordTableVO.getHeaderList().size(); i++) {
            WordTableHeaderVO header = wordTableVO.getHeaderList().get(i);
            XWPFTableCell xwpfTableCell = getXwpfTableCell(row, i);
            xwpfTableCell.setColor(header.getColor());
            xwpfTableCell.setWidth(header.getCellWidth());
            if (header.getStyleVO() == null) {
                xwpfTableCell.setText(header.getValue());
            } else {
                XWPFRun xwpfRun = getTableCellParagraph(0, xwpfTableCell).createRun();
                xwpfRun.setText(header.getValue());
                setParagraphStyle(xwpfRun, header.getStyleVO());
            }
        }
        if (isHasHead) {
            row = table.createRow();
        }
        for (int j = 0; j < wordTableVO.getDataList().size(); j++) {
            List<WordTableCellVO> rowDataList = wordTableVO.getDataList().get(j);
            for (int i = 0; i < rowDataList.size(); i++) {
                XWPFTableCell xwpfTableCell = getXwpfTableCell(row, i);
                WordTableCellVO rowData = rowDataList.get(i);
                xwpfTableCell.setColor(rowData.getColor());
                if (rowData.getStyleVO() == null) {
                    xwpfTableCell.setText(rowData.getValue());
                } else {
                    XWPFRun xwpfRun = getTableCellParagraph(0, xwpfTableCell).createRun();
                    xwpfRun.setText(rowData.getValue());
                    setParagraphStyle(xwpfRun, rowData.getStyleVO());
                }
            }
            if (j < (wordTableVO.getDataList().size() - 1)) {
                row = table.createRow();
            }

        }
    }

    private XWPFTableCell getXwpfTableCell(XWPFTableRow row, int pos) {
        XWPFTableCell xwpfTableCell = row.getCell(pos);
        if (xwpfTableCell == null) {
            xwpfTableCell = row.createCell();
        }
        return xwpfTableCell;
    }


    public XWPFParagraph getTableCellParagraph(int pos, XWPFTableCell xwpfTableCell) {
        XWPFParagraph paragraph = xwpfTableCell.getParagraphArray(pos);
        if (paragraph == null) {
            paragraph = xwpfTableCell.addParagraph();
        }
        return paragraph;
    }

    public void setParagraphStyle(XWPFRun xwpfRun, WordParagraphStyleVO styleVO) {
        xwpfRun.setColor(styleVO.getColor());
        xwpfRun.setBold(styleVO.getBlod());
        xwpfRun.setFontSize(styleVO.getFontSize());
    }

    public Node getBookmarkNextNode(final Node firstNode, final CTP ctp) {
        Node nextNode = firstNode.getNextSibling();
        while (nextNode != null) {
            String nodeName = nextNode.getNodeName();
            if (nodeName.equals(BOOKMARK_END_TAG)) {
                break;
            }
            Node delNode = nextNode;
            nextNode = nextNode.getNextSibling();
            ctp.getDomNode().removeChild(delNode);
        }
        return nextNode;
    }
}
