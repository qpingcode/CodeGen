package me.qping.utils.codegen.generator;

import lombok.extern.slf4j.Slf4j;
import me.qping.utils.codegen.bean.build.BuildConfig;
import me.qping.utils.codegen.bean.build.JavaFile;
import me.qping.utils.codegen.bean.build.JavaFiles;
import me.qping.utils.codegen.freemarker.FreemarkerUtil;
import me.qping.utils.codegen.util.GenUtil;
import me.qping.utils.database.metadata.bean.ColumnMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName JavaGenerator
 * @Description java 文件生成器
 * @Author qping
 * @Date 2019/8/5 10:36
 * @Version 1.0
 **/
@Slf4j
public class JavaGenerator {

    public static final String FILE_SEPARATOR = "/";
    public static final String TEMPLATE_SUFFIX = ".ftl";
    public static final String CONDITION_DEFAULT_PACKAGE = "me.qping.utils.codegen.generator.condition";

    public void create(JavaFile javaFile, BuildConfig config) {

        String condition = javaFile.getCondition();

        // 判断是否要生成该文件
        if(!judgeCondition(condition, config)){
            return;
        }

        // 构建 freemarker 输入数据模型，将 buildConfig 转为 freemarker 方便调用的格式
        Map dataModel = new HashMap<>();
        dataModel.put("table", config.getTable());
        dataModel.put("java", javaFile);
        dataModel.put("javaImports", config.getTable().getColumns().stream().map(v-> v.getJavaImport()).collect(Collectors.toSet()));
        dataModel.put("javaRef", javaFilesToMap(config.getJavas()));
        dataModel.put("copyright", config.getCopyright());

        // 对于java的预处理，将包路径转为路径，用于生成文件
        String javaPackage = notNull(config.getJavas().getBasePackage()) +
                (javaFile.getJavaPackage() == null ? "" : ("." + javaFile.getJavaPackage()));

        // 生成路径不包含 basePackage 包路径
        // String filePath = notNull(config.getJavas().getBasePath()) + FILE_SEPARATOR + javaPackage.replaceAll("\\.", FILE_SEPARATOR);
        String filePath = notNull(config.getJavas().getBasePath()) + FILE_SEPARATOR +
                javaFile.getJavaPackage().replaceAll("\\.", FILE_SEPARATOR);

        if(needFreemark(javaFile.getFileName())){
            String fileName = FreemarkerUtil.process(dataModel, javaFile.getFileName());
            javaFile.setFileName(fileName);
        }

        javaFile.setFilePath(filePath);
        javaFile.setJavaPackage(javaPackage);
        javaFile.setJavaName(GenUtil.getJavaName(javaFile.getFileName()));

        // 构建各种路径，文件输出路径，文件名，模版路径，模版名称
        String outputParentPath = notNull(config.getOutput()) + javaFile.getFilePath();
        String outputFileName = javaFile.getFileName();

        String ftlPath = String.format(BuildConfig.ftlPath, config.getSchema());
        String ftlName = javaFile.getFileTpl();
        ftlName = ftlName.endsWith(TEMPLATE_SUFFIX) ? ftlName : ftlName + TEMPLATE_SUFFIX;

        FreemarkerUtil.process(dataModel, ftlPath, ftlName, outputParentPath, outputFileName);

        log.info("generate file: " + outputParentPath + "/"+ javaFile.getFileName());
    }

    public static boolean needFreemark(String content){
        return Pattern.compile("(\\$\\{[\\s\\S]*\\})").matcher(content).find();
    }

    public Map javaFilesToMap(JavaFiles javaFiles){

        Map map = new HashMap();
        for(JavaFile javaFile : javaFiles.getList()){
            String key = javaFile.getKey();
            if(key == null){
                key = javaFile.getFileTpl();
            }
            map.put(key, javaFile);
        }

        return map;
    }

    private boolean judgeCondition(String condition, BuildConfig config) {
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

    public String notNull(String str){
        return str == null ? "" : str;
    }

}
