<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${sdkbasepackage}.dao;

import java.util.List;
import ${sdkbasepackage}.model.entity.${className};
import com.bst.gamesdk.dao.base.BaseMapper;

/**
 <#include "/java_description.include">
 */
public interface ${className}Mapper extends BaseMapper<${className}>{

	/**
	 * 更新记录
	 */
	int update(${className} ${classNameFirstLower});
	/**
	 * 删除记录
	 */
	int delete(long id);
	/**
	 * 根据ID查询记录
	 */
	${className} getById(long id);

}