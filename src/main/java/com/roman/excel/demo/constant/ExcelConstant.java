package com.roman.excel.demo.constant;

public class ExcelConstant {
    /**
     * 每个sheet存储的记录数 100W
     */
    public static final Integer PER_SHEET_ROW_COUNT = 10000;

    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 20W
     */
    public static final Integer PER_WRITE_ROW_COUNT = 500;


    /**
     * 每个sheet的写入次数 5
     */
    public static final Integer PER_SHEET_WRITE_COUNT = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;

}
