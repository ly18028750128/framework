package com.unknow.first.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * word字符类
 *
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/14 7:08
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WordTextVO {
    private String value;
    private WordParagraphStyleVO styleVO;  //内容的样式默认为空，就直接设置字符

    public WordTextVO(String value) {
        this.value = value;
    }
}
