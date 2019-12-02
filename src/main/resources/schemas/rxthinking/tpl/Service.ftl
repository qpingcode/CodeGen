package ${java.file.javaPackage};

import com.rxthinking.common.anno.ServiceMapping;
import com.rxthinking.common.core.BusinessException;
import com.rxthinking.common.pojo.request.PageBean;
import com.rxthinking.common.pojo.request.RequestParams;
import com.rxthinking.common.pojo.request.RequestParamsPage;
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

/**
 * @ClassName ${java.file.javaName}
 * @Author ${copyright.author!''}
 * @Date ${copyright.date!''}
 * @Version 1.0
 **/
@Service
@ServiceMapping("${userParams.projectPrefix}${table.nameWithoutPrefix}")
public class ${java.file.javaName} extends BaseService{

    @Autowired
    ${java.refs.Repository.javaName} ${java.refs.Repository.javaName?uncap_first};

    public ${java.refs.Bean.javaName} findById(RequestParams requestParams) {
        // 获取入参
        Integer id = getParam(requestParams, "id");
        // 查询
        return ${java.refs.Repository.javaName?uncap_first}.findById(id).orElse(null);
    }

    public boolean save(RequestParams requestParams) {
        // 获取入参
        ${java.refs.Bean.javaName} bean = getParamsBO(requestParams, ${java.refs.Bean.javaName}.class);
        // 保存
        ${java.refs.Repository.javaName?uncap_first}.save(bean);
        return true;
    }

    public boolean saveAll(RequestParams requestParams) {
        // 获取入参
        List<${java.refs.Bean.javaName}> list = getParamsListBO(requestParams, "list", ${java.refs.Bean.javaName}.class);
        // 保存
        ${java.refs.Repository.javaName?uncap_first}.saveAll(list);
        return true;
    }

    public boolean delete(RequestParams requestParams) {
        // 获取入参
        Integer id = getParam(requestParams, "id");
        // 删除
        ${java.refs.Repository.javaName?uncap_first}.deleteById(id);
        return true;
    }

    public List<${java.refs.Bean.javaName}> findAll(RequestParams requestParams) {
        // 获取入参
        ${java.refs.BeanBO.javaName} bo = getParamsBO(requestParams, ${java.refs.BeanBO.javaName}.class);
        // 排序
        Sort sort = createSort(bo.getOrderBy());
        // 查询
        return ${java.refs.Repository.javaName?uncap_first}.findAll(getSpec(bo), sort);
    }

    public PageData<${java.refs.Bean.javaName}> findPage(RequestParamsPage requestParams) {
        // 获取入参
        ${java.refs.BeanBO.javaName} bo = getParamsBO(requestParams, ${java.refs.BeanBO.javaName}.class);
        PageBean pageBean = getPageBean(requestParams);
        // 排序
        Sort sort = createSort(bo.getOrderBy());
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


    public static final String COLUMS_ALLOW = "<#list table.columns as column>${column.camelCase!''}<#if column_has_next>,</#if></#list>";

    private Sort createSort(String orderBy) {
        if(orderBy == null || "".equals(orderBy)){
            return Sort.unsorted();
        }
        String[] arr = orderBy.split(" ");
        String column = arr[0];
        String direction = arr[1];
        if(!COLUMS_ALLOW.contains(column)){
            throw new BusinessException("非法的排序字段");
        }
        return Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), column);
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