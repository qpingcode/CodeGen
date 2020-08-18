package me.qping.utils.codegen;

import com.alibaba.fastjson.JSONObject;
import me.qping.utils.codegen.bean.build.*;
import me.qping.utils.codegen.generator.JavaGenerator;
import me.qping.utils.codegen.util.GenUtil;
import me.qping.utils.database.DataBaseUtilBuilder;
import me.qping.utils.database.metadata.bean.ColumnMeta;
import me.qping.utils.database.metadata.bean.TableMeta;
import me.qping.utils.database.util.MetaDataUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CodeGenUtil
 * @Author qping
 * @Date 2019/8/2 16:02
 * @Version 1.0
 **/
public class CodeGenUtil {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        // 获取数据表元数据
        MetaDataUtil metadataUtil = DataBaseUtilBuilder
                .mysql("192.168.1.201", "30306", "disease_report", "root", "rxthinkingmysql")
                .build();

        // 转换build.json
        String schemaName = "rxthinking-swagger";


        // 批量生成所有
//        List<TableMeta> tableMetas = metadataUtil.getTables();
//        for(TableMeta tableMeta: tableMetas){
//            doExecute(metadataUtil, tableMeta.getName(), schemaName);
//        }

        // 生成单个表
        doExecute(metadataUtil, "reg_pneumonia", schemaName);

    }

    private static void doExecute(MetaDataUtil metadataUtil, String tableName, String schemaName) throws IOException, SQLException {

        TableMeta tableMeta = metadataUtil.getTableInfo(tableName);

        InputStream stream = CodeGenUtil.class.getResourceAsStream(String.format(BuildConfig.buildJsonPath, schemaName));
        String json = IOUtils.toString(stream, "UTF-8");

        BuildConfig buildConfig = JSONObject.parseObject(json, BuildConfig.class);
        buildConfig.setSchema(schemaName);

        Table table = createBy(tableMeta, buildConfig.getJava().getIgnoreTableName(), buildConfig.getJava().getIgnoreColumnName());
        buildConfig.setTable(table);

        // 生成文件
        JavaFiles javaFiles = buildConfig.getJava();
        if(javaFiles != null){
            JavaGenerator javaGenerator = new JavaGenerator();
            for(JavaFile javaFile : javaFiles.getList()){
                javaGenerator.create(javaFile, buildConfig);
            }
        }
    }

    private static Table createBy(TableMeta tableMeta, String[] ignoreTableName, String[] ignoreColumnName) {
        // 将数据库返回的 TableMeta 和 ColumnMeta 转换简单的 Table 和 Column
        // 表名转驼峰命名
        String beanName = GenUtil.getCamelCase(tableMeta.getName(), ignoreTableName, true);
        String nameWithoutPrefix = GenUtil.getNameWithoutPrefix(tableMeta.getName(), ignoreTableName).toLowerCase();

        Table table = new Table();
        table.setName(tableMeta.getName());
        table.setNameLower(tableMeta.getNameLower());
        table.setComment(tableMeta.getComment());
        table.setCamelCase(beanName);
        table.setPrimaryKeys(tableMeta.getPrimaryKeys());
        table.setNameWithoutPrefix(nameWithoutPrefix);

        List<Column> columns = new ArrayList<>();
        table.setColumns(columns);

        for(ColumnMeta columnMeta : tableMeta.getColumns()){

            // 列名转驼峰命名
            String camelCase = GenUtil.getCamelCase(columnMeta.getName(), ignoreColumnName, false);

            Column column = new Column();
            column.setCamelCase(camelCase);
            column.setName(columnMeta.getName());
            column.setJavaType(columnMeta.getJavaType());
            column.setJavaPackage(columnMeta.getJavaPackage());
            column.setComment(columnMeta.getComment());
            column.setColumnDefine(columnMeta.getColumnDefinition());
            column.setPrimaryKey(columnMeta.isPrimaryKey());
            columns.add(column);
        }

        return table;
    }


}
