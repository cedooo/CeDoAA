<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
package com.bst.gamesdk.service;

import com.bst.gamesdk.model.entity.${className};
import com.bst.gamesdk.service.base.IService;

/**
 <#include "/java_description.include">
 */
public interface I${className}Service extends IService<${className}> {

}