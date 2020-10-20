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
@Data
@ApiModel(value = "${table.comment!'业务对象BO'} - ${table.name!''}")
public class ${template.javaName!''} {

    <#list table.columns as column>
        <@align left=4>
            @ApiModelProperty(value = "${column.comment!''}")
            private ${column.javaType!''}  ${column.camelCase!''};

        </@align>
    </#list>

    @ApiModelProperty(value = "排序方式, 可选列: <#list table.columns as column>${column.camelCase!''}<#if column_has_next>,</#if></#list>  可选排序: desc,asc; 示例：id asc")
    String orderBy;
}