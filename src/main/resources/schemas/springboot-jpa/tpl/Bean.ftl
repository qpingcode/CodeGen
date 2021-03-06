package ${template.javaPackage};

import lombok.Data;
import javax.persistence.*;
<#list template.importPackages as package>
    <#if package??>
        <@align>
            import ${package};
        </@align>
    </#if>
</#list>

/**
 * @ClassName ${template.javaName!''}
 * @Description ${table.comment!''}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
@Entity
@Table(name = "${table.name!''}")
@Data
public class ${template.javaName!''} {

    <#list table.columns as column>
        <#if column.primaryKey == true>
            <@align left=4>
                @Id
                @Column(name = "${column.name!''}")
                private ${column.javaType!''}  ${column.camelCase!''};

            </@align>
        <#else>
            <@align left=4>
                @Basic
                @Column(name = "${column.name!''}"<#if column.columnDefine??>,columnDefinition = "${column.columnDefine}"</#if>)
                private ${column.javaType!''}  ${column.camelCase!''};<#if column.comment??> // ${column.comment}</#if>

            </@align>
        </#if>
    </#list>

}