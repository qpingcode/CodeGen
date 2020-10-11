package me.qping.utils.codegen.generator.lang;

import lombok.Data;
import me.qping.utils.codegen.bean.build.Template;

import java.util.Set;

@Data
public class JavaFile extends Template {

    Set<String> importPackages;
    String javaPackage;
    String javaName;

}
