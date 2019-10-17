<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/c" %>
<%@ taglib prefix="fmt" uri="/fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath %>">
    
    <title>新增编辑</title>
    
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
   	</form>
  </body>
</html>
