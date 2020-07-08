package org.cloud.common.service;

import java.util.List;
import java.util.Map;

public interface IWordService {
    /**
     * 导出方法
     * @param params  表格里的数据列表
     * @param orgFullName   需要替换的页眉的公司的名称
     * @param logoFilePath  需要替换的页眉的Logo的图片的地址
     * @param tableIndex    需替换的第几个表格的下标
     * @param textMap       需替换的文本的数据入参
     * @return
     * @throws Exception
     */
    public boolean export(List<Map<String,String>> params, String orgFullName, String logoFilePath, int tableIndex, Map<String,String> textMap) throws Exception;

}
