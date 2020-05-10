package com.roman.excel.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.roman.excel.demo.pojo.MerchantInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-10
 */
public interface MerchantInfoService extends IService<MerchantInfo> {

    void export_web(MerchantInfo vo, HttpServletResponse response) throws Exception;

    void export_web_multi(MerchantInfo vo, HttpServletResponse response) throws Exception;


}
