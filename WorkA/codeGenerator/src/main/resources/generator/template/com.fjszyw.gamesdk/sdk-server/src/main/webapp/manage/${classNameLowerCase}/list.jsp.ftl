<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="${basepackage}.manage.vo.${className}Vo" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <title><%=${className}Vo.TABLE_ALIAS%>管理</title>
	    <jsp:include page="/common/header.jsp"></jsp:include>
	    <jsp:include page="/common/tail.jsp"></jsp:include>
	    
	    <base target="<%=basePath%>">
	    
	    <script type="text/javascript">
		    var rowId="";
			var rowData = "";
			var id="";

	    	$(function() {
	    		
	    		$("#queryBtn").click(function() {
	    			page=1;
	    			reload();
	    		});
	    		
	    		$("#table_list").jqGrid({
	    			url: getRoot() + "manage/${classNameLowerCase}/queryList.zul",
	                datatype: "json",
	                mtype: "post",
	                height: "400px",
	                autowidth: true,
	                shrinkToFit: true,
	                multiselect: true,
	                rowNum: "20",
	                colNames: [ <#list table.columns as column>"<%=${className}Vo.ALIAS_${column.constantName} %>"<#if column_has_next>,</#if></#list> ],
	                colModel: [
							<#list table.columns as column>
							{
								name: "<#if column.isDateTimeColumn>${column.columnNameLower}<#else>${column.columnNameLower}</#if>",index: "${column.columnNameLower}",
								width: 10,sorttype: "int",align:"center",sortable:false
								<#if column.columnNameLower?ends_with("id")||column.columnNameLower?ends_with("createTime")||column.columnNameLower?ends_with("updateTime")|| column.columnNameLower?ends_with("delTag") >
								,hidden: true
								</#if>
                                <#if column.columnNameLower?ends_with("Tag")||column.columnNameLower?ends_with("Type")||column.columnNameLower?ends_with("stats") >
                                ,formatter:function(cellvalue, options, rowObject){
									var jsonEnumArray = <%=${className}Vo.${column.constantName}_JSON_MAP %>;
									if(jsonEnumArray&&jsonEnumArray.length>0){
										for (var i = 0; i < jsonEnumArray.length; i++) {
											if(cellvalue == jsonEnumArray[i].key){
												return jsonEnumArray[i].value;
											}
										}
										return "";
									}else{
										return "";
									}
								}
                                </#if>
							}<#if column_has_next>,</#if></#list>
	                ],
	                pager: "#pager_list",
	                viewrecords: true,
	                //caption: "<%=${className}Vo.TABLE_ALIAS%>",
	                //hidegrid: true,
	           	});
	    		
                 $(window).bind("resize", function () {
                     var width = $(".jqGrid_wrapper").width();
                     $("#table_list").setGridWidth(width);
                 });
			
     	    	$("#table-toolbar").mybuttons({
    				buttons: [{
    					text: "新增",
    					iconCls: "btn btn-primary fa fa-plus",
    					handler: function() {
    						var index = parent.layer.open({
    						    type: 2,
    						    title: "新增<%=${className}Vo.TABLE_ALIAS %>",
    						    shadeClose: true,
    						    shade: 0.8,
    						    time: 0,
    						    area: ["60%", "70%"],
    						    content: getRoot() + "manage/${classNameLowerCase}/edit.zul", //iframe的url,
    						    btn: ["保存","取消"],
    						    yes: function(index, layero){
    						    	var framefrom = parent.window.frames["layui-layer-iframe" + index];
    						    	//var data = framefrom.$("#channelForm").serialize();
    						    	if (framefrom.valid())
    						    	{
    						    		var loadIndex = parent.layer.msg('保存中', {
    						                icon : 16,
    						                time : 1000,
    						                shade : 0.1
    						            });
    						    		$.ajax({
    						    			url: getRoot() + "manage/${classNameLowerCase}/doedit.zul",
    						    			data: framefrom.$("#${className}Form").serialize(),
    						    			type: "POST",
    						    			success: function(data) {
    						    				parent.layer.close(loadIndex);
    						    				var json = data;
    						    				var id=json.id;
    						    				if(json.success == true) {
    						    					framefrom.$("#id").val(id);
    						    						parent.layer.alert("保存成功",{
    						     							time:2000,
    						     							end:function(){
    						     								 reload();
    						     								parent.layer.close(index);
    						     							}
    						     						});
    						    				} else {
    						    					parent.layer.msg(json.msg);
    						    				}
    						    			},
    						    			error: function() {
    						    				parent.layer.close(loadIndex);
    						    				parent.layer.msg("系统异常，请联系管理员！");
    						    			}
    						    		});
    						    	}
    						    },btn2: function(index, layero) {
    						    	
    						    },
		    					cancle : function() {
		    						//		reload();
		    					}
    						});
    					}
    				},
    				{
    					text: "编辑",
    					iconCls: "btn btn-primary fa fa-edit",
    					handler: function() {
    						rowId = $("#table_list").jqGrid("getGridParam","selrow");
    						if(rowId != null){
	    						 rowData = $("#table_list").jqGrid("getRowData",rowId);
	    						 id=rowData.ID;
    						}else{
    							$.msg.alert("提示", "请选择要编辑的<%=${className}Vo.TABLE_ALIAS %>");
    							return;
    						}
    						var rowIdsArr = jQuery("#table_list").jqGrid('getGridParam', 'selarrrow'); 
    						if( rowIdsArr.length>1){
    							 $.msg.alert("提示", "只能选择单个进行编辑");
    								return;
    						}
    						var id = rowId;
    						var index = parent.layer.open({
    						    type: 2,
    						    title: "编辑<%=${className}Vo.TABLE_ALIAS %>",
    						    shadeClose: true,
    						    shade: 0.8,
    						    time: 0,
    						    area: ["60%", "70%"],
    						    content: getRoot() + "manage/${classNameLowerCase}/edit.zul?id="+id, //iframe的url,
    						    btn: ["保存","取消"],
    						    yes: function(index, curDocument){
    						    	var framefrom = parent.window.frames["layui-layer-iframe" + index];
    						    	//var data = framefrom.$("#gameAnnouncementForm").serialize();
    						    	if (framefrom.valid())
    						    	{
    						    		var formJson = framefrom.$("#${className}Form").serialize();
										$.ajax({
    						    			url: getRoot() + "manage/${classNameLowerCase}/doedit.zul",
    						    			data: formJson,
    						    			type: "POST",
    						    			success: function(data) {
    						    			//	var json = eval("("+data+")");
    						    				var json = data;
    						    				if(json.success == true) {
    						    					 reload();
    						    						parent.layer.alert("保存成功",{
    						     							time:2000,
    						     							end:function(){
    						     								parent.layer.close(index); //执行关闭 
    						     							}
    						     						});
    						    				} else {
    						    					parent.layer.msg(json.msg);
    						    				}
    						    			},
    						    			error: function() {
    						    				parent.layer.msg("系统异常，请联系管理员！");
    						    				//$.msg.alert("温馨提示", "系统异常，请联系管理员！");
    						    			}
    						    		});
    						    	}
    						    },btn2: function(index, layero) {
    						    	//alert("btn2");
    						    },
    						    cancel: function() {
    						    	reload();
    						    	parent.layer.close(index); 
    						    }
    						});
    					}
    				},
    				{
    					text: "删除",
    					iconCls: "btn  btn-danger fa fa-times",
    					handler: function() {
    						var rowIdsArr = jQuery("#table_list").jqGrid('getGridParam', 'selarrrow'); 
    						if(rowIdsArr == null || rowIdsArr.length == 0){
	    						 $.msg.alert("提示", "请选择要删除的<%=${className}Vo.TABLE_ALIAS%>");
	    							return;
    						}
    						  $.msg.confirm("删除<%=${className}Vo.TABLE_ALIAS%>提示", "您确定删除吗?", function() {
		    						$.ajax({
		            					url: getRoot() + "manage/${classNameLowerCase}/del.zul",
		            					data:{"arr[]":rowIdsArr},
		            					type: "POST",
		            					success: function(data) {
		            						if("0" == data) {
		            							$.msg.alert("提示", "删除成功");
		            							//删除成功后刷新页面
		            							reload();
		            						} else {
		            							$.msg.alert("提示", "删除失败");
		            						}
		            					},
		            					error: function() {
		            						$.msg.alert("温馨提示", "系统异常，请联系管理员！");
		            					}
		            				});
    						  });
    					}
    				}
    				],
    			});

				//初始化下拉框，日期选择框
				<#list table.columns as column>
					<#if column.isDateTimeColumn>
				 		//初始化时间
                var ${column.columnNameLower}start = {
                    elem: '#${column.columnNameLower}Begin', //id为star的输入框
                    format: 'YYYY-MM-DD',
                    //max: laydate.now(), //最大日期
                    istime: false,
                    istoday: false,
                    choose: function(datas){
                        ${column.columnNameLower}end.min = datas;//开始日选好后，重置结束日的最小日期
                    }
                };
                var ${column.columnNameLower}end = {
                    elem: '#${column.columnNameLower}End', //id为star的输入框
                    format: 'YYYY-MM-DD',
                    //max: laydate.now(), //最大日期
                    istime: false,
                    istoday: false,
                    choose: function(datas){
                        ${column.columnNameLower}start.max = datas;//开始日选好后，重置结束日的最小日期
                    }
                };
                laydate(${column.columnNameLower}start);
                laydate(${column.columnNameLower}end);
				    <#elseif column.columnNameLower?ends_with("Tag")||column.columnNameLower?ends_with("Type")||column.columnNameLower?ends_with("stats")>
				var jsonEnumArray = <%=${className}Vo.${column.constantName}_JSON_MAP %>;
				if(jsonEnumArray&&jsonEnumArray.length>0){
                    $("#searchForm select[name='${column.columnNameLower}']").append(
                        "<option value=''>全部</option>");
					for (var i = 0; i < jsonEnumArray.length; i++) {
						$("#searchForm select[name='${column.columnNameLower}']").append(
								"<option value='" + jsonEnumArray[i].key +  "'>" + jsonEnumArray[i].value +  "</option>")
					}
				}
					</#if>
				</#list>

				//隐藏通用参数
				$("#updateTimeBegin, #delTag, #createTimeBegin, #id").parent().hide();
	    	});
	    	page = 1;
	    	//刷新页面
	    	function reload(){

				/**
				 * 自定义序列化工具函数
				 $.fn.serializeObjectWithEmpty = function() {
					var o = {};
					var a = this.serializeArray();
					$.each(a, function () {
						if(this.value){
							if (o[this.name]) {
								if (!o[this.name].push) {
									o[this.name] = [o[this.name]];
								}
								o[this.name].push(this.value || '');
							} else {
								o[this.name] = this.value || '';
							}
						}else{
							o[this.name] = '';
						}
					});
					return o;
				 }
				 */
				var postDatas = $("#searchForm").serializeObjectWithEmpty();
				$("#table_list").jqGrid("setGridParam",{
					postData:postDatas,
					page:page
				}).trigger("reloadGrid");
	    	}
	    </script>
	</head>
	
	<body class="gray-bg">
	    <div class="wrapper wrapper-content  animated fadeInRight">
	        <div class="row">
                <div class="ibox">
                    <div class="ibox-title">
                        <h5 id="listTitle"><%=${className}Vo.TABLE_ALIAS %></h5>
                    </div>
					<div class="ibox-content">
						<form class="form-inline" id="searchForm">
							<#list table.columns as column>
							<div class="form-group" style="margin-bottom:10px;">
					    		<label><%=${className}Vo.ALIAS_${column.constantName} %>:</label>
								<#if column.isDateTimeColumn>
									<input type="text" class="form-control layer-date" id="${column.columnNameLower}Begin" name="${column.columnNameLower}Begin"> -
									<input type="text" class="form-control layer-date" id="${column.columnNameLower}End" name="${column.columnNameLower}End">
								<#elseif column.columnNameLower?ends_with("Tag")||column.columnNameLower?ends_with("Type")||column.columnNameLower?ends_with("stats")>
									<select class="form-control chosen-select" id="${column.columnNameLower}" name="${column.columnNameLower}" style="width: 150px;"></select>
								<#else>
                                     <input type="text" maxlength="${column.size}" class="form-control" id="${column.columnNameLower}" name="${column.columnNameLower}" />
								</#if>
					  		</div>
							</#list>
					  		<div class="form-group">
		                 		<button type="button" class="btn btn-sm btn-primary" id="queryBtn">搜索</button>
		                 		<button type="reset" class="btn btn-sm btn-primary" id="clearBtn">重置</button>
		                 	</div>
						</form>
						<%--列表 --%>
						<div id="table-toolbar" class="toolbar top-bottom-padding"></div>
	                     <div class="jqGrid_wrapper">
	                         <table id="table_list"></table>
	                         <div id="pager_list"></div>
	                     </div>
					</div>
                </div>
	        </div>
	    </div>
	</body>
</html>