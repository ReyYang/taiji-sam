package com.taiji.boot.dal.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;

public class TaiAutoGenerator extends BaseGenerator {

    /**
     * 生成文件的包名即 com.taiji.boot.dal.base.${modelName}
     */
    private final static String modelName = "authority";

    /**
     * 数据库的表名 一定要带 tb_
     */
    private final static String tableName = "tb_permission_group";

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(getGlobalConfig())
                .setDataSource(getDataSourceConfig())
                .setPackageInfo(getPackageConfig(modelName))
                .setStrategy(getStrategyConfig(tableName))
                .setCfg(getInjectionConfig(modelName))
                .setTemplate(getTemplateConfig());
        generator.execute();
    }
}
