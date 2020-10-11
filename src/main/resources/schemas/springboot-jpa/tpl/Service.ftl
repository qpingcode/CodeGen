package ${template.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import ${refs.Dao.javaPackage}.${refs.Dao.javaName};
import ${refs.Bean.javaPackage}.${refs.Bean.javaName};

/**
 * @ClassName ${template.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
@Service
public class ${template.javaName} {

    @Autowired
    ${refs.Dao.javaName} ${refs.Dao.javaName?uncap_first};

    public ${refs.Bean.javaName} findById(Integer id) {
        return ${refs.Dao.javaName?uncap_first}.findById(id).orElse(null);
    }

    public List<${refs.Bean.javaName}> findAll() {
        return ${refs.Dao.javaName?uncap_first}.findAll();
    }

    public Object save(${refs.Bean.javaName} ${refs.Bean.javaName?uncap_first}) {
        return ${refs.Dao.javaName?uncap_first}.save(${refs.Bean.javaName?uncap_first});
    }

    public boolean delete(${refs.Bean.javaName} ${refs.Bean.javaName?uncap_first}) {
        ${refs.Dao.javaName?uncap_first}.delete(${refs.Bean.javaName?uncap_first});
        return true;
    }

}