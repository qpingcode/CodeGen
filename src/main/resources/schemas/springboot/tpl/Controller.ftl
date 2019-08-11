package ${java.javaPackage};

import ${javaRef.service.javaPackage}.${javaRef.service.javaName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName ${java.javaName}
 * @Description
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Controller
@RequestMapping(value = "/${javaRef.bean.javaName?uncap_first}")
public class ${java.javaName} {

    @Autowired
    ${javaRef.service.javaName} ${javaRef.service.javaName?uncap_first};

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test(String param, ModelMap map){
        return "ok";
    }




}