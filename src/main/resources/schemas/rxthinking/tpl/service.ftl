package ${template.javaPackage};

import com.rxthinking.common.anno.ServiceMapping;
import com.rxthinking.common.core.BusinessException;
import com.rxthinking.common.pojo.request.PageBean;
import com.rxthinking.common.pojo.response.PageData;
import com.rxthinking.common.service.BaseService;

import ${refs.repository.javaPackage}.${refs.repository.javaName};
import ${refs.bean.javaPackage}.${refs.bean.javaName};
import ${refs.beanBO.javaPackage}.${refs.beanBO.javaName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName ${template.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version ${copyright.version!''}
 **/
@Service
@ServiceMapping("${params.projectNameEn}_${table.nameWithoutPrefix?uncap_first}")
@Api(tags =  "${table.comment!''}")
@RequestMapping("/api/req/v1/${params.projectNameEn}")
public class ${template.javaName} extends BaseService{

    @Autowired
    ${refs.repository.javaName} ${refs.repository.javaName?uncap_first};

    public static final String COLUMS_ALLOW = "<#list table.columns as column>${column.camelCase!''}<#if column_has_next>,</#if></#list>";

    @ApiOperation(value="查询")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:findById")
    public ${refs.bean.javaName} findById(@ApiParam("${table.comment!''}主键") Integer id) {
        return ${refs.repository.javaName?uncap_first}.findById(id).orElse(null);
    }

    @ApiOperation(value="保存")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:save")
    public boolean save(${refs.bean.javaName} bean) {
        ${refs.repository.javaName?uncap_first}.save(bean);
        return true;
    }

    @ApiOperation(value="保存多个")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:saveAll")
    public boolean saveAll(List<${refs.bean.javaName}> list) {
        ${refs.repository.javaName?uncap_first}.saveAll(list);
        return true;
    }

    @ApiOperation(value="删除")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:delete")
    public boolean delete(@ApiParam("${table.comment!''}主键") Integer id) {
        ${refs.repository.javaName?uncap_first}.deleteById(id);
        return true;
    }

    @ApiOperation(value="查询列表")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:findAll")
    public List<${refs.bean.javaName}> findAll(${refs.beanBO.javaName} bo) {
        // 排序
        Sort sort = createSort(bo.getOrderBy(), COLUMS_ALLOW);
        // 查询
        return ${refs.repository.javaName?uncap_first}.findAll(getSpec(bo), sort);
    }

    @ApiOperation(value="查询分页")
    @PostMapping(value = "${params.projectNameEn}_${table.nameWithoutPrefix}:findPage")
    public PageData<${refs.bean.javaName}> findPage(${refs.beanBO.javaName} bo, PageBean pageBean) {
        // 排序
        Sort sort = createSort(bo.getOrderBy(), COLUMS_ALLOW);
        // 分页
        PageRequest pageable = PageRequest.of(pageBean.getPageNum(), pageBean.getPageSize(), sort);
        // 查询
        Page<${refs.bean.javaName}> pageResult = ${refs.repository.javaName?uncap_first}.findAll(getSpec(bo), pageable);
        // 拼装结果
        PageData<${refs.bean.javaName}> pageData = new PageData<>();
        pageData.setPageNum(pageBean.getPageNum());
        pageData.setPageSize(pageBean.getPageSize());
        pageData.setData(pageResult.getContent());
        pageData.setTotalSize(pageResult.getTotalElements());
        return pageData;
    }

    private Specification<${refs.bean.javaName}> getSpec(${refs.beanBO.javaName} bo) {
        if(bo == null){
            throw new BusinessException("条件不能为空");
        }
        return new Specification<${refs.bean.javaName}>() {
            @Override
            public Predicate toPredicate(Root<${refs.bean.javaName}> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

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