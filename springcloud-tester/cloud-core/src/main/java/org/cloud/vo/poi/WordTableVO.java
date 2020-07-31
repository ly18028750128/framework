package org.cloud.vo.poi;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/12 20:06
 */
@Data
@ToString
public class WordTableVO {
    private List<WordTableHeaderVO> headerList = new ArrayList<>();  // 表格头
    private List<List<WordTableCellVO>> dataList = new ArrayList<>();    // 表格行数据

    private Integer rowHeight = 20;
    private Integer headerHeight = 30;
}
