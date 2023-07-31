package com.unknow.first.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/13 7:18
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WordParagraphStyleVO {
    private Integer fontSize = 12;
    private Boolean blod = false;
    private String color = "000000";

    public WordParagraphStyleVO(Integer fontSize, Boolean blod) {
        this.fontSize = fontSize;
        this.blod = blod;
    }

    public WordParagraphStyleVO(Integer fontSize) {
        this.fontSize = fontSize;
    }


//    //垂直居中
//        p.setVerticalAlignment(TextAlignment.CENTER);
//    //水平居中
//        p.setAlignment(ParagraphAlignment.CENTER);


}
