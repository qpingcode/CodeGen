package ${java.javaPackage};

import lombok.Data;
import javax.persistence.*;
<#list table.columns as column>
    <#if column.javaImport??>
        <@align>
            ${column.javaImport}
        </@align>
    </#if>
</#list>

/**
 * @ClassName ${java.javaName!''}
 * @Description ${table.comment!''}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Entity
@Table(name = "${table.name!''}")
@Data
public class ${java.javaName!''} {

    <#list table.columns as column>
        <#if column.primaryKey == true>
            <@align left=4>
                @Id
                @Column(name = "${column.name!''}")
                private ${column.javaType!''}  ${column.alias!''};

            </@align>
        <#else>
            <@align left=4>
                @Basic
                @Column(name = "${column.name!''}")
                private ${column.javaType!''}  ${column.alias!''}; <#if column.comment??>// ${column.comment}</#if>

            </@align>
        </#if>
    </#list>

}