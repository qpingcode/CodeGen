package me.qping.utils.codegen.bean.build;

import lombok.Data;
import me.qping.utils.database.metadata.bean.TableMeta;

import java.util.Set;

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

    Copyright copyright;
    TableMeta table;
    JavaFiles javas;

    public static BuildConfig create(){
        return new BuildConfig();
    }
}
