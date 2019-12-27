<#include "/custom.include">
<#include "/java_copyright.include">
<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
/*
 * Powered By fjszyw
 * Web Site: http://www.fjszyw.com
 * Since 2008 - 2019
 */
package com.fjszyw.gamesdk.notice.dto;

import com.bst.gamesdk.model.entity.${className};
import io.swagger.annotations.ApiModel;
import java.io.Serializable;

/**
 *
 * @author cedoo email:cedoo(a)qq.com
 * @version 1.0
 * @since 1.0
 */
@ApiModel(description = "<@tbAlias />", value = "<@tbAlias />")
public class ${className}Vo implements Serializable{

    public ${className}Vo(${className} ${classNameFirstLower}){

    }

}

