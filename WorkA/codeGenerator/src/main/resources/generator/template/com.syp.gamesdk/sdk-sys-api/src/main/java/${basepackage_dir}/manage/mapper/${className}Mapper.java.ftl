<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.manage.mapper;

import java.util.List;
import ${basepackage}.manage.pojo.${className};
import ${basepackage}.manage.query.${className}Query;

/**
 <#include "/java_description.include">
 */
public interface ${className}Mapper {

	/**
	 * 插入新的记录
	 */
	int insert(${className} ${classNameFirstLower});
	/**
	 * 更新记录
	 */
	int update(${className} ${classNameFirstLower});
	/**
	 * 删除记录
	 */
	int delete(${table.idColumn.javaType} id);
	/**
	* 删除记录
	*/
	int deleteWithTag(${table.idColumn.javaType} id);
	/**
	 * 根据ID查询记录
	 */
	${className} getById(${table.idColumn.javaType} id);
	/**
	 * 查询分页总数
	 */
	int findPageCount(${className}Query ${classNameFirstLower}Query);
	/**
	 * 查询分页数据
	 */
	List<${className}> findPage(${className}Query ${classNameFirstLower}Query);

}