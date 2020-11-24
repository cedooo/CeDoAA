<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${r'${systemName}'}</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>

<!-- 以下引入zTree相关js/css -->
<link rel="stylesheet" href="${r'${ctx}'}/js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript"  src="${r'${ctx}'}/js/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<link rel="stylesheet" type="text/css" href="${r'${ctx}'}/themes/metro/ui.metro.selectionTree.css"/>
<script type="text/javascript" src="${r'${ctx}'}/js/UI/ui.core.min.js"></script>
<script type="text/javascript" src="${r'${ctx}'}/js/UI/ui.selectionTree.min.js"></script>
<style>
	.searchArea.panel-body.layout-body {background: #fff;}
</style>
</head>

<!-- easyui-layout布局 -->
<body class="easyui-layout" fit="true" style="overflow: hidden;"> 
	<!-- 中间部分 列表 -->
            <div data-options="region:'west',split:true,collapsible:false" style="width: 200px;">
                <ul id="sendTypeTreeView" class="ztree" style="margin-top:0; width:300px; height: 300px;"></ul>
            </div>
            <div data-options="region:'center',split:true,collapsible:false" class="searchArea" style="width: 650px;">
                <div id="cmdeptButton8" style="display: none;">
                        <form id="departmentSearchForm8" method="post">
                            <table class="showtable" >
                                <tr>
                                    <td>
                                        <span>部门名称：</span>
                                        <input type="text" id="departmentName8" class="easyui-textbox" style="width: 150px;"/>
                                    </td>
                                    <td>
                                        <span>&nbsp;部门类型：</span>
                                        <select id="optdeptType" style="width: 120px;">
                                            <!-- <option value="FGW">发改委</option> -->
                                            <option value="">全部</option>
                                            <option value="FGWCS">科室</option>
                                            <option value="DEPT">行业部门</option>
                                            <option value="ZDCOMPANY">重点企业</option>
                                           <!--  <option value="DEPT">行业部门</option>
                                            <option value="COMPANY">重点企业</option> -->
                                            <!--<option value="ZF">政府</option>-->
                                        </select>
                                    </td>
                                    <td>
                                        <div style="margin-top: 5px">
                                            <a id="btnQuery8" href="#" class="easyui-linkbutton m-btn searchBtn" style="width:80px;margin-left:5px;"  onclick="javascript:ConfigSp.searchDept8()"></a>
                                            <a id="btnQuery88" href="#" class="easyui-linkbutton" style="width:80px;margin-left:5px;"  onclick="javascript:ConfigSp.selectDEPT()">添加</a>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                </div>

               <table id="sc-datagrid" fit="true" style="width:100%;height:100px"></table>
            </div>
            <div data-options="region:'east',split:true" class="searchArea">
                <table class="showtable" >
                    <tr>
                        <td>
                            <div style="margin-top: 5px">
                                <a id="cleanQuery8" href="#" class="easyui-linkbutton" style="width:80px;margin-left:5px;"  onclick="javascript:ConfigSp.cleanSelectDEPT()">清空</a>
                                <a id="cleanQuery9" href="#" class="easyui-linkbutton" style="width:80px;margin-left:5px;"  onclick="javascript:ConfigSp.delSelectDEPT()">删除</a>
                                <!-- &nbsp;&nbsp;<span id="checkNum"></span> -->
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <ul id="selectData" class="ulStyle">
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>
    </div>
</body>

<script type="text/javascript">
ConfigSp.depttype = function(value, rowData, rowIndex){
	
	if (value=="DEPT"){
		return "行业部门";
	}else if(value=="FGWCS"){
		return "科室";
	}else if(value=="ZDCOMPANY"){
		return "重点企业";
	}	
}
var api = frameElement.api, W = api.opener;
var sendType="${r'${params.sendType}'}";
var taskId="${r'${params.task_id}'}";
var pubid="${r'${params.pubid}'}";//通知发布表id
var t_status="${r'${params.t_status}'}";

var areaId="";
var deptType="";
var _sendTask = [];

;(function(){
    initDeptTreeData();
    loadSendTask();


    //下发/分发树
    var resourceSetting = {
        view: {
            selectedMulti: false,
            showLine: true, //是否显示线，true为显示，false为不显示
            showIcon: false,
            dblClickExpand:false
        },
        data: {
            key:{
                name: "name"
            },
            simpleData: {
                enable: true,
                idKey : "id",
                pIdKey : "pId"
            }
        },
        check:{
            enable: false
        },
        callback:{
            onClick:function(event, treeId, treeNode, clickFlag){

                deptType = $.trim($("#optdeptType").val());
                areaId=treeNode.id

                ConfigSp.searchDept8();
            }
        }
    }



    /**
     * 页面展示树设定数据
     */
    function initDeptTreeData() {
        selectData = $("#selectData");

        $.ajax({
            cache : false,
            type : 'POST',
            dataType : "json",
            traditional : true,
            //url: "${r'${ctx}'}/major/scplan/commonPlan!getAllAREADepartment.action",//请求的action路径
            url : "${r'${ctx}'}//major/scplan/commonPlan!getAreaTree",//请求的action路径
            error : function() {//请求失败处理函数
                //
            },
            success : function(data) { //请求成功后处理函数。
                $.fn.zTree.init($("#sendTypeTreeView"), resourceSetting, data);

                var treeObj = $.fn.zTree.getZTreeObj("sendTypeTreeView");
                var nodes = treeObj.getNodes();
                treeObj.selectNode(nodes[0]);//设置父节点为选中
                treeObj.expandNode(nodes[0], true, null, null, true);//默认展开根节点

                areaId=nodes[0].id
                /* deptType ="FGWCS"; */
				csType = "2";
                //加载部门数据
                loadScDataGrid(areaId,deptType);

            }
        });
    }

    /**
     * 加载部门数据
     */
    function loadScDataGrid(areaId,deptType){
        $('#sc-datagrid').datagrid({
            rownumbers: true,
            singleSelect: false,
            queryParams:{
                "params.areaId":areaId,
                "params.cstype":csType,
                "params.type":deptType
            },
            url:'${r'${ctx}'}/major/scplan/commonPlan!getAreaDeptData.action',
            pagination:true,
            pageSize:20,
            fitColumns: true,
            method: 'post',
            toolbar:'#cmdeptButton8',
            frozenColumns:[[
                {field:'departmentGuid',checkbox:true}
            ]],
            columns:[[
                {field:'departmentFullname',title:'部门名称',width:80,align:'left'},
                {field:'createTime',title:'创建时间',width:80,align:'center',hidden:true},
                {field:'type',title:'部门类型',width:80,align:'center',formatter:ConfigSp.depttype}
            ]],
            loadFilter:function(data) {
                return {'total': data.totalrecords, 'rows': data.result}
            }

        });
        $('#cmdeptButton8').show();
    }
    
    //加载已下发或已分发记录
    function loadSendTask(){
    	debugger;
    	if(t_status == 1||t_status == 4||t_status == 6){
    		var taskpubType = 1;
    		$.ajax({
                 async : false,
                 cache:false,
                 data:  {
                     "params.pubid":taskId,
                     "params.taskpubType":taskpubType
                 },
                 type: 'POST',
                 dataType : 'json',
                 url: '${r'${ctx}'}/major/scplan/showTaskInfo!getSendTaskMsg',
                 error: function (data) {
                     
                 },
                 success:function(data){
                 	var inHtml = "";
                     if(data.length>0){
                     	for(var i=0;i<data.length;i++){
                 			var sendJson = {};
                     		if(data[i].taskpub_type == "2"){
                     			inHtml += "<li id="+ data[i].dept_id +"><label>"+  data[i].department_fullname +"</label></li>";
                     			sendJson.fcre = "0";
                     			sendJson.xiafa = "1";
                     			sendJson.fenfa = "0";
                     			sendJson.dept_id = data[i].dept_id;
                     		}else if(data[i].taskpub_type == "1"){
                     			sendJson.fcre = "1";
                     			sendJson.xiafa = "0";
                     			sendJson.fenfa = "0";
                     			sendJson.dept_id = data[i].dept_id;
                     		}else{
                     			sendJson.fcre = "0";
                     			sendJson.xiafa = "0";
                     			sendJson.fenfa = "1";
                     			sendJson.dept_id = data[i].dept_id;
                     		}
                     		_sendTask.push(sendJson);
                     	}
                     	$("#selectData").append(inHtml);
                     }
                 }
           });
    	}else if(t_status == 2||t_status == 4||t_status == 6){
    		var taskpubType = 2;
    		$.ajax({
                 async : false,
                 cache:false,
                 data:  {
                     "params.pubid":taskId,
                     "params.taskpubType":taskpubType
                 },
                 type: 'POST',
                 dataType : 'json',
                 url: '${r'${ctx}'}/major/scplan/showTaskInfo!getSendTaskMsg',
                 error: function (data) {
                     
                 },
                 success:function(data){
                 	var inHtml = "";
                     if(data.length>0){
                     	for(var i=0;i<data.length;i++){
                 			var sendJson = {};
                     		if(data[i].taskpub_type == "2"){
                     			sendJson.fcre = "0";
                     			sendJson.xiafa = "1";
                     			sendJson.fenfa = "0";
                     			sendJson.dept_id = data[i].dept_id;
                     		}else if(data[i].taskpub_type == "1"){
                     			sendJson.fcre = "1";
                     			sendJson.xiafa = "0";
                     			sendJson.fenfa = "0";
                     			sendJson.dept_id = data[i].dept_id;
                     		}else{
                     			inHtml += "<li id="+ data[i].dept_id +"><label>"+  data[i].department_fullname +"</label></li>";
                     			sendJson.fcre = "0";
                     			sendJson.xiafa = "0";
                     			sendJson.fenfa = "1";
                     			sendJson.dept_id = data[i].dept_id;
                     		}
                     		_sendTask.push(sendJson);
                     	}
                     	$("#selectData").append(inHtml);
                     }
                 }
           });
    	}
    }
    
    /**
     * 下拉后执行查询
     */
    $("#optdeptType").change(function(){
        ConfigSp.searchDept8();
    });
    
    /**
     * 查询
     */
    ConfigSp.searchDept8 = function() {
        // 获取查询条件
        var departmentName = $.trim($("#departmentName8").val());
        var optdeptType = $.trim($("#optdeptType").val());

        $('#sc-datagrid').datagrid({
            queryParams: {
                "params.deptName":departmentName,
                "params.areaId": areaId,
                "params.cstype":csType,
                "params.type":optdeptType
            },
            url:"${r'${ctx}'}/major/scplan/commonPlan!getAreaDeptData.action"
        });
    };


    //定义接收选择的值
    var _deptIds=[];
    var _deptNames=[];
    var _type=[];

    /**
     * 点击选择
     */
    ConfigSp.selectDEPT=function(){
        var rowDatas="";
        if ($('#sc-datagrid').datagrid('getSelections').length > 0) {
            rowDatas = $('#sc-datagrid').datagrid('getSelections');
        } else {
            W.$.dialog.alert('至少选择一个部门', "warning");
            return false;
        }

        if(rowDatas != ""){
            var htmlStr="";
            for(var i=0;i<rowDatas.length;i++){
                var _row=rowDatas[i];
                var flag = true;
                if(_sendTask.length > 0){
                	for(var x=0;x<_sendTask.length;x++){
						if(_row.departmentGuid == _sendTask[x].dept_id){
                     		if(_sendTask[x].fcre == "1"){
                     			W.$.dialog.alert('该部门为通知创建部门，请勿选择！', "warning");
                     		}else if(_sendTask[x].xiafa == "1"){
                     			W.$.dialog.alert('通知已下发至该部门，请勿重复下发！', "warning");
                     		}else if(_sendTask[x].fenfa == "1"){
                     			W.$.dialog.alert('通知已分发至该部门，请勿重复下发！', "warning");
                     		}
							flag = false;
						}
					}
					if(flag){
		                if(_deptIds.length > 0){
		                    if(_deptIds.contains(_row.departmentGuid)){
		                        //htmlStr += _row.departmentFullname+" 、";
		                        htmlStr += "<li id="+ _row.departmentGuid +"><label for='input_"+_row.departmentGuid+"'><input name='label_input' id='input_"+_row.departmentGuid+"' type='checkbox'>"+  _row.departmentFullname +"</label></li>";
		
		
		                        _deptIds.push(_row.departmentGuid);
		                        _deptNames.push(_row.departmentFullname);
		                        _type.push(_row.type);
		                    }
		                }else{
		                    //htmlStr += _row.departmentFullname+" 、";
		                    htmlStr += "<li id="+ _row.departmentGuid +"><label for='input_"+_row.departmentGuid+"'><input name='label_input' id='input_"+_row.departmentGuid+"' type='checkbox'>"+  _row.departmentFullname +"</label></li>";
		
		                    _deptIds.push(_row.departmentGuid);
		                    _deptNames.push(_row.departmentFullname);
		                    _type.push(_row.type);
		                }
		            }
	                
                }else{
                	if(_deptIds.length > 0){
	                    if(_deptIds.contains(_row.departmentGuid)){
	                        //htmlStr += _row.departmentFullname+" 、";
	                        htmlStr += "<li id="+ _row.departmentGuid +"><label for='input_"+_row.departmentGuid+"'><input name='label_input' id='input_"+_row.departmentGuid+"' type='checkbox'>"+  _row.departmentFullname +"</label></li>";
	
	
	                        _deptIds.push(_row.departmentGuid);
	                        _deptNames.push(_row.departmentFullname);
	                        _type.push(_row.type);
	                    }
	                }else{
	                    //htmlStr += _row.departmentFullname+" 、";
	                    htmlStr += "<li id="+ _row.departmentGuid +"><label for='input_"+_row.departmentGuid+"'><input name='label_input' id='input_"+_row.departmentGuid+"' type='checkbox'>"+  _row.departmentFullname +"</label></li>";
	
	                    _deptIds.push(_row.departmentGuid);
	                    _deptNames.push(_row.departmentFullname);
	                    _type.push(_row.type);
	                }
                }
				
            }


            /* $("#checkNum").html(_deptIds.length); */
            $("#selectData").append(htmlStr);
        }
    }

    /**
     * 排除选择相同的部门
     */
    Array.prototype.contains = function ( needle ) {
        for (i in this) {
            if (this[i] == needle) return false;
        }
        return true;
    }

    /**
     * 清空选择的部门
     */
    ConfigSp.cleanSelectDEPT=function(){
        /* $("#selectData").html("");
        _deptIds=[];
        _deptNames=[];
        _type=[];

        $('#sc-datagrid').datagrid('clearSelections'); */
        /* for(var j=0; j < _deptIds.length; j++){
        	var li = $("#" + _deptIds[j]);
            _deptIds.splice(j, 1); //将这个元素移除
            _deptNames.splice(j, 1); //将这个元素移除
            _type.splice(j, 1); //将这个元素移除
            li.remove();
        } */
        var length = _deptIds.length;
        for(var j=0; j < length; j++){
         	var li = $("#" + _deptIds[j]);
            li.remove(); 
        }
        _deptIds=[];
        _deptNames=[];
        _type=[];
        _sendTask=[];
        
        /* $("#checkNum").html(""); */
    }


    //点击操作前左侧树被选取的所有数据集合
    var checkq;
    //加载的标签
    var selectData ;

    /**
     * 删除
     */
    ConfigSp.delSelectDEPT=function(){
        var selectList = getSelectedInfo();
        var selToRemove = selectData.find('li');

        for (var i = 0; i < selectList.length; i++) {
            var id = selectList[i].id;
            var li = $("#" + id);

            for(var j=0; j < _deptIds.length; j++){
                if(_deptIds[j] == id){
                    _deptIds.splice(j, 1); //将这个元素移除
                    _deptNames.splice(j, 1); //将这个元素移除
                    _type.splice(j, 1); //将这个元素移除
                }
            }
            li.remove();
        }

        /* $("#checkNum").html(_deptIds.length); */
        //lefttree.refresh();

    }

    /**
     * 获取选中的船载终端类型
     */
    function getSelectedInfo() {
        var selectList = new Array(); //排序列的Array
        var items = document.getElementsByName("label_input");
        for (var index = 0; index < items.length; index++) {
            var info = items[index];
            if (info.checked) {
                var data = {
                    id : info.id.split("_")[1]
                };
                selectList.push(data);
            }
        }
        return selectList;
    }


    /**
     * 构造资源的数据
     * @returns {string}
     */
   function getSaveParam(){
        var data={};
        data.taskId=taskId;
        data.pubid=pubid;
        data.sendType=sendType;
        var deptIds=[];
        var deptNames=[];
        var type=[];


        if(_deptIds.length > 0){
            data.deptIds=_deptIds.join(",");
            data.deptNames=_deptNames.join(",");
            data.type=_type.join(",");
        }else{
            data.deptIds="";
            data.deptNames="";
            data.type="";
        }


        // //获取树对象
        // var treeObj = $.fn.zTree.getZTreeObj("sendTypeTreeView");
        // // 获取选中节点对象
        // var nodes = treeObj.getCheckedNodes(true);
        // // 如果存在被选中的节点
        // if (nodes.length > 0) {
        //     // 遍历选中节点
        //     $.each(nodes,function(i,n){
        //         // 选中的资源ID
        //         deptIds.push(n.id);
        //         // 选中的资源类型
        //         type.push(n.type);
        //     });
        //     data.deptIds=deptIds.join(",");
        //     data.type=type.join(",");
        // }else{
        //     data.deptIds="";
        //     data.type="";
        // }
        return data;
	}

    /**
     * 下发时调用
     * @returns {*}
     */
    MplanSp.saveRes=function(){
        if(_deptIds.length <= 0){
        	//请求比对是否重复挑选
            var data = getSaveParam();
            var deptIds=data.deptIds;
            var taskId = data.taskId;
            var response = $.ajax({
               async : false,
               cache:false,
               data:  {
                   "params.selectDepentments":deptIds+",",
                   "params.taskId":taskId
               },
               type: 'POST',
               dataType : 'text',
               url: '${r'${ctx}'}/major/scplan/mPlan!isSelectDepartment.action',
               error: function (data) {
            	   
               },
               success:function(data){
            	   
               }
            }).responseText
            response = JSON.parse(response);
            /* if(response.code=="ParamError"){
            	W.$.dialog.alert("所选部门("+response.resultData+"),已经被其他部门挑选!");
            	return;
            }else  */if(response.code=="Error"){
            	W.$.dialog.alert(response.Msg);
            	return;
            }
        	 var responseText;
             responseText = $.ajax({
                 url : '${r'${ctx}'}/major/scplan/mPlan!saveDeptRes.action',
                 type : 'POST',
                 async : false,
                 data : getSaveParam(),
                 success: function(data) {
                 },
                 error : function(data) {
                 }
             }).responseText;
             return responseText;
        	
        	
           /*  W.$.dialog.alert('未选择下发部门', "warning");
            return false; */
        }else{
            //请求比对是否重复挑选
            var data = getSaveParam();
            var deptIds=data.deptIds;
            var taskId = data.taskId;
            var response = $.ajax({
               async : false,
               cache:false,
               data:  {
                   "params.selectDepentments":deptIds+",",
                   "params.taskId":taskId
               },
               type: 'POST',
               dataType : 'text',
               url: '${r'${ctx}'}/major/scplan/mPlan!isSelectDepartment.action',
               error: function (data) {
            	   
               },
               success:function(data){
            	   
               }
            }).responseText
            response = JSON.parse(response);
            /* if(response.code=="ParamError"){
            	W.$.dialog.alert("所选部门("+response.resultData+"),已经被其他部门挑选!");
            	return;
            }else  */if(response.code=="Error"){
            	W.$.dialog.alert(response.Msg);
            	return;
            }
            // 保存资源
            var responseText;
            responseText = $.ajax({
                url : '${r'${ctx}'}/major/scplan/mPlan!saveDeptRes.action',
                type : 'POST',
                async : false,
                data : getSaveParam(),
                success: function(data) {
                },
                error : function(data) {
                }
            }).responseText;
            return responseText;
        }
    }


    /**
     * 分发时调用
     * @returns {*}
     */
    MplanSp.saveReceiveDept=function(){

        if(_deptIds.length <= 0){
            W.$.dialog.alert('未选择分发部门', "warning");
            return false;
        }else{
            var responseText=getSaveParam();
            return responseText;
        }
    }
    /**
     * 问题督办挑选配合部门
     * @returns {*}
     */
    MplanSp.cooperate_dept=function(){
    	if(_deptIds.length <= 0){
            W.$.dialog.alert('未选择部门', "warning");
            return false;
        }else{
        	var data=[_deptIds,_deptNames];
        	return data;
        }
    }
    /**
     * 问题督办挑选牵头部门
     * @returns {*}
     */
    MplanSp.dealdepthid_dept=function(){
    	if(_deptIds.length <= 0){
            W.$.dialog.alert('未选择部门', "warning");
            return false;
        }else if(_deptIds.length > 1 ){
            W.$.dialog.alert('只能选择一个部门', "warning");
            return false;
        }else{
        	var data=[_deptIds,_deptNames];
        	return data;
        }
    }
})();
</script>
</html>
