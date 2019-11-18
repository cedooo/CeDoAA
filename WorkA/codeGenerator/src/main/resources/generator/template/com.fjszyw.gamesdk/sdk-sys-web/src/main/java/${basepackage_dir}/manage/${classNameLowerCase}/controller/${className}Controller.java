<#include "/custom.include">
<#include "/java_copyright.include">
<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.manage.${classNameLowerCase}.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bst.sdk.common.util.CodeUtil;
import com.bst.sdk.common.util.ReturnUtil;
import com.bst.sdk.util.BindingResultToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.bst.sdk.common.json.JsonlibUtils;
import com.bst.sdk.common.util.Page;
import com.bst.sdk.sys.controller.BaseController;

import ${basepackage}.manage.${classNameLowerCase}.service.${className}Service;
import ${basepackage}.manage.pojo.${className};
import ${basepackage}.manage.query.${className}Query;

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
		<#list table.columns as column>
		<#if column.isDateTimeColumn && !column.contains(timeRangeTag)>
		binder.registerCustomEditor(Date.class, "${column.columnNameLower}Begin", new CustomDateEditor(dateFormat2, true));
		binder.registerCustomEditor(Date.class, "${column.columnNameLower}End", new CustomDateEditor(dateFormat2, true));
		</#if>
		</#list>
	}


	@RequestMapping({ "/list" })
	/**
	 * 列表
	 */
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return "list";
	}
	/**
	 * 编辑界面
	 */
	@RequestMapping({ "/edit" })
	public String edit(HttpServletRequest request,
		HttpServletResponse response, Model model, @RequestParam(required = false) Long id) {
		${className} msg = new  ${className}();
		if (id != null&&id!= 0) {
			msg = ${classNameFirstLower}Service.getById(id);
		}
		Assert.notNull(msg);
		//announcementService.updateStatus(msg, true);
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
			Long id = ${classNameFirstLower}.getId();
			${className} ${classNameFirstLower}Edited = null;
			if(id==null){
				${classNameFirstLower}Edited = ${classNameFirstLower}Service.save(${classNameFirstLower});
			}else{
				${classNameFirstLower}Edited = ${classNameFirstLower}Service.update(${classNameFirstLower});
			}
			if(${classNameFirstLower}Edited!=null){
				Long newId = ${classNameFirstLower}Edited.getId();
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
        HttpServletResponse response, @ModelAttribute ${className}Query ${classNameFirstLower}Query) {
        Page page = ${classNameFirstLower}Service.findPage(${classNameFirstLower}Query);
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
		HttpServletResponse response, Model model, @RequestParam(value = "arr[]") Long[] arr) {
		try {
			${classNameFirstLower}Service.delBatch(arr);
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1";
	}
}
