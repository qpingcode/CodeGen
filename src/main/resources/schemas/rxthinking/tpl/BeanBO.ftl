package ${java.file.javaPackage};

import lombok.Data;
import javax.persistence.*;
<#list java.importPackages as package>
    <#if package??>
        <@align>
            import ${package};
        </@align>
    </#if>
</#list>

/**
 * @ClassName ${java.file.javaName!''}
 * @Description ${table.comment!''}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Data
public class ${java.file.javaName!''} {

    <#list table.columns as column>
        <@align left=4>
            private ${column.javaType!''}  ${column.camelCase!''};
        </@align>
    </#list>
    String orderBy;

}