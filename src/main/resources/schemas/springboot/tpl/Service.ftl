package ${java.file.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.file.util.List;

import ${java.refs.Dao.javaPackage}.${java.refs.Dao.javaName};
import ${java.refs.Bean.javaPackage}.${java.refs.Bean.javaName};

/**
 * @ClassName ${java.file.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Service
public class ${java.file.javaName} {

    @Autowired
    ${java.refs.Dao.javaName} ${java.refs.Dao.javaName?uncap_first};

    public ${java.refs.Bean.javaName} findById(Integer id) {
        return ${java.refs.Dao.javaName?uncap_first}.findById(id).orElse(null);
    }

    public List<${java.refs.Bean.javaName}> findAll() {
        return ${java.refs.Dao.javaName?uncap_first}.findAll();
    }

    public Object save(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}) {
        return ${java.refs.Dao.javaName?uncap_first}.save(${java.refs.Bean.javaName?uncap_first});
    }

    public boolean delete(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}) {
        ${java.refs.Dao.javaName?uncap_first}.delete(${java.refs.Bean.javaName?uncap_first});
        return true;
    }

}