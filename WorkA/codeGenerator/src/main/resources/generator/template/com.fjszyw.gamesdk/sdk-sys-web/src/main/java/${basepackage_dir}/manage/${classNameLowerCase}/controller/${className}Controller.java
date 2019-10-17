<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.${classNameLowerCase}.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bst.sdk.common.json.JsonlibUtils;
import com.bst.sdk.common.redis.StringHelper;
import com.bst.sdk.common.util.DateUtil;
import com.bst.sdk.common.util.Page;
import com.bst.sdk.common.util.ReturnMsg;
import com.bst.sdk.common.util.ReturnUtil;
import com.bst.sdk.common.util.jxl.ExportExecl;
import com.bst.sdk.common.util.jxl.JxlExportHandle;
import com.bst.sdk.sys.controller.BaseController;

import ${basepackage}.manage.${classNameLowerCase}.service.${className}Service;
import ${basepackage}.manage.pojo.${className};

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@RequestMapping("manage/${classNameLowerCase}")
@Controller
public class ${className}Controller extends BaseController {
	
	@Autowired
	private ${className}Service ${classNameFirstLower}Service;
	@RequestMapping({ "/list" })
	/**
	 * 公告信息
	 * @param request
	 * @param response
	 * @return
	 */
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return "list";
	}

	@RequestMapping({ "/edit" })
	public String edit(HttpServletRequest request,
		HttpServletResponse response, Model model, @RequestParam(required = false) Long id) {
		${className} msg = new  ${className}();
		if (id != null&&id!= 0) {
		msg = ${classNameFirstLower}Service.getById(id);
		}
		Assert.notNull(msg);
//		announcementService.updateStatus(msg, true);
		model.addAttribute("message", msg);
		return "edit";
	}
}
