package com.roman.excel.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.roman.excel.demo.mapper.MerchantInfoMapper;
import com.roman.excel.demo.pojo.MerchantInfo;
import com.roman.excel.demo.service.MerchantInfoService;
import com.roman.excel.demo.service.WriteExcelDataDelegated;
import com.roman.excel.demo.util.DateUtil;
import com.roman.excel.demo.util.PoiUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-10
 */
@Service
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements MerchantInfoService {

    @Override
    public void export_web_multi(MerchantInfo vo, HttpServletResponse response) throws Exception {
        // 总记录数
        Integer totalRowCount = count();

        // 导出EXCEL文件名称
        String filaName = "商户渠道EXCEL";

        // 标题
        String[] titles = {"主键", "名称", "地址", "电话", "钱钱", "描述", "状态", "法人代表"};

        // 开始导入
        PoiUtil.exportExcelToWebsite_multi(response, totalRowCount, filaName, titles, new WriteExcelDataDelegated() {
            @Override
            public void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {

                Page<MerchantInfo> page = new Page<>(currentPage, pageSize);
                QueryWrapper<MerchantInfo> qw = new QueryWrapper();
                IPage<MerchantInfo> page1 = page(page, qw);
                List<MerchantInfo> records = page1.getRecords();
                if (records.isEmpty()) {
                    return;
                }

                // --------------   这一块变量照着抄就行  强迫症 后期也封装起来     ----------------------
                for (int i = startRowCount; i <= endRowCount; i++) {
                    SXSSFRow eachDataRow = eachSheet.createRow(i);
                    if ((i - startRowCount) < records.size()) {

                        MerchantInfo mc = records.get(i - startRowCount);
                        // ---------   这一块变量照着抄就行  强迫症 后期也封装起来     -----------------------
                        setCellVal(eachDataRow.createCell(0),mc.getId());
                        setCellVal(eachDataRow.createCell(1),mc.getName());
                        setCellVal(eachDataRow.createCell(2),mc.getAddress());
                        setCellVal(eachDataRow.createCell(3),mc.getPhone());
                        setCellVal(eachDataRow.createCell(4),mc.getMoney());
                        setCellVal(eachDataRow.createCell(5),mc.getDescDesc());
                        setCellVal(eachDataRow.createCell(6),mc.getStatus());
                        setCellVal(eachDataRow.createCell(7),mc.getFarenPresent());
                    }
                }
            }
        });
    }

    @Override
    public void export_web(MerchantInfo vo, HttpServletResponse response) throws Exception {
        // 总记录数
        Integer totalRowCount = count();

        // 导出EXCEL文件名称
        String filaName = "商户渠道EXCEL";

        // 标题
        String[] titles = {"主键", "名称", "地址", "电话", "钱钱", "描述", "状态", "法人代表"};

        // 开始导入
        PoiUtil.exportExcelToWebsite(response, totalRowCount, filaName, titles, new WriteExcelDataDelegated() {
            @Override
            public void writeExcelData(SXSSFSheet eachSheet, Integer startRowCount, Integer endRowCount, Integer currentPage, Integer pageSize) throws Exception {

                Page<MerchantInfo> page = new Page<>(currentPage, pageSize);
                QueryWrapper<MerchantInfo> qw = new QueryWrapper();
                IPage<MerchantInfo> page1 = page(page, qw);
                List<MerchantInfo> records = page1.getRecords();
                if (records.isEmpty()) {
                    return;
                }

                // --------------   这一块变量照着抄就行  强迫症 后期也封装起来     ----------------------
                for (int i = startRowCount; i <= endRowCount; i++) {
                    SXSSFRow eachDataRow = eachSheet.createRow(i);
                    if ((i - startRowCount) < records.size()) {

                        MerchantInfo mc = records.get(i - startRowCount);
                        // ---------   这一块变量照着抄就行  强迫症 后期也封装起来     -----------------------
                        setCellVal(eachDataRow.createCell(0),mc.getId());
                        setCellVal(eachDataRow.createCell(1),mc.getName());
                        setCellVal(eachDataRow.createCell(2),mc.getAddress());
                        setCellVal(eachDataRow.createCell(3),mc.getPhone());
                        setCellVal(eachDataRow.createCell(4),mc.getMoney());
                        setCellVal(eachDataRow.createCell(5),mc.getDescDesc());
                        setCellVal(eachDataRow.createCell(6),mc.getStatus());
                        setCellVal(eachDataRow.createCell(7),mc.getFarenPresent());
                    }
                }
            }
        });
    }

    private void setCellVal(SXSSFCell cell,Object val) {
        cell.setCellValue(val == null? "" : val.toString());
    }

}
