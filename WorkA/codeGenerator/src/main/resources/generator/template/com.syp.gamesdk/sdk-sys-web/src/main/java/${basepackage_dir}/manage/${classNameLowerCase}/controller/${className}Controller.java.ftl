<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.manage.${classNameLowerCase}.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bst.sdk.common.util.*;
import com.bst.sdk.manage.query.${className}Query;
import com.bst.sdk.util.BindingResultToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.bst.sdk.common.json.JsonlibUtils;
import com.bst.sdk.sys.controller.BaseController;
import org.springframework.util.Assert;

import ${basepackage}.manage.${classNameLowerCase}.service.${className}Service;
import ${basepackage}.manage.pojo.${className};

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@RequestMapping("manage/${classNameLowerCase}")
@Controller
public class ${className}Controller extends BaseController {
	
	@Autowired
	private ${className}Service ${classNameFirstLower}Service;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        DateFormat dateFormat2= new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Date.class, "createTimeBegin", new CustomDateEditor(dateFormat2, true));
        binder.registerCustomEditor(Date.class, "createTimeEnd", new CustomDateEditor(dateFormat2, true));
        binder.registerCustomEditor(Date.class, "updateTimeBegin", new CustomDateEditor(dateFormat2, true));
        binder.registerCustomEditor(Date.class, "updateTimeEnd", new CustomDateEditor(dateFormat2, true));
        binder.registerCustomEditor(Date.class, "getTimeBegin", new CustomDateEditor(dateFormat2, true));
        binder.registerCustomEditor(Date.class, "getTimeEnd", new CustomDateEditor(dateFormat2, true));
    }
    /**
     * 列表
     */
    @RequestMapping({ "/list" })
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        ${className}Query query = new ${className}Query();
        //query.setDelTag("0");
        query.setPage(1);
        query.setPagecount(1000);
        Page pages = ${classNameFirstLower}Service.findPage(query);
        model.addAttribute("${classNameFirstLower}", pages.getRows());
        return "list";
    }
    /**
     * 编辑界面
     */
    @RequestMapping({ "/edit" })
    public String edit(HttpServletRequest request,
        HttpServletResponse response, Model model,
        @RequestParam(required = false) ${table.idColumn.javaType} ${table.idColumn.columnNameLower}) {
        ${className} msg = new  ${className}();
        <#list table.compositeIdColumns as column>
        <#if 'java.lang.Long'==column.javaType>
        if (${table.idColumn.columnNameLower} != null&&${table.idColumn.columnNameLower}!= 0) {
            msg = ${classNameFirstLower}Service.getById(${table.idColumn.columnNameLower});
        }
        <#elseif 'java.lang.String'==column.javaType>
        if (${table.idColumn.columnNameLower} != null) {
            msg = ${classNameFirstLower}Service.getById(${table.idColumn.columnNameLower});
        }
        </#if>
        </#list>
        Assert.notNull(msg);
        model.addAttribute("obj", msg);
        return "edit";
    }
    /**
     * 编辑
     */
    @RequestMapping("/doedit")
    @ResponseBody
    public JSON doedit(HttpServletRequest request,HttpServletResponse response,
    @ModelAttribute @Valid ${className} ${classNameFirstLower}, BindingResult bindingResult){
        Map<String, Object> msgMap = new HashMap<String, Object>(10);
        String msg = "保存成功";
        try {
            if (bindingResult.hasErrors()) {
                String errorInfo = BindingResultToString.errorInfo(bindingResult);
                CodeUtil.throwExcep(errorInfo);
            }
            ${table.idColumn.javaType} ${table.idColumn.columnNameLower} = ${classNameFirstLower}.get${table.idColumn.columnName}();
            ${className} ${classNameFirstLower}Edited = null;
            if(${table.idColumn.columnNameLower}==null){
                ${classNameFirstLower}Edited = ${classNameFirstLower}Service.save(${classNameFirstLower});
            }else{
                ${classNameFirstLower}Edited = ${classNameFirstLower}Service.update(${classNameFirstLower});
            }
            if(${classNameFirstLower}Edited!=null){
                ${table.idColumn.javaType}  newId = ${classNameFirstLower}Edited.get${table.idColumn.columnName}();
                msgMap.put("id", newId);
                msgMap.put("msg", msg);
                return ReturnUtil.getReturnJSON(true, msgMap);
            }else{
                return ReturnUtil.getReturnJSON(false, msgMap);
            }
        } catch (Exception ex) {
            msg = "保存失败";
            msgMap.put("msg", msg);
            return ReturnUtil.getReturnJSON(false, msgMap);
        }
    }
    /**
     * 分页查询
     */
    @RequestMapping({ "/queryList" })
    @ResponseBody
    public void queryPage(HttpServletRequest request,
        HttpServletResponse response, @ModelAttribute ${className}Query ${classNameLowerCase}Query) {
        Page page = ${classNameFirstLower}Service.findPage(${classNameLowerCase}Query);
        JSONObject json = JSONObject.fromObject(page, JsonlibUtils.getJsonConfigNoCycle());
        System.out.println(json.toString());
        this.output(response, json.toString());
    }
    /**
     * 删除
     */
    @RequestMapping({ "/del" })
    @ResponseBody
    public String del(HttpServletRequest request,
        HttpServletResponse response, Model model,
        @RequestParam(value = "arr[]") ${table.idColumn.javaType}[] ${table.idColumn.columnNameLower}) {
        try {
            ${classNameFirstLower}Service.delBatch(${table.idColumn.columnNameLower});
		    return "0";
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return "1";
	}
}
