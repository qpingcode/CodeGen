package me.qping.utils.codegen.bean.build;

import lombok.Data;

import java.util.Map;

/**
 * @ClassName BuildConfig
 * @Author qping
 * @Date 2019/8/5 09:57
 * @Version 1.0
 **/
@Data
public class BuildConfig {

    public static final String ftlPath = "/schemas/%s/tpl";
    public static final String buildJsonPath = "/schemas/%s/build.json";

    String schema;
    String output;

    // user define params
    Map<String,Object> userParams;

    Copyright copyright;
    Table table;
    JavaFiles java;

    public static BuildConfig create(){
        return new BuildConfig();
    }
}
