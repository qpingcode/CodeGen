package me.qping.utils.codegen.bean.datagrab;

import lombok.Data;
import me.qping.utils.excel.anno.Excel;

/**
 * @ClassName Statis
 * @Description TODO
 * @Author qping
 * @Date 2021/5/13 01:10
 * @Version 1.0
 **/
@Data
public class Statis {

    @Excel(name = "机构id")
    int orgId;

    @Excel(name = "机构名")
    String orgName;

    @Excel(name = "表名")
    String tableName ;

    @Excel(name = "输入")
    int il;

    @Excel(name = "输出")
    int ol;

    @Excel(name = "天数")
    int days;
}
