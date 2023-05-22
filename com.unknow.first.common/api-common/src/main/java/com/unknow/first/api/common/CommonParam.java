package com.unknow.first.api.common;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CommonParam implements Serializable {

    @ApiModelProperty(value = "关键字")
    private String search;
    @ApiModelProperty(value = "页数", required = true, example = "1")
    private Integer page = 1;
    @ApiModelProperty(value = "条数", required = true, example = "10")
    private Integer limit;

    @ApiModelProperty(value = "排序", example = "id desc, age asc")
    private String sorts;

    {
        limit = 10;
    }

    public void setPage(Integer page) {
        this.page = page;
        if (this.page < 1){
            throw new IllegalArgumentException("Page number is too small");
        }
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    /**
     * 获取起始行数
     *
     * @return
     */
    public Integer getStart() {
        return (page - 1) * limit;
    }


}
