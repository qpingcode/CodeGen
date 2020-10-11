package ${template.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${refs.Service.javaPackage}.${refs.Service.javaName};
import ${refs.Bean.javaPackage}.${refs.Bean.javaName};

/**
 * @ClassName ${template.javaName}
 * @Description
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
@RestController
@RequestMapping(value = "/${refs.Bean.javaName?uncap_first}")
public class ${template.javaName} {

    @Autowired
    ${refs.Service.javaName} ${refs.Service.javaName?uncap_first};

    @RequestMapping(value = "/findById")
    public Object findById(Integer id){
        return ${refs.Service.javaName?uncap_first}.findById(id);
    }

    @RequestMapping(value = "/findAll")
    public Object findAll(){
        return ${refs.Service.javaName?uncap_first}.findAll();
    }

    @RequestMapping(value = "/save")
    public Object save(${refs.Bean.javaName} ${refs.Bean.javaName?uncap_first}){
        return ${refs.Service.javaName?uncap_first}.save(${refs.Bean.javaName?uncap_first});
    }

    @RequestMapping(value = "/delete")
    public Object delete(${refs.Bean.javaName} ${refs.Bean.javaName?uncap_first}){
        return ${refs.Service.javaName?uncap_first}.delete(${refs.Bean.javaName?uncap_first});
    }

}