package com.roman.excel.demo.service;

import org.apache.poi.xssf.streaming.SXSSFSheet;

public interface WriteExcelDataDelegated {
    /**
     * EXCEL写数据委托类  针对不同的情况自行实现
     *
     * @param eachSheet     指定SHEET
     * @param startRowCount 开始行
     * @param endRowCount   结束行
     * @param currentPage   分批查询开始页
     * @param pageSize      分批查询数据量
     * @throws Exception
     */
    void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception;


}
