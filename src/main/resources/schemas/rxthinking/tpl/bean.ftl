package ${template.javaPackage};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                @Column(name = "${column.name!''}"<#if column.columnDefine??>,columnDefinition = "${column.columnDefine}"</#if>)
                @ApiModelProperty(value = "主键")
                private ${column.javaType!''} ${column.camelCase!''};

            </@align>
        <#else>
            <@align left=4>
                @Basic
                @Column(name = "${column.name!''}"<#if column.columnDefine??>,columnDefinition = "${column.columnDefine}"</#if>)
                @ApiModelProperty(value = "${column.comment!''}")
                private ${column.javaType!''} ${column.camelCase!''};

            </@align>
        </#if>
    </#list>

}