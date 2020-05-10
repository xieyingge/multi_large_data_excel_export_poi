package com.roman.excel.demo.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Foo {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    //    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DATA_URL = "jdbc:mysql://127.0.0.1:3306/xyg?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    //    private static final String DATA_URL = "jdbc:oracle:thin:@szoraqa01.skystartrade.com:1521/sstqa01";
    private static final String USERNAME = "root";
    //    private static final String USERNAME = "skystar";
    private static final String PASSWORD = "root";
    //    private static final String PASSWORD = "skystar2018";
    private static final String projectPath = System.getProperty("user.dir");//获取项目路劲
    private static final String ROOT_DIR = projectPath + "/generator"; //生成文件输出路劲，会在项目路劲下，生成一个generator文件夹，然后对应下面配置的报名，xml文件下生产文件

    public static void main(String[] args) {

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName(DRIVER);
        dsc.setUrl(DATA_URL);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(ROOT_DIR);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
//        gc.setAuthor(AUTHOR);
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");

        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[]{"T_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{"t_merchant_info"}); // 需要生成的表   上面加了前缀 那生成的 实体等名字为 SysUser,没设置前缀属性就是 TsysUser

        PackageConfig pc = new PackageConfig();
        pc.setParent("com.roman.excel.demo");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("pojo");
        pc.setMapper("mapper");

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-rb");
                this.setMap(map);
            }
        };
//xml生成路径
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return ROOT_DIR + "/xml/" + tableInfo.getEntityName() + "Mapper.xml";
//                return "src/main/resources/"+"/mybatis/tables/"+tableInfo.getEntityName()+"Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);


// 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        AutoGenerator mpg = new AutoGenerator();
        mpg.setCfg(cfg);
        mpg.setDataSource(dsc);            //数据源配置
        mpg.setGlobalConfig(gc);        //全局配置
        mpg.setStrategy(strategy);        //生成策略配置
        mpg.setPackageInfo(pc);            //包配置
        mpg.setCfg(cfg);                //xml配置
        mpg.setTemplate(tc);            //
// 执行生成
        mpg.execute();
    }
}
