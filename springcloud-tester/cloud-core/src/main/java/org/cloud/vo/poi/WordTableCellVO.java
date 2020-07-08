package org.cloud.vo.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.cloud.utils.SystemStringUtil;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/13 7:15
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WordTableCellVO {
    private String value;  // 数据值
    private String type = "String";   // 数据类型,根据不同的类型需要做下转换
    private String color = "FFFFFF"; // 列的底色
    private WordParagraphStyleVO styleVO=new WordParagraphStyleVO();  //内容的样式默认为空，就直接设置字符

    public WordTableCellVO(String value) {
        this.value = value;
    }

    public WordTableCellVO(String value, String type) {
        this.value = value;
        this.type = SystemStringUtil.single().isEmpty(type) ? "String" : type;
    }

    public WordTableCellVO(String value, String type, String color) {
        this.value = value;
        this.type = SystemStringUtil.single().isEmpty(type) ? "String" : type;
        this.color = SystemStringUtil.single().isEmpty(color) ? "FFFFFF" : color;
        ;
    }
}
