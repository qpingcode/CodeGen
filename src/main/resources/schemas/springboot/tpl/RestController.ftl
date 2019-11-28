package ${java.file.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${java.refs.Service.javaPackage}.${java.refs.Service.javaName};
import ${java.refs.Bean.javaPackage}.${java.refs.Bean.javaName};

/**
 * @ClassName ${java.file.javaName}
 * @Description
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@RestController
@RequestMapping(value = "/${java.refs.Bean.javaName?uncap_first}")
public class ${java.file.javaName} {

    @Autowired
    ${java.refs.Service.javaName} ${java.refs.Service.javaName?uncap_first};

    @RequestMapping(value = "/findById")
    public Object findById(Integer id){
        return ${java.refs.Service.javaName?uncap_first}.findById(id);
    }

    @RequestMapping(value = "/findAll")
    public Object findAll(){
        return ${java.refs.Service.javaName?uncap_first}.findAll();
    }

    @RequestMapping(value = "/save")
    public Object save(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}){
        return ${java.refs.Service.javaName?uncap_first}.save(${java.refs.Bean.javaName?uncap_first});
    }

    @RequestMapping(value = "/delete")
    public Object delete(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}){
        return ${java.refs.Service.javaName?uncap_first}.delete(${java.refs.Bean.javaName?uncap_first});
    }




}