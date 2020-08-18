package me.qping.utils.codegen;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CodeGenApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CodeGenApp.class).run(args);
    }

}
