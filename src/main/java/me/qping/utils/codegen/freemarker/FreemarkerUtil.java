package me.qping.utils.codegen.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @ClassName FreemarkerUtil
 * @Author qping
 * @Date 2019/8/5 11:45
 * @Version 1.0
 **/
public class FreemarkerUtil {

    public static Configuration getPackageConfiguration(String templateBasePackagePath){
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setClassForTemplateLoading(FreemarkerUtil.class, templateBasePackagePath);
        cfg.setSharedVariable("align", new AlignDirective());
        return cfg;
    }

    public static Configuration getStringConfiguration(){
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateLoader(new StringTemplateLoader());
        cfg.setSharedVariable("align", new AlignDirective());
        return cfg;
    }

    public static String process(Object dataModel, String stringTemplate) {
        try {
            Configuration cfg = getStringConfiguration();
            StringWriter stringWriter = new StringWriter();
            Template template = new Template("tempStringTemplate", stringTemplate, cfg);
            template.process(dataModel, stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean process(Object dataModel, String templateBasePackagePath, String templateName, String outputPath, String outputFileName){

        try {
            Configuration cfg = getPackageConfiguration(templateBasePackagePath);
            File parent = new File(outputPath);

            if(!parent.exists()){
                parent.mkdirs();
            }

            Template temp = cfg.getTemplate(templateName);
            temp.process(dataModel, new FileWriter(new File(outputPath, outputFileName)));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return false;
    }

}
