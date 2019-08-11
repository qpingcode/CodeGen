package ${java.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${javaRef.Service.javaPackage}.${javaRef.Service.javaName};
import ${javaRef.Bean.javaPackage}.${javaRef.Bean.javaName};

/**
 * @ClassName ${java.javaName}
 * @Description
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/${javaRef.Bean.javaName?uncap_first}")
public class ${java.javaName} {

    @Autowired
    ${javaRef.Service.javaName} ${javaRef.Service.javaName?uncap_first};

    @RequestMapping(value = "/findById")
    public Object findById(Integer id){
        return ${javaRef.Service.javaName?uncap_first}.findById(id);
    }

    @RequestMapping(value = "/findAll")
    public Object findAll(){
        return ${javaRef.Service.javaName?uncap_first}.findAll();
    }

    @RequestMapping(value = "/save")
    public Object save(${javaRef.Bean.javaName} ${javaRef.Bean.javaName?uncap_first}){
        return ${javaRef.Service.javaName?uncap_first}.save(${javaRef.Bean.javaName?uncap_first});
    }

    @RequestMapping(value = "/delete")
    public Object delete(${javaRef.Bean.javaName} ${javaRef.Bean.javaName?uncap_first}){
        return ${javaRef.Service.javaName?uncap_first}.delete(${javaRef.Bean.javaName?uncap_first});
    }




}