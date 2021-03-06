<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLowerFirst = className?uncap_first>
<#assign classNameLower = className?lower_case>
package ${basepackage}.manage.${classNameLower}.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import com.bst.sdk.common.util.Page;
import ${basepackage}.manage.query.${className}Query;
import ${basepackage}.manage.${classNameLower}.service.${className}Service;
<#include "/java_imports.include">

import java.util.List;
import java.util.Date;
/**
<#include "/java_description.include">
 */
@Service
@Transactional
public class ${className}ServiceImpl implements ${className}Service {


	@Autowired
	private ${className}Mapper ${classNameLowerFirst}Mapper;

	//private ${className}Dao ${classNameLowerFirst}Dao;
	/**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,请注意大小写*/
	public void set${className}Mapper(${className}Mapper mapper) {
		this.${classNameLowerFirst}Mapper = mapper;
	}
	
	/** 
	 * 创建${className}
	 **/
	public ${className} save(${className} ${classNameLowerFirst}) {
	    Assert.notNull(${classNameLowerFirst},"'${classNameLowerFirst}' must be not null");
	    initDefaultValuesForCreate(${classNameLowerFirst});
	    new ${className}Checker().checkCreate${className}(${classNameLowerFirst});
	    this.${classNameLowerFirst}Mapper.insert(${classNameLowerFirst});
	    return ${classNameLowerFirst};
	}
	
	/** 
	 * 更新${className}
	 **/	
    public ${className} update(${className} ${classNameLowerFirst}) {
        Assert.notNull(${classNameLowerFirst},"'${classNameLowerFirst}' must be not null");
		initDefaultValuesForUpdate(${classNameLowerFirst});
        new ${className}Checker().checkUpdate${className}(${classNameLowerFirst});
        this.${classNameLowerFirst}Mapper.update(${classNameLowerFirst});
        return ${classNameLowerFirst};
    }	
    
	/** 
	 * 删除${className}
	 **/
    public void removeByKey(${table.idColumn.javaType} key) {
        this.${classNameLowerFirst}Mapper.delete(key);
    }
    
	/** 
	 * 根据ID得到${className}
	 **/    
    public ${className} getByKey(${table.idColumn.javaType} key) {
        return this.${classNameLowerFirst}Mapper.getByKey(key);
    }
    
	/** 
	 * 分页查询: ${className}
	 **/      
	@Transactional(readOnly=true)
	public Page findPage(${className}Query query) {
		PageHelper.startPage(query.getPage(), query.getPagecount());
		Assert.notNull(query,"'query' must be not null");
		List<${className}> list =  ${classNameLowerFirst}Mapper.findPage(query);
		int total = ${classNameLowerFirst}Mapper.findPageCount(query);
		Page page = new Page();
		page.setRows(list);
		page.setRecords(total);
		return page;
	}


	@Override
	@Transactional()
	public void delBatch(${table.idColumn.javaType}[] keys) {
		for(${table.idColumn.javaType} key : keys) {
			${classNameLowerFirst}Mapper.deleteWithTag(key);
		}
	}

<#if 'java.lang.Long'==table.idColumn.javaType>
<#elseif 'java.lang.String'==table.idColumn.javaType>
    @Override
    public ${className} insertOrUpdate(${className} ${classNameLowerFirst}) {
        Assert.notNull(${classNameLowerFirst},"'${classNameLowerFirst}' must be not null");
        initDefaultValuesForCreate(${classNameLowerFirst});
        new ${className}Checker().checkCreate${className}(${classNameLowerFirst});
        this.${classNameLowerFirst}Mapper.insertOrUpdate(${classNameLowerFirst});
        return ${classNameLowerFirst};
	}
</#if>

<#list table.columns as column>
	<#if column.unique && !column.pk>
	@Transactional(readOnly=true)
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return ${classNameLowerFirst}Mapper.getBy${column.columnName}(v);
	}
	</#if>
</#list>
    
	/**
	 * 为创建时初始化相关默认值 
	 **/
    public void initDefaultValuesForCreate(${className} v) {
    	Date now = new Date();
		v.setCreatedTime(now);
		v.setUpdatedTime(now);
		v.setDelTag("0");
    }
	/**
	 * 为创建时初始化相关默认值
	 **/
	public void initDefaultValuesForUpdate(${className} v) {
		Date now = new Date();
		v.setUpdatedTime(now);
	}

    /**
     * ${className}的属性检查类,根据自己需要编写自定义检查
     **/
    public class ${className}Checker {
        /**可以在此检查只有更新才需要的特殊检查 */
        public void checkUpdate${className}(${className} v) {
            check${className}(v);
        }
    
        /**可以在此检查只有创建才需要的特殊检查 */
        public void checkCreate${className}(${className} v) {
            check${className}(v);
        }
        
        /** 检查到有错误请直接抛异常，不要使用 return errorCode的方式 */
        public void check${className}(${className} v) {
        	// Bean Validator检查,属性检查失败将抛异常
        	//TODO validateWithException(v);

        	//复杂的属性的检查一般需要分开写几个方法，如 checkProperty1(v),checkProperty2(v)
        }
    }
}
