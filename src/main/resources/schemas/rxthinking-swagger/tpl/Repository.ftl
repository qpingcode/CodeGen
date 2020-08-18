package ${java.file.javaPackage};

import ${java.refs.Bean.javaPackage}.${java.refs.Bean.javaName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName ${java.file.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
public interface ${java.file.javaName} extends JpaRepository<${java.refs.Bean.javaName}, Integer>, JpaSpecificationExecutor<${java.refs.Bean.javaName}> {

}