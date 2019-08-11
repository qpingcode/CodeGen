package me.qping.utils.codegen.bean.build;

import lombok.Data;

import java.util.List;

/**
 * @ClassName JavaFile
 * @Author qping
 * @Date 2019/8/5 09:14
 * @Version 1.0
 **/
@Data
public class JavaFiles {
    String basePath;
    String basePackage;

    String[] ignoreTableName;
    String[] ignoreColumnName;

    List<JavaFile> list;
}
