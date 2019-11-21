<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLowerFirst = className?uncap_first>
package com.bst.gamesdk.service.impl;

import com.bst.gamesdk.dao.${className}Mapper;
import com.bst.gamesdk.dao.base.BaseMapper;
import com.bst.gamesdk.service.I${className}Service;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.stereotype.Component;
import com.bst.gamesdk.model.entity.${className};
import com.bst.gamesdk.common.annotation.CacheConfig;

import com.bst.gamesdk.service.base.ServiceImpl;

import javax.annotation.Resource;

/**
 <#include "/java_description.include">
 */
//@CacheConfig(nameSpace = CACHENAMESPACE_CHANNEL+CACHENAMESPACE)
@Component
public class I${className}ServiceImpl extends ServiceImpl<${className}> implements I${className}Service {

    @Resource
    private ${className}Mapper ${classNameLowerFirst}Mapper;


    @Override
    protected BaseMapper<${className}> getMapper() {
            return ${classNameLowerFirst}Mapper;
    }

    @Override
    protected TypeReference<?> getCacheTypeReference(int type) {
        return null;
    }

}