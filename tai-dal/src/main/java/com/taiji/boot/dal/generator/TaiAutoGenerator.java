package com.taiji.boot.dal.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;

public class TaiAutoGenerator extends BaseGenerator {
    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(getGlobalConfig())
                .setDataSource(getDataSourceConfig())
                .setPackageInfo(getPackageConfig())
                .setStrategy(getStrategyConfig())
                .setCfg(getInjectionConfig())
                .setTemplate(getTemplateConfig());
        generator.execute();
    }
}
