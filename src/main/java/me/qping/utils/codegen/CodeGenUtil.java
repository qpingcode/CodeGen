package me.qping.utils.codegen;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.qping.utils.codegen.bean.build.*;
import me.qping.utils.codegen.freemarker.FreemarkerUtil;
import me.qping.utils.codegen.generator.Condition;
import me.qping.utils.codegen.generator.lang.Decorator;
import me.qping.utils.codegen.util.GenUtil;
import me.qping.utils.codegen.util.SnowFlakeId;
import me.qping.utils.codegen.util.ZipUtil;
import me.qping.utils.database.DataBaseUtilBuilder;
import me.qping.utils.database.metadata.bean.ColumnMeta;
import me.qping.utils.database.metadata.bean.TableMeta;
import me.qping.utils.database.util.MetaDataUtil;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static me.qping.utils.codegen.bean.build.Template.CONDITION_DEFAULT_PACKAGE;

/**
 * @ClassName CodeGenUtil
 * @Author qping
 * @Date 2019/8/2 16:02
 * @Version 1.0
 **/
@Slf4j
public class CodeGenUtil {

    static SnowFlakeId worker = new SnowFlakeId(2, 1, 1);

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        // 获取数据表元数据
        MetaDataUtil metadataUtil = DataBaseUtilBuilder
                .mysql("192.168.1.201", "30306", "data_transform", "root", "rxthinkingmysql")
                .build();

        TableMeta tableMeta = metadataUtil.getTableInfo("dt_data_dept");

        String schemaName = "rxthinking";

        // 用户自定义参数
        Map<String,String> userParams = new HashMap<>();
        userParams.put("projectPrefix", "datatrans");
        userParams.put("basePackage", "com.rxthinking.datatrans");

        // 版权信息
        Copyright copyright = new Copyright();
        copyright.setAuthor("qping");
        copyright.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        copyright.setVersion("1.0");

        // 生成单个表
        long batchId = doExecute(userParams, tableMeta, schemaName, copyright);


        // 压缩文件
        ZipUtil.compress(CodeGenUtil.addSplit(CodeGenUtil.notNull(BuildConfig.outputPath)) + batchId);


        // 批量生成所有
//        List<TableMeta> tableMetas = metadataUtil.getTables();
//        for(TableMeta tableMeta: tableMetas){
//            doExecute(userParams, tableMeta, schemaName, copyright);
//        }

    }

    public static long doExecute(Map<String,String> userParams, TableMeta tableMeta, String schemaName, Copyright copyright) throws IOException {

        long batchId = worker.nextId();

        InputStream stream = CodeGenUtil.class.getResourceAsStream(String.format(BuildConfig.buildJsonPath, schemaName));
        String json = IOUtils.toString(stream, "UTF-8");


        BuildConfig buildConfig = JSONObject.parseObject(json, BuildConfig.class);
        buildConfig.getParams().putAll(userParams);
        buildConfig.setCopyright(copyright);

        // 初始化参数变量
        Map dataModel = init(buildConfig, tableMeta);

        // 生成文件
        for(Template template : buildConfig.getTemplates()){

            if(!template.isGenerateFileFlag()){
                continue;
            }

            // 当前template塞入上下文
            dataModel.put("template", template);


            // 文件输出路径，文件名
            String outputParentPath = BuildConfig.getOutputPath() + addSplit(batchId + "") + template.getFilePath();
            String outputFileName = template.getFileName();

            // 模版路径，模版名称
            String ftlPath = String.format(BuildConfig.ftlPath, schemaName);
            String ftlName = template.getFtlName();


            FreemarkerUtil.process(dataModel, ftlPath, ftlName, outputParentPath, outputFileName);
            log.info("generate file: " + outputParentPath + "/"+ outputFileName);
        }

        return batchId;
    }

    public static String addSplit(String path) {
        if(path.endsWith("/")){
            return path;
        }
        return path + "/";
    }

    private static Map init(BuildConfig config, TableMeta tableMeta) {
        List<Template> templates = config.getTemplates();

        if(templates == null || templates.size() == 0){
            throw new RuntimeException("无法生成代码，没有定义文件列表");
        }

        String ignoreTableNames = config.getParam("ignoreTableName");
        String ignoreColumnNames = config.getParam("ignoreColumnName");
        Table table = createBy(tableMeta, ignoreTableNames, ignoreColumnNames);

        config.setTable(table);

        Map dataModel = new HashMap<>();
        dataModel.put("params", config.getParams());
        dataModel.put("copyright", config.getCopyright());
        dataModel.put("table", table);

        for(int i = 0; i < templates.size(); i++){
            Template template = templates.get(i);

            // fileName
            if(template.getFileName() != null && needFreemark(template.getFileName())){
                String fileName = FreemarkerUtil.process(dataModel, template.getFileName());
                template.setFileName(fileName);
            }

            // filePackage
            if(template.getFilePackage() != null && needFreemark(template.getFilePackage())){
                String filePackage = FreemarkerUtil.process(dataModel, template.getFilePackage());
                template.setFilePackage(filePackage);
            }

            // filePath
            if(template.getFilePath() != null && needFreemark(template.getFilePath())){
                String filePath = FreemarkerUtil.process(dataModel, template.getFilePath());
                template.setFilePath(filePath);
            }

            // generateFileFlag
            if(judgeCondition(template.getCondition(), config)){
                template.setGenerateFileFlag(true);
            }

            String fileExt = getFileExt(template.getFileName());

            template = Decorator.create(fileExt, template, config);
            templates.set(i, template);
        }

        dataModel.put("refs", listToMap(config.getTemplates()));

        return dataModel;

    }


    public static Table createBy(TableMeta tableMeta, String ignoreTableNames, String ignoreColumnNames) {

        String[] ignoreTableNameArr = ignoreTableNames == null ? new String[0] : ignoreTableNames.split(",");
        String[] ignoreColumnNameArr = ignoreColumnNames == null ? new String[0] : ignoreColumnNames.split(",");

        // 将数据库返回的 TableMeta 和 ColumnMeta 转换简单的 Table 和 Column
        // 表名转驼峰命名
        String camelCase = GenUtil.getCamelCase(tableMeta.getName(), ignoreTableNameArr, true);
        String nameWithoutPrefix = GenUtil.getNameWithoutPrefix(tableMeta.getName(), ignoreTableNameArr).toLowerCase();

        Table table = new Table();
        table.setName(tableMeta.getName());
        table.setNameLower(tableMeta.getName().toLowerCase());
        table.setCamelCase(camelCase);
        table.setComment(tableMeta.getComment());
        table.setPrimaryKeys(tableMeta.getPrimaryKeys());
        table.setNameWithoutPrefix(nameWithoutPrefix);

        List<Column> columns = new ArrayList<>();
        table.setColumns(columns);

        for(ColumnMeta columnMeta : tableMeta.getColumns()){

            // 列名转驼峰命名
            String camelCaseCol = GenUtil.getCamelCase(columnMeta.getName(), ignoreColumnNameArr, false);

            Column column = new Column();
            column.setName(columnMeta.getName());
            column.setCamelCase(camelCaseCol);
            column.setJavaType(columnMeta.getJavaType());
            column.setJavaPackage(columnMeta.getJavaPackage());
            column.setComment(columnMeta.getComment());
            column.setColumnDefine(columnMeta.getColumnDefinition());
            column.setPrimaryKey(columnMeta.isPrimaryKey());
            columns.add(column);
        }
        return table;
    }

    public static boolean needFreemark(String content){
        return Pattern.compile("(\\$\\{[\\s\\S]*\\})").matcher(content).find();
    }

    public static Map listToMap(List<Template> templates){
        Map map = new HashMap();
        for(Template t : templates){
            String key = t.getFileKey();
            map.put(key, t);
        }
        return map;
    }

    public static boolean judgeCondition(String condition, BuildConfig config) {
        if(condition == null || "".equals(condition)) return true;

        if(condition.startsWith("@")){
            condition = condition.substring(1, condition.length());
            condition = CONDITION_DEFAULT_PACKAGE + condition;
        }

        try {
            Class clazz = Class.forName(condition);
            Condition conditionInstance = (Condition) clazz.newInstance();
            return conditionInstance.judge(config);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String notNull(String str){
        return str == null ? "" : str;
    }

    public static String getFileExt(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index > -1 && (index + 1) <= fileName.length()) {
            return fileName.substring(index + 1, fileName.length());
        } else {
            return "";
        }
    }

}
