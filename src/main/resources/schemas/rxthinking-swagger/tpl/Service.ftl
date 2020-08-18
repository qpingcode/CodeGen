package ${java.file.javaPackage};

import com.rxthinking.common.anno.ServiceMapping;
import com.rxthinking.common.core.BusinessException;
import com.rxthinking.common.pojo.request.PageBean;
import com.rxthinking.common.pojo.response.PageData;
import com.rxthinking.common.service.BaseService;

import ${java.refs.Repository.javaPackage}.${java.refs.Repository.javaName};
import ${java.refs.Bean.javaPackage}.${java.refs.Bean.javaName};
import ${java.refs.BeanBO.javaPackage}.${java.refs.BeanBO.javaName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ${java.file.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Service
@ServiceMapping("${userParams.projectPrefix}_${table.nameWithoutPrefix}")
@Api(tags =  "${table.comment!''}")
@RequestMapping("/api/req/v1/${userParams.projectRabbitmqPath}")
public class ${java.file.javaName} extends BaseService{

    @Autowired
    ${java.refs.Repository.javaName} ${java.refs.Repository.javaName?uncap_first};

    @ApiOperation(value="查询")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:findById")
    public ${java.refs.Bean.javaName} findById(Integer id) {
        return ${java.refs.Repository.javaName?uncap_first}.findById(id).orElse(null);
    }

    @ApiOperation(value="保存")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:save")
    public boolean save(${java.refs.Bean.javaName} bean) {
        ${java.refs.Repository.javaName?uncap_first}.save(bean);
        return true;
    }

    @ApiOperation(value="保存多个")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:saveAll")
    public boolean saveAll(List<${java.refs.Bean.javaName}> list) {
        ${java.refs.Repository.javaName?uncap_first}.saveAll(list);
        return true;
    }

    @ApiOperation(value="删除")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:delete")
    public boolean delete(Integer id) {
        ${java.refs.Repository.javaName?uncap_first}.deleteById(id);
        return true;
    }

    public static final String COLUMS_ALLOW = "<#list table.columns as column>${column.camelCase!''}<#if column_has_next>,</#if></#list>";

    @ApiOperation(value="查询列表")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:findAll")
    public List<${java.refs.Bean.javaName}> findAll(${java.refs.BeanBO.javaName} bo) {
        // 排序
        Sort sort = createSort(bo.getOrderBy(), COLUMS_ALLOW);
        // 查询
        return ${java.refs.Repository.javaName?uncap_first}.findAll(getSpec(bo), sort);
    }

    @ApiOperation(value="查询分页")
    @PostMapping(value = "${userParams.projectPrefix}_${table.nameWithoutPrefix}:findPage")
    public PageData<${java.refs.Bean.javaName}> findPage(${java.refs.BeanBO.javaName} bo, PageBean pageBean) {
        // 排序
        Sort sort = createSort(bo.getOrderBy(), COLUMS_ALLOW);
        // 分页
        PageRequest pageable = PageRequest.of(pageBean.getPageNum(), pageBean.getPageSize(), sort);
        // 查询
        Page<${java.refs.Bean.javaName}> pageResult = ${java.refs.Repository.javaName?uncap_first}.findAll(getSpec(bo), pageable);
        // 拼装结果
        PageData<${java.refs.Bean.javaName}> pageData = new PageData<>();
        pageData.setPageNum(pageBean.getPageNum());
        pageData.setPageSize(pageBean.getPageSize());
        pageData.setData(pageResult.getContent());
        pageData.setTotalSize(pageResult.getTotalElements());
        return pageData;
    }

    private Specification<${java.refs.Bean.javaName}> getSpec(${java.refs.BeanBO.javaName} bo) {
        if(bo == null){
            throw new BusinessException("条件不能为空");
        }
        return new Specification<${java.refs.Bean.javaName}>() {
            @Override
            public Predicate toPredicate(Root<${java.refs.Bean.javaName}> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Predicate predicate = builder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();

                <#list table.columns as column>
                // ${column.comment}
                if (bo.get${column.camelCase?cap_first}() != null) {
                    expressions.add(builder.equal(root.get("${column.camelCase}"), bo.get${column.camelCase?cap_first}()));
                }
                </#list>

                return predicate;
            }
        };
    }

}