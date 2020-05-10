package com.roman.excel.demo.util;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ll on 2017/1/13.
 */
public abstract class ExcelFactory<T> {

    private Workbook workbook;

    private Sheet sheet;

    private String[] titleArray;

    private int rowIndex;

    private Font[] fonts;

    private CellStyle[] styles;

    public ExcelFactory(Sheet st, String[] titleArray) {
        this.sheet = st;
        this.workbook = st.getWorkbook();
        this.titleArray = titleArray;
        rowIndex = 0;
        fonts = new Font[5];
        styles = new CellStyle[5];
        generate5CellStyle();
    }

    public ExcelFactory(Sheet st, List<String> titleList) {
        this(st, titleList.toArray(new String[titleList.size()]));
    }

    /**
     * 格式化显示时间
     *
     * @param time
     * @return
     */
    public static String fomartTime(String time) {
        return StringUtil.formatTime(time);
    }

    /**
     * 格式化显示金额
     *
     * @param amount 分
     * @return
     */
    public static double formatAmount(Integer amount) {
        if (amount == null || amount == 0) {
            return 0.0;
        }
        BigDecimal b = new BigDecimal(amount);
        b = b.divide(new BigDecimal(100));
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    //判断excel行数限制多于 maxRow 返回true
    public static Boolean validateMaxRow(Sheet st, int maxRow) {
        if (st == null) {
            return false;
        }
        return st.getLastRowNum() > maxRow;
    }

    /**
     * 需要自定义样式时可以修改内容样式
     *
     * @param index 0内容  1 标题行 2 时间标题 3 orgName标题 4 主标题
     * @return
     */
    public CellStyle getStyle(int index) {
        return styles[index];
    }

    public void setStyle(int index, CellStyle style) {
        styles[index] = style;
    }

    public CellStyle generateCellStyle(short background) {
        CellStyle arrayTitleStyle = workbook.createCellStyle();
        Font arrayTitlefont = workbook.createFont();
        arrayTitlefont.setBold(false);
        arrayTitlefont.setFontHeightInPoints((short) 12);
        arrayTitlefont.setColor(HSSFFont.COLOR_NORMAL);
        arrayTitleStyle.setFont(arrayTitlefont);
//        arrayTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        arrayTitleStyle.setFillForegroundColor(background);
//        arrayTitleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        arrayTitleStyle.setBorderBottom((short) 1);    //设置边框样式
//        arrayTitleStyle.setBorderLeft((short) 1);     //左边框
//        arrayTitleStyle.setBorderRight((short) 1);    //右边框
//        arrayTitleStyle.setBorderTop((short) 1);    //顶边框
        return arrayTitleStyle;
    }

    /**
     * 每行对应的具体内容的object数组，暂时只支持String、Double类型数据
     *
     * @param o 生成内容时，传入列表的的具体对象
     * @return
     */
    protected abstract Object[] setContents(T o);

    /**
     * 需要对列表内容进行过滤 重写该方法
     *
     * @param o
     * @return
     */
    protected boolean listValidate(T o) {
        return true;
    }

    /**
     * 生成内容
     *
     * @param list
     * @return
     */
    public ExcelFactory generateContent(List<T> list, int width) {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 12, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(list.size()));
        for (T o : list) {
            if (!listValidate(o)) {
                continue;
            }
            Row row = sheet.createRow(rowIndex++);
            generateRowContent(row, setContents(o));
//            threadPoolExecutor.execute(() -> generateRowContent(row, setContents(o)));
        }

        for (int i = 0; i < titleArray.length; i++) {
            if (width == 0)
                sheet.autoSizeColumn(i);
            else
                sheet.setColumnWidth(i, width);
        }
        return this;
    }

    public ExcelFactory generateContent(List list) {
        return generateContent(list, 0);
    }

    public ExcelFactory generateTotalRow(int spanSize, String spanName, Object[] values) {
        if (spanSize > 1)
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, spanSize - 1));
        Row row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellValue(spanName);
        setContentStyle(cell);
        for (int i = 1; i <= spanSize; i++) {
            Cell t = row.createCell(i);
            setContentStyle(t);
        }


        for (int i = 0; i < values.length; i++) {
            generateContentCell(row, spanSize + i, values[i]);
        }
        return this;
    }

    /**
     * 生成主标题
     */
    public ExcelFactory generateMainTitle(String text) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, titleArray.length - 1));
        Cell cell = sheet.createRow(rowIndex++).createCell(0);
        cell.setCellValue(text);
        setMainTitleStyle(cell);
        cell.getRow().setHeight((short) 1000);
        return this;
    }

    /**
     * 生成机构信息
     *
     * @param orgName 机构名称
     */
    public ExcelFactory generateOrgTitle(String orgName) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, titleArray.length - 1));
        Cell cell = sheet.createRow(rowIndex++).createCell(0);
        cell.setCellValue(orgName);
        setOrgTitleStyle(cell);
        return this;
    }

    /**
     * 生成时间 title  请在generateOrgTitle()后调用
     *
     * @param timeStart
     * @param timeEnd
     */
    public ExcelFactory generateTimeTitle(String timeStart, String timeEnd) {
        String text = "           时间：";
        if (StringUtils.hasText(timeStart) && StringUtils.hasText(timeEnd)) {
            text += timeStart + "到" + timeEnd;
        } else if (StringUtils.hasText(timeStart)) {
            text += timeStart;
        } else if (StringUtils.hasText(timeEnd)) {
            text += timeEnd;
        } else {
            text = "        ";
        }
        Row currentRow;
        Cell cell;
        if (rowIndex > 1) {
            currentRow = sheet.getRow(rowIndex - 1);
            cell = currentRow.getCell(0);
            cell.setCellValue(cell.getStringCellValue() + text);
        } else {
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, titleArray.length - 1));
            cell = sheet.createRow(rowIndex++).createCell(0);
            cell.setCellValue(text);
        }
        setTimeTitleStyle(cell);
        return this;
    }

    /**
     * 列标题
     */
    public ExcelFactory generateArrayTitle() {
        Row row = sheet.createRow(rowIndex++);
        for (int i = 0; i < titleArray.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titleArray[i]);
            setArrayTitleStyle(cell);
        }
        return this;
    }

    /**
     * 生成内容row
     *
     * @param row
     * @param objects
     */
    private void generateRowContent(Row row, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            generateContentCell(row, i, objects[i]);
        }
    }

    /**
     * 生成内容cell
     *
     * @param row    所在行
     * @param cIndex 所在列
     * @param text   内容
     */
    private void generateContentCell(Row row, int cIndex, Object text) {
        Cell cell = row.createCell(cIndex);
        if (text instanceof String) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((String) text);
        } else if (text instanceof Double) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Double) text);

        } else if (text instanceof Integer) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Integer) text);
        } else if (text instanceof BigDecimal) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((BigDecimal) text).intValue());
        } else {
            //todo orther type
        }
        setContentStyle(cell);
    }

    private void generate5CellStyle() {
        //---------------------contentStyle
        CellStyle contentStyle = workbook.createCellStyle();
        Font contentFont = workbook.createFont();
        contentFont.setBold(false);
        contentFont.setFontHeightInPoints((short) 12);
        contentFont.setColor(HSSFFont.COLOR_NORMAL);
        contentFont.setFontName("宋体");
        contentStyle.setFont(contentFont);
//        contentStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        contentStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        contentStyle.setBorderBottom((short) 1);    //设置边框样式
//        contentStyle.setBorderLeft((short) 1);     //左边框
//        contentStyle.setBorderRight((short) 1);    //右边框
//        contentStyle.setBorderTop((short) 1);    //顶边框
        fonts[0] = contentFont;
        styles[0] = contentStyle;
        //---------------------ArrayTitleStyle
        CellStyle arrayTitleStyle = workbook.createCellStyle();
        Font arrayTitlefont = workbook.createFont();
        arrayTitlefont.setBold(false);
        arrayTitlefont.setFontHeightInPoints((short) 12);
        arrayTitlefont.setColor(HSSFFont.COLOR_NORMAL);
        arrayTitleStyle.setFont(arrayTitlefont);
//        arrayTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        arrayTitleStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
//        arrayTitleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        arrayTitleStyle.setBorderBottom((short) 1);    //设置边框样式
//        arrayTitleStyle.setBorderLeft((short) 1);     //左边框
//        arrayTitleStyle.setBorderRight((short) 1);    //右边框
//        arrayTitleStyle.setBorderTop((short) 1);    //顶边框
        fonts[1] = arrayTitlefont;
        styles[1] = arrayTitleStyle;
        //-----------------------TimeTitleStyle
        CellStyle timeTitleStyle = workbook.createCellStyle();
        Font timeTitlefont = workbook.createFont();
        timeTitlefont.setBold(false);
        timeTitlefont.setFontHeightInPoints((short) 12);
        timeTitlefont.setColor(HSSFFont.COLOR_NORMAL);
        timeTitleStyle.setFont(timeTitlefont);
//        timeTitleStyle.setAlignment(CellStyle.ALIGN_LEFT);
//        timeTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        timeTitleStyle.setBorderBottom((short) 0);    //设置边框样式
//        timeTitleStyle.setBorderLeft((short) 0);     //左边框
//        timeTitleStyle.setBorderRight((short) 0);    //右边框
//        timeTitleStyle.setBorderTop((short) 0);    //顶边框
        fonts[2] = timeTitlefont;
        styles[2] = timeTitleStyle;
        //--------------------------------OrgTitleStyle
        CellStyle orgTitleStyle = workbook.createCellStyle();
        Font orgTitlefont = workbook.createFont();
        orgTitlefont.setBold(false);
        orgTitlefont.setFontHeightInPoints((short) 12);
        orgTitlefont.setColor(HSSFFont.COLOR_NORMAL);
        orgTitleStyle.setFont(orgTitlefont);
//        orgTitleStyle.setAlignment(CellStyle.ALIGN_LEFT);
//        orgTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        orgTitleStyle.setBorderBottom((short) 0);    //设置边框样式
//        orgTitleStyle.setBorderLeft((short) 0);     //左边框
//        orgTitleStyle.setBorderRight((short) 0);    //右边框
//        orgTitleStyle.setBorderTop((short) 0);    //顶边框
        fonts[3] = orgTitlefont;
        styles[3] = orgTitleStyle;
        //----------------------------------MainTitleStyle
        CellStyle mainTitleStyle = workbook.createCellStyle();
        Font mainTitlefont = workbook.createFont();
        mainTitlefont.setBold(true);
        mainTitlefont.setFontHeightInPoints((short) 20);
        mainTitlefont.setColor(HSSFFont.COLOR_NORMAL);
        mainTitleStyle.setFont(mainTitlefont);
//        mainTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        mainTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        mainTitleStyle.setBorderBottom((short) 0);    //设置边框样式
//        mainTitleStyle.setBorderLeft((short) 0);     //左边框
//        mainTitleStyle.setBorderRight((short) 0);    //右边框
//        mainTitleStyle.setBorderTop((short) 0);    //顶边框
        fonts[4] = mainTitlefont;
        styles[4] = mainTitleStyle;
    }

    private void setContentStyle(Cell cell) {
        cell.setCellStyle(styles[0]);
    }

    private void setArrayTitleStyle(Cell cell) {
        cell.setCellStyle(styles[1]);
    }

    private void setTimeTitleStyle(Cell cell) {
        cell.setCellStyle(styles[2]);
    }

    private void setOrgTitleStyle(Cell cell) {
        cell.setCellStyle(styles[3]);
    }

    private void setMainTitleStyle(Cell cell) {
        cell.setCellStyle(styles[4]);
    }


}
