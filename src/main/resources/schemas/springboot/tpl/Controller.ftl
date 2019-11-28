package ${java.file.javaPackage};

import ${java.refs.Service.javaPackage}.${java.refs.Service.javaName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ${java.file.javaName}
 * @Description
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/${java.refs.Bean.javaName?uncap_first}")
public class ${java.file.javaName} {

    @Autowired
    ${java.refs.Service.javaName} ${java.refs.Service.javaName?uncap_first};

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(String param, ModelMap map){
        return "ok";
    }




}