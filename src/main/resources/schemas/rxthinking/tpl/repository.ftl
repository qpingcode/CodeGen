package ${template.javaPackage};

import ${refs.bean.javaPackage}.${refs.bean.javaName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName ${template.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
public interface ${template.javaName} extends JpaRepository<${refs.bean.javaName}, Integer>, JpaSpecificationExecutor<${refs.bean.javaName}> {

}