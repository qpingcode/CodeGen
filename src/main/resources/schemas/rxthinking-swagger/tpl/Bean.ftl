package ${java.file.javaPackage};

import lombok.Data;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity
@Table(name = "${table.name!''}")
@ApiModel(value = "${table.comment!'数据库实体对象Bean'} - ${table.name!''}")
@Data
public class ${java.file.javaName!''} {

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