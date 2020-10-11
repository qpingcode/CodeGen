package me.qping.utils.codegen.generator.lang;

import me.qping.utils.codegen.bean.build.BuildConfig;
import me.qping.utils.codegen.bean.build.Template;

import java.util.stream.Collectors;

public class Decorator {


    public static Template create(String fileExt, Template template, BuildConfig config) {

        if(fileExt == null || "".equals(fileExt)) return template;

        switch(fileExt){
            case "java":
                JavaFile javaFile = new JavaFile();
                javaFile.setFileKey(template.getFileKey());
                javaFile.setFileName(template.getFileName());
                javaFile.setCondition(template.getCondition());
                javaFile.setFilePackage(template.getFilePackage());
                javaFile.setFilePath(template.getFilePath());
                javaFile.setGenerateFileFlag(template.isGenerateFileFlag());

                // java 专属 properties
                javaFile.setJavaPackage(template.getFilePackage());
                javaFile.setJavaName(template.getFileName().substring(0, template.getFileName().length() - 5));
                javaFile.setImportPackages(config.getTable().getColumns().stream().map(v-> v.getJavaPackage()).collect(Collectors.toSet()));

                return javaFile;


        }

        return template;
    }
}
