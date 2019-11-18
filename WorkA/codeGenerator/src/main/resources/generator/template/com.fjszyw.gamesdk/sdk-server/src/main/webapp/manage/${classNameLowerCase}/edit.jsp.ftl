<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="${basepackage}.manage.vo.${className}Vo" %>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<#macro jspEL value>${r"${"}${value}}</#macro>
<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath %>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<title>新增编辑</title>
	<jsp:include page="/common/header.jsp"></jsp:include>
	<jsp:include page="/common/tail.jsp"></jsp:include>
	<script type="text/javascript">
	$(function() {
		<#list table.columns as column>
		<#if column.isDateTimeColumn>
		//初始化时间
		var ${column.columnNameLower} = {
			elem: '#${column.columnNameLower}', //id为star的输入框
			format: 'YYYY-MM-DD hh:mm:ss',
			min: laydate.now(), //最大日期
			istime: true,
			istoday: true,
			choose: function(datas){
			}
		};
		laydate(${column.columnNameLower});
		<#elseif column.columnNameLower?ends_with("Tag")||column.columnNameLower?ends_with("Type")||column.columnNameLower?ends_with("stats")>
		var jsonEnumArray = <%=${className}Vo.${column.constantName}_JSON_MAP %>;
		if(jsonEnumArray&&jsonEnumArray.length>0){
			$("#searchForm select[name='${column.columnNameLower}']").append(
					"<option value=''>全部</option>");
			for (var i = 0; i < jsonEnumArray.length; i++) {
				$("#${className}Form select[name='${column.columnNameLower}']").append(
						"<option value='" + jsonEnumArray[i].key +  "'>" + jsonEnumArray[i].value +  "</option>")
			}
		}
		</#if>
		</#list>

		$("#updateTime, #delTag, #createTime, #id").parents(".form-group").hide();
	});
	//保存验证
	function valid() 
	{
		return $("#${className}Form").valid();
	}
	
	function view() {
		$('form').find('input,textarea,select').attr('disabled',true);
	}
	</script>
  </head>
  <body>
    <form action="" class="form-horizontal" id="${className}Form">
		<#--<input id="id" name="id" value="<@jspEL classNameFirstLower+".id"/>" type="hidden" class="form-control" >-->
		<div class="box-body">
			<br>
			<#list table.columns as column>
			<div class="form-group">
				<label class="col-sm-2 control-label"><%=${className}Vo.ALIAS_${column.constantName} %>：</label>
				<div class="col-md-3 col-sm-3 no-padding">
					<#if column.isDateTimeColumn>
						<input type="text" class="form-control layer-date" id="${column.columnNameLower}"
							value="<fmt:formatDate value="<@jspEL "obj." + column.columnNameLower></@jspEL>" pattern="yyyy-MM-dd HH:mm:ss"/>"
							readonly    name="${column.columnNameLower}">
					<#elseif column.columnNameLower?ends_with("Tag")||column.columnNameLower?ends_with("Type")||column.columnNameLower?ends_with("stats")>
						<select class="form-control chosen-select" id="${column.columnNameLower}" name="${column.columnNameLower}" style="width: 150px;"></select>
					<#else>
						<input type="text" class="form-control" id="${column.columnNameLower}" name="${column.columnNameLower}"  value="<@jspEL "obj."+column.columnNameLower></@jspEL>" />
					</#if>
				</div>
			</div>
			</#list>
		</div>
	</form>
  </body>
</html>
