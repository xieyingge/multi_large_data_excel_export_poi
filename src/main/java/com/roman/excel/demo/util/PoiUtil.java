package com.roman.excel.demo.util;

import com.roman.excel.demo.service.WriteExcelDataDelegated;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.roman.excel.demo.constant.ExcelConstant.*;

public class PoiUtil {
    private final static Logger logger = LoggerFactory.getLogger(PoiUtil.class);

    /**
     * 初始化EXCEL(sheet个数和标题)
     *
     * @param totalRowCount 总记录数
     * @param titles        标题集合
     * @return XSSFWorkbook对象
     */
    public static SXSSFWorkbook initExcel(Integer totalRowCount, String[] titles) {

        // 在内存当中保持 100 行 , 超过的数据放到硬盘中在内存当中保持 100 行 , 超过的数据放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
//        wb.setCompressTempFiles();

        Integer sheetCount = ((totalRowCount % PER_SHEET_ROW_COUNT == 0) ?
                (totalRowCount / PER_SHEET_ROW_COUNT) : (totalRowCount / PER_SHEET_ROW_COUNT + 1));

        // 根据总记录数创建sheet并分配标题
        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet sheet = wb.createSheet("sheet" + (i + 1));
            SXSSFRow headRow = sheet.createRow(0);

            for (int j = 0; j < titles.length; j++) {
                SXSSFCell headRowCell = headRow.createCell(j);
                headRowCell.setCellValue(titles[j]);
            }
        }
        return wb;
    }

    /**
     * 下载EXCEL到本地指定的文件夹
     *
     * @param wb         EXCEL对象SXSSFWorkbook
     * @param exportPath 导出路径
     */
    public static void downLoadExcelToLocalPath(SXSSFWorkbook wb, String exportPath) {
        FileOutputStream fops = null;
        try {
            fops = new FileOutputStream(exportPath);
            wb.write(fops);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != fops) {
                try {
                    fops.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 下载EXCEL到浏览器
     *
     * @param wb       EXCEL对象XSSFWorkbook
     * @param response
     * @param fileName 文件名称
     * @throws IOException
     */
    public static void downLoadExcelToWebsite(SXSSFWorkbook wb, HttpServletResponse response, String fileName) throws IOException {

        response.setHeader("Content-disposition", "attachment; filename="
                + new String((fileName + ".xlsx").getBytes("utf-8"), "ISO8859-1"));//设置下载的文件名

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != wb) {
                try {
                    wb.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 导出Excel到浏览器
     *
     * @param response
     * @param totalRowCount           总记录数
     * @param fileName                文件名称
     * @param titles                  标题
     * @param writeExcelDataDelegated 向EXCEL写数据/处理格式的委托类 自行实现
     * @throws Exception
     */
    public static final void exportExcelToWebsite(HttpServletResponse response, Integer totalRowCount, String fileName, String[] titles, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        logger.info("开始导出：" + com.roman.excel.demo.util.DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));


        // 初始化EXCEL
        SXSSFWorkbook wb = PoiUtil.initExcel(totalRowCount, titles);


        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();

        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);
//            pool.submit(new EachSheetTask());


            for (int j = 1; j <= PER_SHEET_WRITE_COUNT; j++) {

                //  去数据库去数据的分页
                int currentPage = i * PER_SHEET_WRITE_COUNT + j;
                int pageSize = PER_WRITE_ROW_COUNT;

                //每次给sheet 创建行的位置
                int startRowCount = (j - 1) * PER_WRITE_ROW_COUNT + 1;
                int endRowCount = startRowCount + pageSize - 1;

                writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);

            }
        }

        /**
         * i = 0
         *  j =1   cur = 1 pa = 10  s = 1  e = 100
         *  j = 2  cur = 2 pa = 10  s = 101 e = 200
         *  j = 3  cur = 3 pa = 10  s = 201 e = 300
         *
         * i = 1
         *  j =1   cur = 6 pa = 10  s = 1  e = 100
         *  j = 2  cur = 7 pa = 10  s = 101 e = 200
         *
         *
         */


        // 下载EXCEL
        PoiUtil.downLoadExcelToWebsite(wb, response, fileName);

        logger.info("导出完成：" + com.roman.excel.demo.util.DateUtil.formatDate(new Date(), com.roman.excel.demo.util.DateUtil.YYYY_MM_DD_HH_MM_SS));
    }


    public static final void exportExcelToWebsite_multi(HttpServletResponse response, Integer totalRowCount, String fileName, String[] titles, WriteExcelDataDelegated writeExcelDataDelegated) throws Exception {

        logger.info("开始导出：" + com.roman.excel.demo.util.DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));


        // 初始化EXCEL
        SXSSFWorkbook wb = PoiUtil.initExcel(totalRowCount, titles);


        // 调用委托类分批写数据
        int sheetCount = wb.getNumberOfSheets();
        CountDownLatch latch = new CountDownLatch(sheetCount);
        ExecutorService pool = Executors.newFixedThreadPool(sheetCount);

        for (int i = 0; i < sheetCount; i++) {
            SXSSFSheet eachSheet = wb.getSheetAt(i);
            int finalI = i;
            pool.submit(() -> {
//                synchronized (eachSheet) {
                    for (int j = 1; j <= PER_SHEET_WRITE_COUNT; j++) {

                        //  去数据库去数据的分页
                        int currentPage = finalI * PER_SHEET_WRITE_COUNT + j;
                        int pageSize = PER_WRITE_ROW_COUNT;

                        //每次给sheet 创建行的位置
                        int startRowCount = (j - 1) * PER_WRITE_ROW_COUNT + 1;
                        int endRowCount = startRowCount + pageSize - 1;

                        try {
                            writeExcelDataDelegated.writeExcelData(eachSheet, startRowCount, endRowCount, currentPage, pageSize);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                }
                latch.countDown();
            });
        }


        latch.await();

        // 下载EXCEL
        PoiUtil.downLoadExcelToWebsite(wb, response, fileName);

        logger.info("导出完成：" + com.roman.excel.demo.util.DateUtil.formatDate(new Date(), com.roman.excel.demo.util.DateUtil.YYYY_MM_DD_HH_MM_SS));
    }

}
