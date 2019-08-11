package ${java.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import ${javaRef.Dao.javaPackage}.${javaRef.Dao.javaName};
import ${javaRef.Bean.javaPackage}.${javaRef.Bean.javaName};

/**
 * @ClassName ${java.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Service
public class ${java.javaName} {

    @Autowired
    ${javaRef.Dao.javaName} ${javaRef.Dao.javaName?uncap_first};

    public ${javaRef.Bean.javaName} findById(Integer id) {
        return ${javaRef.Dao.javaName?uncap_first}.findById(id).orElse(null);
    }

    public List<${javaRef.Bean.javaName}> findAll() {
        return ${javaRef.Dao.javaName?uncap_first}.findAll();
    }

    public Object save(${javaRef.Bean.javaName} ${javaRef.Bean.javaName?uncap_first}) {
        return ${javaRef.Dao.javaName?uncap_first}.save(${javaRef.Bean.javaName?uncap_first});
    }

    public boolean delete(${javaRef.Bean.javaName} ${javaRef.Bean.javaName?uncap_first}) {
        ${javaRef.Dao.javaName?uncap_first}.delete(${javaRef.Bean.javaName?uncap_first});
        return true;
    }

}