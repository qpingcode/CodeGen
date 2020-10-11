package ${template.javaPackage};

import ${refs.Bean.javaPackage}.${refs.Bean.javaName};
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ${template.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
public interface ${template.javaName} extends JpaRepository<${refs.Bean.javaName}, Integer> {

}