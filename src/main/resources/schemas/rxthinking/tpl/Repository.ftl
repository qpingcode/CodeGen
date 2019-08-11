package ${java.javaPackage};

import ${javaRef.Bean.javaPackage}.${javaRef.Bean.javaName};
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ClassName ${java.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
public interface ${java.javaName} extends JpaRepository<${javaRef.Bean.javaName}, Integer> {

}