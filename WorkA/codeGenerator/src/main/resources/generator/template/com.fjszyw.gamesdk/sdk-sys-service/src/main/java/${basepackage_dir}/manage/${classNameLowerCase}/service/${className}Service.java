<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
package ${basepackage}.manage.${classNameLowerCase}.service;

import com.bst.sdk.common.util.Page;
import ${basepackage}.manage.query.${className}Query;

<#include "/java_imports.include">

/**
<#include "/java_description.include">
 */
public interface ${className}Service {

	/** 
	 * 创建${className}
	 **/
	public ${className} save(${className} ${classNameLower});
	
	/** 
	 * 更新${className}
	 **/	
    public ${className} update(${className} ${classNameLower});
    
	/** 
	 * 删除${className}
	 **/
    public void removeById(${table.idColumn.javaType} id);
    
	/** 
	 * 根据ID得到${className}
	 **/    
    public ${className} getById(${table.idColumn.javaType} id);
    
	/** 
	 * 分页查询: ${className}
	 **/      
	public Page findPage(${className}Query query);
	/**
	 * 批量删除
	 */
	public void delBatch(Long[] ids);


<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} param);
	
	</#if>
</#list>
    
}
