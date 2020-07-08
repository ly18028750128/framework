package org.cloud.vo.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/13 7:16
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WordTableHeaderVO {
    private String value;
    private String color = "FFFAE3"; // 列的底色
    private String cellWidth = "50";
    private WordParagraphStyleVO styleVO = new WordParagraphStyleVO(14, true, "000000");  //内容的样式默认为空，默认为加粗

    public WordTableHeaderVO(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public WordTableHeaderVO(String value) {
        this.value = value;
    }
}
