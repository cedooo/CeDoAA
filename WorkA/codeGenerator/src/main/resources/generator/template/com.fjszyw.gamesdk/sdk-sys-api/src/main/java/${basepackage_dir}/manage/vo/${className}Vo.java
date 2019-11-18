<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.manage.vo;

import ${basepackage}.manage.pojo.${className};
import com.bst.sdk.common.util.DateUtil;

<#include "/java_imports.include">

/**
<#include "/java_description.include">
 */
public class ${className}Vo extends ${className}  implements java.io.Serializable{
	private static final long serialVersionUID = 5454155825314635342L;

	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * alias "${table.tableAlias}";
	 */
	public static final String TABLE_ALIAS = <#list table.columns as column><#if column.columnNameLower == 'id'>"${column.columnAlias?substring(0,column.columnAlias?index_of('ID'))}"</#if></#list>;

	<#list table.columns as column>
	<#if column.columnAlias?matches('.+:.*')>
	public static final String ALIAS_${column.constantName} = "${column.columnAlias?substring(0,column.columnAlias?index_of(':'))}";
	<@generateStatusEnum column.constantName column.columnAlias/>
	<#else>
	public static final String ALIAS_${column.constantName} = "${column.columnAlias}";
	</#if>
	</#list>

	/**
	 * date formats
	 */
	<#list table.columns as column>
	<#if column.isDateTimeColumn>
	public static final String FORMAT_${column.constantName} = DATE_FORMAT;
	</#if>
	</#list>

<@generateJavaVoColumns/>

}

