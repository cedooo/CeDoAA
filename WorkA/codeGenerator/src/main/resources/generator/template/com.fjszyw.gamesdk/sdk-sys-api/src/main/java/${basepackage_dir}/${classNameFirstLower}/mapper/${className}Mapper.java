<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.${classNameFirstLower}.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 <#include "/java_description.include">
 */
public interface ${className}Mapper {

	public

}