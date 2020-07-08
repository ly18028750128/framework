package org.cloud.vo.poi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author 悠悠龙
 * @version 1.0
 * @date 2020/5/14 20:41
 */
@Data
@ToString
@AllArgsConstructor
public class WordPictureVO {
    private String picPath;
    private Integer width = 400;
    private Integer height = 300;

    public WordPictureVO(String picPath) {
        this.picPath = picPath;
    }
}
