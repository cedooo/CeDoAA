<#include "/custom.include">
<#include "/java_copyright.include">
<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package com.fjszyw.gamesdk.notice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/**
 * 获取<@tbAlias />参数表单
 * @author cedoo email:cedoo(a)qq.com
 * @version 1.0
 * @since 1.0
 */
@ApiModel(description = "获取<@tbAlias />参数表单", value = "获取<@tbAlias />参数表单")
public class Get${className}Form implements Serializable {

    @ApiModelProperty(name="gameCode", value="游戏应用代码", example = "jYREnW4GI0", required = true)
    @NotBlank(message = "游戏编码为空")
    private String gameCode;

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }
}
