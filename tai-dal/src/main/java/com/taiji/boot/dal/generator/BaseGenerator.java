package com.taiji.boot.dal.generator;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public abstract class BaseGenerator {

    private final static String projectPath = System.getProperty("user.dir") + "/tai-dal/src/main/";

    private final static String modelName = "user";

    /**
     * 功能描述: 代码生成器全局配置
     *
     * @return : com.baomidou.mybatisplus.generator.config.GlobalConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 20:28
     */
    protected static GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/java")
                .setFileOverride(true)
                .setEnableCache(true).setAuthor("taiji").setBaseResultMap(true).setBaseColumnList(true)
                .setDateType(DateType.ONLY_DATE).setEntityName("%sEntity").setMapperName("%sMapper").setOpen(false);
        return globalConfig;
    }

    /**
     * 功能描述: 数据源配置
     *
     * @return : com.baomidou.mybatisplus.generator.config.DataSourceConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 20:29
     */
    protected static DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://47.107.226.43:3306/test?useUnicode=true&characterEncoding=utf-8")
                .setDriverName("com.mysql.jdbc.Driver").setUsername("root").setPassword("mysql970913..")
                .setDbType(DbType.MYSQL).setTypeConvert(new MySqlTypeConvert()).setDbQuery(new MySqlQuery());
        return dataSourceConfig;
    }

    /**
     * 功能描述: 自定义策略配置
     *
     * @return : com.baomidou.mybatisplus.generator.config.StrategyConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 22:57
     */
    protected static StrategyConfig getStrategyConfig() {
        StrategyConfig config = new StrategyConfig();
        config.setTablePrefix("tb_").setNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setEntityTableFieldAnnotationEnable(true);
        // todo 需要生成的表名-生成时修改
        config.setInclude("tb_user");
        return config;
    }

    /**
     * 功能描述: 包配置
     *
     * @return : com.baomidou.mybatisplus.generator.config.PackageConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 20:29
     */
    protected static PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.taiji.boot.dal.base").setModuleName(modelName);
        return packageConfig;
    }

    /**
     * 功能描述: 注入配置（主要用来在 resources 下生成 mapper.xml 文件）
     *
     * @return : com.baomidou.mybatisplus.generator.InjectionConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 20:30
     */
    protected static InjectionConfig getInjectionConfig() {
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        String mapperxmlPath = "/templates/mapper.xml.vm";
        List<FileOutConfig> focList = Lists.newArrayList();
        focList.add(new FileOutConfig(mapperxmlPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String entityName = tableInfo.getEntityName().contains("Entity") ?
                        tableInfo.getEntityName().substring(0, tableInfo.getEntityName().lastIndexOf("Entity")) : tableInfo.getEntityName();
                return projectPath + "/resources/mapper/" + getPackageConfig().getModuleName() + "/"
                        + entityName + "Mapper" + StringPool.DOT_XML;
            }
        });
        config.setFileOutConfigList(focList);
        return config;
    }

    /**
     * 功能描述: 自定义模板配置
     *
     * @return : com.baomidou.mybatisplus.generator.config.TemplateConfig
     * @author : yangyihui
     * @date : 2020/1/5 0005 20:30
     */
    protected static TemplateConfig getTemplateConfig() {
        TemplateConfig config = new TemplateConfig();
        config.setController(null)
                .setService(null)
                .setServiceImpl(null)
                .setXml(null);
        return config;
    }
}
