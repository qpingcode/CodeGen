package me.qping.utils.codegen.bean.build;

import lombok.Data;
import me.qping.utils.database.metadata.bean.ColumnMeta;
import me.qping.utils.database.metadata.bean.PrimaryKeyMeta;
import me.qping.utils.database.metadata.bean.TableMeta;

import java.util.List;

/**
 * @ClassName Table
 * @Description 表属性
 * @Author qping
 * @Date 2019/11/28 17:47
 * @Version 1.0
 **/
@Data
public class Table {
    String name;
    String nameLower;
    String nameWithoutPrefix;

    String camelCase;   // 首字母小写的驼峰明明

    String comment;
    List<Column> columns;
    List<PrimaryKeyMeta> primaryKeys;
}
