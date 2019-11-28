package ${java.file.javaPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.file.util.List;

import ${java.refs.Repository.javaPackage}.${java.refs.Repository.javaName};
import ${java.refs.Bean.javaPackage}.${java.refs.Bean.javaName};

import com.rxthinking.common.anno.ServiceMapping;
import com.rxthinking.common.pojo.request.RequestParams;
import com.rxthinking.common.service.BaseService;

/**
 * @ClassName ${java.file.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Service
@ServiceMapping("${userParams.projectPrefix}")
public class ${java.file.javaName} {

    @Autowired
    ${java.refs.Repository.javaName} ${java.refs.Repository.javaName?uncap_first};

    public Object findById(RequestParams requestParams) {
        return ${java.refs.Repository.javaName?uncap_first}.findById(id).orElse(null);
    }

    public List<${java.refs.Bean.javaName}> findAll() {
        return ${java.refs.Repository.javaName?uncap_first}.findAll();
    }

    public Object save(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}) {
        return ${java.refs.Repository.javaName?uncap_first}.save(${java.refs.Bean.javaName?uncap_first});
    }

    public boolean delete(${java.refs.Bean.javaName} ${java.refs.Bean.javaName?uncap_first}) {
        ${java.refs.Repository.javaName?uncap_first}.delete(${java.refs.Bean.javaName?uncap_first});
        return true;
    }

}