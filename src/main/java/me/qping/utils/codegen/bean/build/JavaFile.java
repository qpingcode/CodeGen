package me.qping.utils.codegen.bean.build;

import lombok.Data;

/**
 * @ClassName JavaFile
 * @Author qping
 * @Date 2019/8/5 09:14
 * @Version 1.0
 **/
@Data
public class JavaFile extends BaseFile {
    // 类的包路径和文件名
    String javaPackage;
    String javaName;
    // 主键的包路径和文件名
    String idPackage;
    String idClass;
}
