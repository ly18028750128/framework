package org.cloud.vo.poi;

import lombok.Data;
import lombok.ToString;


@Data
@ToString

public class WordBookmarkVO<T> {
    private String name; // 名称
    private String type; // 类型：文字、图片、表格
    private T value;// 文字内容、图片地址、表格内容列表
}
