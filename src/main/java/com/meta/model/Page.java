package com.meta.model;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Log4j2
public class Page {

    // 第几页
    private Long pageIndex = null;

    // 一页多少条
    private Long pageSize = 20L;

    // 总条数
    private Long total = null;

    // 总页数
    private Long totalPage = null;

    /**
     * 获取当前页码
     * */
    public Long getPageIndex(){
        return this.pageIndex;
    }

    /**
     * 获取每页条数
     * */
    public Long getPageSize(){
        return this.pageSize;
    }

    /**
     * 获取总条数
     * */
    public Long getTotal(){
        return this.total;
    }

    /**
     * 获取总页数
     * */
    public Long getTotalPage(){
        return this.totalPage;
    }

    /**
     * 设置当前页码
     * */
    public void setPageIndex(Long pageIndex){
        if (ObjectUtils.isNotEmpty(pageIndex)){
            this.pageIndex = pageIndex;
        }
    }

    /**
     * 设置每页条数
     * */
    public void setPageSize(Long pageSize){
        if (ObjectUtils.isNotEmpty(pageSize)){
            this.pageSize = pageSize;
        }
    }

    /**
     * 设置总条数
     * */
    public void setTotal(Long total){
        if (ObjectUtils.isNotEmpty(total)){
            long totalPage = total / this.pageSize;
            this.total = total;
            this.totalPage = totalPage;
        }
    }

    /**
     * 获取分页 sql
     * */
    public String limitSql(){
        log.info("pageIndex:{}, pageSize:{}", pageIndex, pageSize);
        if (ObjectUtils.isEmpty(this.pageIndex) || ObjectUtils.isEmpty(this.pageSize)){
            return null;
        }
        if (this.pageIndex - 1L < 0){
            return null;
        }
        long startIndex = (this.pageIndex - 1L) * this.pageSize;
        return "limit " + startIndex + ", " + pageSize;
    }

}
