package me.qping.utils.codegen.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    String basePath = "/Users/qping/test/upload";

    @RequestMapping(value = { "", "/", "/index" })
    public String index(){
        return "index";
    }

    @RequestMapping(value = {"/pages/{path}"})
    public String pages(@PathVariable String path) {
        return "pages/" + path;
    }

    @RequestMapping(value = {"/pages/{moduleName}/{pageName}"})
    public String module(@PathVariable String moduleName, @PathVariable String pageName ) {
        return  "pages/" + moduleName + "/" + pageName;
    }

}
