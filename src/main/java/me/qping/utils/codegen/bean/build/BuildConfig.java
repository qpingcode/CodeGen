package me.qping.utils.codegen.bean.build;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BuildConfig
 * @Author qping
 * @Date 2019/8/5 09:57
 * @Version 1.0
 **/
@Data
public class BuildConfig {


    public static final String outputPath = "/Users/qping/test/gencode";
    public static final String ftlPath = "/schemas/%s/tpl";
    public static final String buildJsonPath = "/schemas/%s/build.json";

    // build.json
    Map<String,String> params;
    List<Template> templates;

    // runtime
    Copyright copyright;
    Table table;

    public static BuildConfig create(){
        return new BuildConfig();
    }

    public String getParam(String key){
        if(params != null && params.containsKey(key)){
            return params.get(key);
        }
        return null;
    }

    public static String getOutputPath(){
        if(outputPath == null) return "";
        return addSplit(outputPath);
    }

    public static String addSplit(String path) {
        if(path.endsWith("/")){
            return path;
        }
        return path + "/";
    }
}
