package me.qping.utils.codegen.bean.build;

import lombok.Data;
import me.qping.utils.codegen.generator.Condition;

/**
 * @ClassName BaseFile
 * @Author qping
 * @Date 2019/8/5 09:15
 * @Version 1.0
 **/
@Data
public class Template {

    public static final String CONDITION_DEFAULT_PACKAGE = "me.qping.utils.codegen.generator.condition";
    public static final String TEMPLATE_SUFFIX = ".ftl";

    String fileKey;
    String fileName;

    // 二者任选其一
    String filePath;
    String filePackage;

    String condition;


    // runtime
    boolean generateFileFlag = false;        // 是否生成文件


    public String getFtlName(){
        return fileKey.endsWith(TEMPLATE_SUFFIX) ? fileKey : fileKey + TEMPLATE_SUFFIX;
    }


}
