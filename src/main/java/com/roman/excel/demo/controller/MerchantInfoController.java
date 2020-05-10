package com.roman.excel.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roman.excel.demo.pojo.MerchantInfo;
import com.roman.excel.demo.service.MerchantInfoService;
import com.roman.excel.demo.util.ExcelFactory;
import lombok.Cleanup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2020-05-10
 */
@Controller
@RequestMapping("/merchantInfo")
public class MerchantInfoController {

    @Autowired
    private MerchantInfoService merchantInfoService;

    @GetMapping("/hello")
    @ResponseBody
    public Object hello() {
        return "hello world";
    }

    @GetMapping("/index")
    @ResponseBody
    public Object index() {
        return "index";
    }

    @GetMapping("/page")
    @ResponseBody
    public Object page(int cp) {
        Page<MerchantInfo> page = new Page<>(cp,10);
        QueryWrapper<MerchantInfo> qw = new QueryWrapper();
        IPage<MerchantInfo> page1 = merchantInfoService.page(page, qw);
        List<MerchantInfo> records = page1.getRecords();
        return records;
    }


    @RequestMapping(value = "/export_web")
    public void export_web(MerchantInfo vo, HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();
        merchantInfoService.export_web(vo, response);

        System.out.println("耗费时间： " + (System.currentTimeMillis() - startTime));

    }

    @RequestMapping(value = "/export_web_multi")
    public void export_web_multi(MerchantInfo vo, HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();
        merchantInfoService.export_web_multi(vo, response);

        System.out.println("耗费时间： " + (System.currentTimeMillis() - startTime));

    }


    @RequestMapping(value = "/export_my")
    public void export_my(MerchantInfo vo, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        int count = merchantInfoService.count();
        System.out.println("-----------------------" + count);
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        SXSSFSheet sheet = wb.createSheet();
        SXSSFRow title = sheet.createRow(0);
        SXSSFCell cell = title.createCell(0);
        cell.setCellValue("主键");
        SXSSFCell cell1 = title.createCell(1);
        cell1.setCellValue("名称");
        SXSSFCell cell2 = title.createCell(2);
        cell2.setCellValue("地址");
        SXSSFCell cell3 = title.createCell(3);
        cell3.setCellValue("电话");
        SXSSFCell cell4 = title.createCell(4);
        cell4.setCellValue("钱钱");
        SXSSFCell cell5 = title.createCell(5);
        cell5.setCellValue("描述");
        SXSSFCell cell6 = title.createCell(6);
        cell6.setCellValue("状态");
        SXSSFCell cell7 = title.createCell(7);
        cell7.setCellValue("法人代表");

        QueryWrapper<MerchantInfo> qw = new QueryWrapper<>();
//        qw.last("limit 100");
        List<MerchantInfo> list = merchantInfoService.list(qw);
        for (int i = 0; i < list.size(); i++) {
            MerchantInfo info = list.get(i);
            SXSSFRow row = sheet.createRow(i + 1);
            SXSSFCell cella = row.createCell(0);
            cella.setCellValue(info.getId());

            SXSSFCell cellb = row.createCell(1);
            cellb.setCellValue(info.getName());

            SXSSFCell cellc = row.createCell(2);
            cellc.setCellValue(info.getAddress());

            SXSSFCell celld = row.createCell(3);
            celld.setCellValue(info.getPhone());

            SXSSFCell celle = row.createCell(4);
            celle.setCellValue(info.getMoney());
            SXSSFCell cellf = row.createCell(5);
            cellf.setCellValue(info.getStatus());
            SXSSFCell cellg = row.createCell(6);
            cellg.setCellValue(info.getDescDesc());
            SXSSFCell cellh = row.createCell(7);
            cellh.setCellValue(info.getFarenPresent());
        }


        response.setHeader("content-disposition",
                "attachment;filename=" + new String("merchantInfo".getBytes("gbk"),
                        "iso8859-1")
                        + ".xls");

        OutputStream fOut = response.getOutputStream();
        wb.write(fOut);
        fOut.close();
        wb.dispose();

        System.out.println("耗费时间： " + (System.currentTimeMillis() - startTime));

    }


    @RequestMapping(value = "/export")
    public void export(MerchantInfo vo, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        QueryWrapper<MerchantInfo> qw = new QueryWrapper<>();
//        qw.last("limit 100");

        List<MerchantInfo> list = merchantInfoService.list(qw);
        System.out.println(list.size() + "---------");

        HSSFWorkbook workbook = new HSSFWorkbook();
        new ExcelFactory(workbook.createSheet("merchant"), new String[]{
                "主键",
                "名称",
                "地址",
                "电话",
                "钱钱",
                "描述",
                "状态",
                "法人代表",
        }) {
            @Override
            protected Object[] setContents(Object o) {
                if (o instanceof MerchantInfo) {
                    MerchantInfo merchantInfo = (MerchantInfo) o;
                    Object[] contents = {
                            merchantInfo.getId(),
                            merchantInfo.getName(),
                            merchantInfo.getAddress(),
                            merchantInfo.getPhone(),
                            merchantInfo.getMoney(),
                            merchantInfo.getDescDesc(),
                            merchantInfo.getStatus(),
                            merchantInfo.getFarenPresent()
                    };
                    return contents;
                }
                return new Object[0];
            }
        }.generateArrayTitle().generateContent(list);
        try {
            response.setHeader("content-disposition",
                    "attachment;filename=" + new String("JxcTakeBillDetail".getBytes("gbk"),
                            "iso8859-1")
                            + ".xls");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            @Cleanup OutputStream fOut = response.getOutputStream();
            workbook.write(fOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("耗费时间： " + (System.currentTimeMillis() - startTime));

    }

}

