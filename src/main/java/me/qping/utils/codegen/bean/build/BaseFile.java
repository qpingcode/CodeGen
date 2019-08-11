package me.qping.utils.codegen.bean.build;

import lombok.Data;

/**
 * @ClassName BaseFile
 * @Author qping
 * @Date 2019/8/5 09:15
 * @Version 1.0
 **/
@Data
public class BaseFile {
    String condition;
    String key;
    String fileTpl;
    String fileName;
    String filePath;
}
