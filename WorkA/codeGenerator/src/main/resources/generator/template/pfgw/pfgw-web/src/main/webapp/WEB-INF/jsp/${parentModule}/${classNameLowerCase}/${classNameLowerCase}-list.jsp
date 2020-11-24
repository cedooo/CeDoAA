<#include "/custom.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="${basepackage}.vo.${parentModule}.${classNameLowerCase}.${className}Vo" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=${className}Vo.TABLE_ALIAS %></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
</head>
<!-- 页面layout -->
<body class="easyui-layout" fit="true" style="overflow: hidden;">
	<div class="easyui-layout" fit="true" style="overflow:hidden">

		<div data-options="region:'north',split:true" class="easyui-panel searchArea" style="overFlow: hidden;">
		 	<form id="taskForm" method="post">
	 			<table class="showtable" >
	 				<tr>
	 					<td >
							<#list table.columns as column>
							<div>
	 							<p><%=${className}Vo.ALIAS_${column.constantName} %></p>
		 						<input type="text" id="${column.columnNameLower}" name="${column.columnNameLower}" class="easyui-combobox"  style="width:110px; editable="false"" />
		 					</div>
							</#list>
							<div style="margin-left:50px;">
								<a id="serach" href="#" class="easyui-linkbutton m-btn searchBtn" style="width:80px;margin-left:5px;"onclick="javascript:MplanSp.searchTaskByCondition()"></a>
								<a id="" href="#" class="easyui-linkbutton m-btn" onclick="javascript:MplanSp.removeTaskConditions()" style="width:80px;">重置</a>
							</div>
		 				</td>
		 			</tr>
	 			</table>
	 		</form>
		</div>

		<!-- 中间部分 列表 -->
		<div data-options="region:'center',split:true" style="overflow: hidden;">
			<!--表格-->
			<table id="taskList"  data-options="plain:true,iconCls:''" >
				<thead>
					 <tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<#list table.columns as column>
							 <th data-options="field:'${column.columnNameLower}',width:260,align:'left'" ><%=${className}Vo.ALIAS_${column.constantName} %></th>
						 </#list>
					</tr>
				</thead>
			</table>
  		</div>
		<div id="toolButton1" >
			<table width="100%">
				<tr>
					<td>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.openWinPlan('add')">新建</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.openWinPlan('edit')">修改</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.deleteTask()">删除</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.sendTaskWin('下发通知','1')">启用</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.stopTaskWin('6')">暂停</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.reCallTaskWin()">撤回</a>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton"  onclick="MplanSp.endTaskWin('7')">结束</a>
					</td>
				</tr>
			</table>
		</div>


  	</div>
<script type="text/javascript" src="${r'${ctx}'}/js/jsTools.js"></script>
<script type="text/javascript">
var ttype = "${r'${ttype}'}";//分类（<1重点、2三年、3年度>）
var api = frameElement ? frameElement.api:null;
var W = api ? api.opener : window;
//形成闭包
;(function(){
    //时间格式化(年月)
    MplanSp.formatTime = function(value,row,index){
        return formatDate(value,"yyyy-MM-dd");
    };

    //格式化通知状态
    MplanSp.formatStatus = function (value,row,index){
        if(value == "-1"){
            return "创建";
        }else if(value == "0"){
            return "新通知";
        }else if(value == "1"){
            return "已下发";
        }else if(value == "2"){
            return "已分发";
        }else if(value == "3"){
            return "已发布";
        }else if(value == "4"){
            return "已撤回";
        }else if(value == "5"){
        	return "已入库";
        }else if(value == "6"){
        	return "已暂停";
        }else if(value == "7"){
        	return "已结束";
        }
    };
    
    
    //格式化通知层级 --伍红林
    MplanSp.formatLevelStatus = function (value,row,index){
    	if(value == "1"){
    		return "县";
    	}else if(value == "2"){
    		return "市";
    	}else if(value == "3"){
    		return "省";
    	}
    };

    //格式化锁定状态
    MplanSp.formatLockStatus = function (value,row,index){
        if(value == "1"){
            return " <img  src='"+ctx+"/major/images/lock.png'>";
        }else if(value == "0"){
            return "-";
        }
    };
    
    //格式化单元格提示信息  
    MplanSp.formatDescTooltip = function (value,row,index){  
    	if(value == undefined||value == null){
    		return "<span title='" + "" + "'>" + "" + "</span>";
    	}else {
    		return "<span title='" + value + "'>" + value + "</span>";
		}
          
    }
    
    //查看下发/分发通知
        MplanSp.viewTask = function(pubid,taskpubType){
        	var title;
        	if(taskpubType == 1){
        		title = "查看下发记录";
        	}else if(taskpubType == 2 || taskpubType == 3){
        		title = "查看分发记录";
        	}
        	var dg =W.$.dialog({
           		id:'viewTask',
           		title:title,
           		width: 800,
           		height: 500,
           		lock: true,
           		min:true,
           		max:true,
           		content:"url:${r'${ctx}'}/major/scplan/checkPlan!viewTask?params.pubid="+pubid+"&params.taskpub_type="+taskpubType,
           		cancelVal: "关闭",
           		cancel:function(){
           			return true;
           		}
           	});
        } 
        
        var moduleTag = "taskPubModule"; 
        /**
         * 加载通知分类
         */
        $("#taskName").combobox({
            url : "${r'${ctx}'}/major/scplan/localPlan!getlocalPlanTaskTree?ttype="+ttype+'&params.moduleTag='+moduleTag,
            multiple : false,
            cascadeCheck : false,
            valueField: 'id',
            textField: 'name',
            onSelect : function(node) {

            }
        });
        
    	
        MplanSp.formatTaskType = function(value,row,index){
        	var taskpubType=row.taskpub_type;
        	var pubid = row.pubid;
        	if(taskpubType == 2){
        		var a = '<a style="color:blue;" iconCls="icon-group" href="javascript:void(0)" onclick="MplanSp.viewTask(\''+pubid+'\','+taskpubType+')">查看</a>';
            	return a;
        	}else if(taskpubType == 1){
        		var a = '<a style="color:blue;" iconCls="icon-group" href="javascript:void(0)" onclick="MplanSp.viewTask(\''+pubid+'\','+taskpubType+')">查看</a>';
            	return a;
        	}else{
        		return "无";
        	}
        }

    /**
     * 获取字典项-通知层级  伍红林
     */
     var storelevel;
    $("#storeLevel").combotree({
        multiple : false,
        cascadeCheck : false,
        valueField: 'id',
    	textField: 'text',
        data:[{
	        "id":3,
	        "text":"省"
	    },{
	        "id":2,
	        "text":"市"
	    },{
	        "id":1,
	        "text":"县"
	    }],
        onSelect : function(node) {
            var tree = $(this).tree;
            var isLeaf = tree('isLeaf', node.target);
            if (!isLeaf) {
                W.$.dialog.alert("请选择最末一级节点");
                return false;
            }
            storelevel=node.id;
        }
    });

	/**
	 * 初始化进入加载通知列表。
	 */
	$("#taskList").datagrid({
		fit:true,
		pagination:true,
		rownumbers:true,
		multeSelect:true,
		striped: true,
		remoteSort: false,
		iconCls:'icon-edit',
		pageSize:20,
        toolbar:'#toolButton1',
		queryParams:{
				"params.ttype":ttype
			},		
		url:'${r'${ctx}'}/major/scplan/mPlan!getTaskList',
		onLoadSuccess:function(data){
	
		}
	});



    /**
	 *查询按钮事件【基本查询】  伍红林
	 */
    MplanSp.searchTaskByCondition = function(){
		//项目名称
        var taskName = $("#taskName").combobox("getText");
		if(taskName=="全部计划通知" ){			
			taskName="";
		}
        var sTime = $("#publicStartTIME_s").val();
        var eTime = $("#publicStartTIME_e").val();
        var taskYear = $("#taskYear").val();
        var taskStatus = $("#taskStatus").combobox("getValue");

        if(sTime !="" && sTime!=""){
            if (sTime > eTime) {
                $.dialog.alert('通知结束的开始时间不能小于结束时间', "warning");
            }
		}
		$("#taskList").datagrid('reload',{
			"params.ttype":ttype,
			"params.taskName":taskName,
			"params.sTime":sTime,
			"params.eTime":eTime,
			"params.taskYear":taskYear,
			"params.storeLevel":storelevel,
			"params.taskStatus":taskStatus
		});
	};
	
	/**
	 * 重置按钮事件【清空查询条件】
	 */
    MplanSp.removeTaskConditions = function(){
		$("#taskForm").form("clear");
		storelevel = null;
		//重置查询
        MplanSp.searchTaskByCondition();
	};

    /**
	 * 撤回
	 */
    MplanSp.reCallTaskWin = function(){
        var getrow="";
        if ($('#taskList').datagrid('getSelections').length > 0) {
            if ($('#taskList').treegrid('getSelections').length > 1) {
                $.dialog.alert('只能选择一个通知', "warning");
                return false;
            }
        } else {
            $.dialog.alert('请选择一个通知', "warning");
            return false;
        }
        if ($('#taskList').datagrid('getSelections').length > 0&& $('#taskList').datagrid('getSelections')[0].t_status=='6') {
            getrow = $('#taskList').datagrid('getSelections');
            var msg="",msg1="",msg2="",msg3="",msg4="";
            var taskids="";
            var pubFids="";
            var callMsgs="";
            for(var i=0;i<getrow.length;i++){
                var row=getrow[i];
                //通知状态(-1=创建，0=新增，1=已下发，2=已分发,3=已发布，4=已撤回，5=已入库)
                var t_status=row.t_status;
                var task_name=row.task_name;
                var task_id=row.task_id;
                var pubFid=row.pub_fid;
                var pubid=row.pubid;
				debugger;
				$.ajax({
                    async : false,
                    cache:false,
                    data:  {
                    	"params.status" : -1,//项目状态(-1则查询，备察和退回状态)
                    	"params.moduleTag" : "made",//编制区（made）、审核区(check)
                    	"ttype":ttype,
                        "params.taskID":task_id
                    },
                    type: 'POST',
                    dataType : 'json',
                    url: '${r'${ctx}'}/major/scplan/checkPlan!getTaskProjectList',
                    error: function (data) {
                    },
                    success:function(data){
                    	if(data.total > 0){
                    		msg1 += task_name+"、";
                    	}
                    }
                });
				
                if(t_status == 1 || t_status ==2||t_status ==6){
                    callMsgs += task_id+"#"+pubFid+"#"+t_status+"#"+pubid+",";
                    taskids+=task_id+",";
                    pubFids+=pubFid+",";
                }else if(t_status == 3){
                	msg2 += task_name+"、";
                }else if(t_status == 4){
                	msg3 += task_name+"、";
                }else if(t_status == 5){
                	msg4 += task_name+"、";
                }else{
                    msg += task_name+"、";
				}

                var lock_status=getrow.t_status;
        		if(lock_status == "7"){
                    $.dialog.alert('通知已结束，不能操作！', "warning");
                    return false;
        		}
        		
			}
                $.dialog.confirm("您确定撤回所选通知吗？",function(){
                    $.ajax({
                        async : false,
                        cache:false,
                        data:  {
                            "params.taskids":taskids,
                            "params.callMsgs":callMsgs,
                            "params.opt":"recall",
                            "params.ttype":ttype
                        },
                        type: 'POST',
                        dataType : 'text',
                        url: '${r'${ctx}'}/major/scplan/mPlan!recallDeptRes.action',
                        error: function (data) {
                            $.dialog.alert(data.msg,'warning');
                        },
                        success:function(data){
                            var json=eu.jsonToObj (data);
                            if(json.code == "0"){
                                $.dialog.tips(json.content,1);
                                $("#taskList").datagrid('reload');
                            }else{
                                $.dialog.alert(json.msg,'warning');
                            }
                        }
                    });
                });

        } else {
            $.dialog.alert('请选择已暂停的通知', "warning");
            return false;
        }
	}
    /**
	 * 下发
	 */
    MplanSp.sendTaskWin = function(title,sendType) {
        var getrow = "";
        if ($('#taskList').datagrid('getSelections').length > 0) {
            if ($('#taskList').treegrid('getSelections').length > 1) {
                $.dialog.alert('只能选择一个通知', "warning");
                return false;
            }
        } else {
            $.dialog.alert('请选择一个通知', "warning");
            return false;
        }
        getrow = $('#taskList').datagrid('getSelections')[0];
        if(getrow.t_status == '1'){
            $.dialog.alert('通知已下发！', "warning");
            return false;
        }
		if(getrow.t_status == '6'){
			var lock_status=getrow.t_status;
	        //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回，5=已入库)
			var t_status=getrow.t_status;
			//-1为自己创建的
	        var pub_fid=getrow.pub_fid;
			var tst='1';
	        $.dialog.confirm("您确定启用所选通知吗？",function(){
	            $.ajax({
	                async : false,
	                cache:false,
	                data:  {
	                    "params.ttype":ttype
	                },
	                type: 'POST',
	                dataType : 'text',
	                url: '${r'${ctx}'}/major/scplan/mPlan!stopTask.action?params.sendType='+tst+'&params.task_id='+getrow.task_id+'&ttype='+ttype+'&params.pubid='+getrow.pubid+'&params.t_status='+t_status,
	                error: function (data) {
	                    $.dialog.alert(data,'warning');
	                },
	                success:function(data){
	                    $.dialog.tips(data,1);
	                    $("#taskList").datagrid('reload');
	                }
	            });
	        });

		}else {
			var lock_status=getrow.t_status;
			/* if(lock_status == "6"){
	            $.dialog.alert('通知已暂停，不能操作！', "warning");
	            return false;
			} */
			if(lock_status == "7"){
	            $.dialog.alert('通知已结束，不能操作！', "warning");
	            return false;
			}

	        //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回，5=已入库)
			var t_status=getrow.t_status;
			//-1为自己创建的
	        var pub_fid=getrow.pub_fid;

	        if(sendType == "1"){
	            /* if(t_status == 1){
	                $.dialog.alert('通知已下发！', "warning");
	                return false;
	            }*/if(t_status == 2){
	                $.dialog.alert('通知已分发！', "warning");
	                return false;
	            } 
	            if(t_status == 3){
	                $.dialog.alert('通知已发布！', "warning");
	                return false;
	            }
	            if(t_status == 5){
	                $.dialog.alert('通知已入库！', "warning");
	                return false;
	            }
	            if(pub_fid != "-1") {
	                $.dialog.alert('此通知不由您创建，不能进行下发', "warning");
	                return false;
	            }
	        }

	        var dg = $.dialog({
	            id : 'TaskWin',
	            title : title,
	            width : 1200,
	            height : 600,
	            lock : true,
	            min : true,
	            max : true,
	            content : 'url:${r'${ctx}'}/major/scplan/mPlan!sendTask?params.sendType='+sendType+'&params.task_id='+getrow.task_id+'&ttype='+ttype+'&params.pubid='+getrow.pubid+'&params.t_status='+t_status,
	            button : [ {
	                id : "editSendTaskWin",
	                name : '确定',
	                callback : function() {
	                    var iframe = this.iframe.contentWindow;
	                    var message = iframe.MplanSp.saveRes();
	                    var json=eu.jsonToObj (message);
	                    if(json){
	                        if(json.messageType == "0"){
	                            W.$.dialog.tips(json.content, 3);
	                            // 关闭弹出的对话框
	                            //$('#taskList').datagrid("clearChecked");
	                            // 重新加载一览画面
	                            $("#taskList").datagrid('reload');
	                            return true;
	                        } else{
	                            // 错误提示
	                            W.$.dialog.alert(json.content,"error");
	                        }
	                    }
	                },
	                focus : true
	            } ],
	            ok : null,
	            cancelVal : "关闭",
	            cancel : function() {
	                return true;
	            }
	        });
		}

	};
//暂停
 MplanSp.stopTaskWin = function(sendType) {
        var getrow = "";
        if ($('#taskList').datagrid('getSelections').length > 0) {
            if ($('#taskList').treegrid('getSelections').length > 1) {
                $.dialog.alert('只能选择一个通知', "warning");
                return false;
            }
        } else {
            $.dialog.alert('请选择一个通知', "warning");
            return false;
        }
        getrow = $('#taskList').datagrid('getSelections')[0];

		var lock_status=getrow.t_status;
		if(lock_status == "6"){
            $.dialog.alert('通知已暂停，不能操作！', "warning");
            return false;
		}
		if(lock_status == "7"){
            $.dialog.alert('通知已结束，不能操作！', "warning");
            return false;
		}

        //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回，5=已入库)
		var t_status=getrow.t_status;
		//-1为自己创建的
        var pub_fid=getrow.pub_fid;

        if(sendType == "6"){
            /* if(t_status == 1){
                $.dialog.alert('通知已下发！', "warning");
                return false;
            }*/if(t_status == 7){
                $.dialog.alert('通知已结束！', "warning");
                return false;
            } 
            if(t_status == 0||t_status == 4){
                $.dialog.alert('通知未启用！', "warning");
                return false;
            }
            /* if(pub_fid != "-1") {
                $.dialog.alert('此通知不由您创建，不能进行下发', "warning");
                return false;
            } */
        }

        $.dialog.confirm("您确定暂停所选通知吗？",function(){
            $.ajax({
                async : false,
                cache:false,
                data:  {
                    "params.ttype":ttype
                },
                type: 'POST',
                dataType : 'text',
                url: '${r'${ctx}'}/major/scplan/mPlan!stopTask.action?params.sendType='+sendType+'&params.task_id='+getrow.task_id+'&ttype='+ttype+'&params.pubid='+getrow.pubid+'&params.t_status='+t_status,
                error: function (data) {
                    $.dialog.alert(data,'warning');
                },
                success:function(data){
                    $.dialog.tips(data,1);
                    $("#taskList").datagrid('reload');
                }
            });
        });

	};
//结束
MplanSp.endTaskWin = function(sendType) {
        var getrow = "";
        if ($('#taskList').datagrid('getSelections').length > 0) {
            if ($('#taskList').treegrid('getSelections').length > 1) {
                $.dialog.alert('只能选择一个通知', "warning");
                return false;
            }
        } else {
            $.dialog.alert('请选择一个通知', "warning");
            return false;
        }
        getrow = $('#taskList').datagrid('getSelections')[0];

		var lock_status=getrow.t_status;
		if(lock_status == "6"){
            $.dialog.alert('通知已暂停，不能操作！', "warning");
            return false;
		}
		if(lock_status == "7"){
            $.dialog.alert('通知已结束，不能操作！', "warning");
            return false;
		}

        //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回，5=已入库)
		var t_status=getrow.t_status;
		//-1为自己创建的
        var pub_fid=getrow.pub_fid;

        if(sendType == "7"){
            /* if(t_status == 1){
                $.dialog.alert('通知已下发！', "warning");
                return false;
            }*/if(t_status == 7){
                $.dialog.alert('通知已结束！', "warning");
                return false;
            } 
            if(t_status == 0||t_status == 4){
                $.dialog.alert('通知未启用！', "warning");
                return false;
            }
            /* if(pub_fid != "-1") {
                $.dialog.alert('此通知不由您创建，不能进行下发', "warning");
                return false;
            } */
        }

        $.dialog.confirm("您确定结束所选通知吗？",function(){
            $.ajax({
                async : false,
                cache:false,
                data:  {
                    "params.ttype":ttype
                },
                type: 'POST',
                dataType : 'text',
                url: '${r'${ctx}'}/major/scplan/mPlan!stopTask.action?params.sendType='+sendType+'&params.task_id='+getrow.task_id+'&ttype='+ttype+'&params.pubid='+getrow.pubid+'&params.t_status='+t_status,
                error: function (data) {
                    $.dialog.alert(data,'warning');
                },
                success:function(data){
                    $.dialog.tips(data,1);
                    $("#taskList").datagrid('reload');
                }
            });
        });

	};
	 /**
	 * 录入通知信息
     * @param operate add:新增 edit:编辑
     */
	MplanSp.openWinPlan = function(operate){
		var url;
		if(operate=="edit"){
			var getrow = "";
      		if ($('#taskList').datagrid('getSelections').length > 0) {
           		if ($('#taskList').treegrid('getSelections').length > 1) {
            		$.dialog.alert('只能选择一个通知', "warning");
                return false;
            	}
       		} else {
           		$.dialog.alert('请选择一个通知', "warning");
            	return false;
        	}
        	getrow = $('#taskList').datagrid('getSelections')[0];
            //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回)
            var t_status=getrow.t_status;

            var lock_status=getrow.t_status;
    		if(lock_status == "7"){
                $.dialog.alert('通知已结束，不能操作！', "warning");
                return false;
    		}
            if(t_status == 1 ){
                $.dialog.alert('通知已下发，不能修改！', "warning");
                return false;
            }
            if(t_status == 2 ){
                $.dialog.alert('分发的通知，不能修改！', "warning");
                return false;
            }
            if(t_status == 3 ){
                $.dialog.alert('通知已发布，不能修改！', "warning");
                return false;
            }
            if(t_status == 5 ){
                $.dialog.alert('通知已入库，不能修改！', "warning");
                return false;
            }

        	url = "url:${r'${ctx}'}/major/scplan/mPlan!input.action?operate="+operate+"&taskId="+getrow.task_id+"&ttype="+ttype;
        }else{
        	url = "url:${r'${ctx}'}/major/scplan/mPlan!input.action?operate="+operate+"&ttype="+ttype;
        }
		
		var dg = W.$.dialog({
			id:'inputTask',
			title:'通知信息',
			width: 800,
			height:500,
			lock: true,
			min:true,
			max:true,
			content:url,
			button:[{
					id : "save",
					name : '保存',
					callback : function() {
                        var iframe = this.iframe.contentWindow;
                        var message = iframe.MplanSp.saveTaskInfo();
                        var json=eu.jsonToObj (message);
                        if(json){
                            if(json.code == "Success"){
                                // 用户更新或增加成功
                                $.dialog.tips(json.msg, 3);
                                // 重新加载一览画面
                                $("#taskList").datagrid('reload');
                                return true;
                            }else if(json.code == "Error"){
                                // 用户更新或增加成功
                                $.dialog.tips(json.msg, 3);
                                $("#taskList").datagrid('reload');
                                return true;
							}else if(json.code == "warning"){
                                // 用户更新或增加成功
                                $.dialog.alert(json.msg, 3);
                                $("#taskList").datagrid('reload');
                                return true;
							}else{
                                // 错误提示
                                $.dialog.alert(json.msg,"error");
                                 return false;
                            }
                        }
					},
					focus : true
				}],
            ok : null,
            cancelVal : "关闭",
            cancel : function() {
                return true;
            }
		});
	};

    /**
	 * 删除通知
     */
    MplanSp.deleteTask =function(){
        if ($('#taskList').datagrid('getSelections').length > 0) {
            var getrow = $('#taskList').datagrid('getSelections');

            var msg1="",msg2="",msg3="",msg4="",msg="";
            var num1=1,num2=1,num3=1,num4=1;
            var taskIDs="";
            for(var i=0;i<getrow.length;i++){
                var row=getrow[i];
                //通知状态(0=新增，1=已下发，2=已分发,3=已发布，4=已撤回)
                var t_status=row.t_status;
                var task_name=row.task_name;
                var task_id=row.task_id;
                var pubFid=row.pub_fid;
                var pubid=row.pubid;

                if(t_status == -1 || t_status == 0 || t_status == 4){
                    taskIDs+=task_id+",";
                }
                if(t_status == 1){
                	if(msg1 != ""){
                		num1++;
                	}else{
                		msg1 += task_name;
                	}
            	}if(t_status == 2){
            		if(msg2 != ""){
                		num2++;
                	}else{
                		msg2 += task_name;
                	}
            	}if(t_status == 3){
            		if(msg3 != ""){
                		num3++;
                	}else{
                		msg3 += task_name;
                	}
            	}if(t_status == 5){
            		if(msg4 != ""){
                		num4++;
                	}else{
                		msg4 += task_name;
                	}
            	}
            }

            if(msg1 != ""){
           		msg+="通知 “"+msg1+"” 等 "+num1+" 个通知已下发，";
            }if(msg2 != ""){
           		msg+="通知 “"+msg2+"” 等 "+num2+" 个通知已分发，";
            }if(msg3 != ""){
           		msg+="通知 “"+msg3+"” 等 "+num3+" 个通知已发布，";
            }if(msg4 != ""){
           		msg+="通知 “"+msg4+"” 等 "+num4+" 个通知已入库，";
            }
            if(msg != ""){
            	$.dialog.alert(msg + "不能进行删除操作！", "warning");
               	return false;
            }
            if(t_status == '6'){
            	$.dialog.alert('通知已暂停，不能操作！', "warning");
                return false;
            }
            if(t_status == '7'){
            	$.dialog.alert('通知已结束，不能操作！', "warning");
                return false;
            }
            $.dialog.confirm("您确定删除所选通知吗？",function(){
                $.ajax({
                    async : false,
                    cache:false,
                    data:  {
                        "params.taskids":taskIDs,
                        "params.ttype":ttype
                    },
                    type: 'POST',
                    dataType : 'text',
                    url: '${r'${ctx}'}/major/scplan/mPlan!deleteTask.action',
                    error: function (data) {
                        $.dialog.alert(data.msg,'warning');
                    },
                    success:function(data){
                        var json=eu.jsonToObj (data);
                        $.dialog.tips(json.msg,1);
                        $("#taskList").datagrid('reload');
                    }
                });
            });

        }else{
            $.dialog.alert('请选择一个通知', "warning");
            return false;
		}
	}

})();
//闭包结束
</script>
</body>
</html>
