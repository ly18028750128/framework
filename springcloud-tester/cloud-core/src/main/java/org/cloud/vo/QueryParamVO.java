package org.cloud.vo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class QueryParamVO {


    private class PageQueryParam {
        Integer pageNum = 1;
        Integer pageSize = 10;
    }

    public QueryParamVO() {

    }

    public QueryParamVO(int page, int pageSize) {
        this.setPageNum(page);
        this.setPageSize(pageSize);
    }

    private PageQueryParam pageQueryParam = new PageQueryParam();

    public void setPageNum(Integer pageNum) {
        pageQueryParam.pageNum = pageNum;
    }

    public Integer getPageNum() {
        return pageQueryParam.pageNum;
    }

    public void setPageSize(Integer pageSize) {
        pageQueryParam.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageQueryParam.pageSize;
    }

    private Map<String, Object> params = new LinkedHashMap<>();
    private List<String> orderColumns = new LinkedList<>();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(List<String> orderColumns) {
        this.orderColumns = orderColumns;
    }

}
