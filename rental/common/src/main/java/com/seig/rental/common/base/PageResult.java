package com.seig.rental.common.base;

import lombok.Data;

import java.util.List;

/**
 * 分页返回结果
 */
@Data
public class PageResult<T> {

    /** 当前页码 */
    private long page;

    /** 每页条数 */
    private long size;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private long pages;

    /** 数据列表 */
    private List<T> records;

    public static <T> PageResult<T> of(long page, long size, long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setPages(total % size == 0 ? total / size : total / size + 1);
        result.setRecords(records);
        return result;
    }
}
