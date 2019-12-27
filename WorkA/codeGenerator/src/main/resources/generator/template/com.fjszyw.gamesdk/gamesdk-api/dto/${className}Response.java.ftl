<#include "/custom.include">
<#include "/java_copyright.include">
<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package com.fjszyw.gamesdk.${classNameLowerCase}.dto;

import com.bst.gamesdk.model.entity.${className};
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author bst
 */
@ApiModel(description = "游戏公告响应对象", value = "游戏公告响应对象")
public class ${className}Response implements Serializable {

    @ApiModelProperty(name="msg", value="消息说明", example = "操作成功")
    private String msg;
    @ApiModelProperty(name="success", value="成功标志,0:失败，1:成功", example = "1", allowableValues = "0,1")
    private String success;

    @ApiModelProperty(name="${classNameFirstLower}", value="<@tbAlias />列表")
    private List<${className}Vo> ${classNameFirstLower};

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<${className}Vo> get${className}() {
        return ${classNameFirstLower};
    }

    public void set${className} (List<${className}Vo> ${classNameFirstLower}) {
        this.${classNameFirstLower} = ${classNameFirstLower};
    }
}
