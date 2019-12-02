package me.qping.utils.codegen.bean.build;

import lombok.Data;
import me.qping.utils.database.metadata.bean.ColumnMeta;
import me.qping.utils.database.metadata.bean.PrimaryKeyMeta;
import me.qping.utils.database.metadata.bean.TableMeta;

import java.util.List;

/**
 * @ClassName Column
 * @Description 字段属性扩展
 * @Author qping
 * @Date 2019/11/28 17:47
 * @Version 1.0
 **/
@Data
public class Column {
    String name;
    String comment;
    String camelCase;
    String javaPackage;
    String columnDefine;
    boolean primaryKey;
    String javaType;
}
