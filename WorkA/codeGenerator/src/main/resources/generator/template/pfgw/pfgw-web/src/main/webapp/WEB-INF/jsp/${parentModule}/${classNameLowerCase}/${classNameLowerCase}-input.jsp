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

<%--引入web uploader-0.1.5--%>
<script type="text/javascript"	src="${r'${ctx}'}/js/SCwebuploader-0.1.5/webuploader.js"></script>
<link rel="stylesheet" type="text/css" href="${r'${ctx}'}/js/SCwebuploader-0.1.5/webuploader.css">
<script type="text/javascript"	src="${r'${ctx}'}/js/SCwebuploader-0.1.5/SCNamespace.js"></script>
</head>


<!-- easyui-layout布局 -->
<body class="easyui-layout" fit="true" style="overflow: hidden;"> 
	<!-- 中间部分 列表 -->
    <div data-options="region:'center',split:true" style="overflow-x: hidden;overflow-y:auto;padding:10px">
       	<form id="inputTask_Form" method="post">
       		<table border="1" width="100%" style="line-height: 30px;">
				<tr>
					<td class="yTextRight" nowrap="nowrap" ><span class="red-star">*</span>通知名称</td>
        			<td class="yPd" colspan="3">
        				<input id="task_name" name="scPalnTask.taskName" type='text' class='easyui-textbox' data-options="validType:['maxLength[50]','sqlkey']"/>
        			</td>

            	</tr>
            	<tr>
					<td class="yTextRight" nowrap="nowrap" ><span class="red-star">*</span>通知年度</td>
					<td class="yPd" >
						<input type="text"
							   id="task_year"
							   name="scPalnTask.taskYear"
							   onclick="WdatePicker({dateFmt:'yyyy',minDate:new Date(),maxDate:'2026'})"
							   data-options="" class="Wdate easyui-validatebox"
							   readOnly="readonly" onChange="" />
					</td>
					<td class="yTextRight" nowrap="nowrap" ><span class="red-star">*</span>通知截止时间</td>
        			<td  class="yPd" colspan="3">
        				<input type="text"
								id="task_end_time"
								name="scPalnTask.endTIME" 
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:new Date(),maxDate:'2026-12-31'})"
								data-options="" class="Wdate easyui-validatebox"
								readOnly="readonly" onChange="" />
        			</td>
            		
            	</tr>
            	<tr>
            	<td class="yTextRight" nowrap="nowrap" >通知描述</td>
            		<td class="yPd" colspan="3" height="60px;">
        				<textarea id="task_desc" name="scPalnTask.taskDesc" type='' class='easyui-validatebox' data-options="validType:['maxLength[200]','sqlkey']" style='width:75%;' rows="6"></textarea>
        			</td>    
            	</tr>
            	
            	<!-- 
            	<tr>
        			<td class="yTextRight" nowrap="nowrap" ><span class="red-star">*</span>下发单位</td>
            		<td class="yPd" >
        				<input id="task_type" type='text' class='easyui-textbox' data-options=''/>
        			</td>
            	</tr> -->
            	
            	<tr>
            	<td class="yTextRight" nowrap="nowrap" >上传附件</td>
            		<td  class="yPd" colspan="3">
						<!-- 附件按钮 id名用附件名 -->
						<div class="btns">
							<div id="spinfo_syimg"></div>
						</div>
						<!-- 附件回显区 id用附件标识 -->
						<ul id="syTag" class="uploader-list"></ul>
						<div class="ul21progressH5 ul21progressNoH5" style="display: none;">
							<!-- 上传进度百分数 -->
							<span id="ul21progressNum" class="ul21progressH5 ul21progressNoH5" style="display: none;"></span>
							<!-- 进度条，支持H5标准则启用 -->
							<progress id="ul21progressing" class="ul21progressH5" style="display: none;" value="" max="100"></progress>
						</div>
        			</td>
            	</tr>
            </table>                   
        	<input type="hidden" id="task_id" name="scPalnTask.id"/>
        	<input type="hidden" id="version" name="scPalnTask.version"/>
        	<input type="hidden"   id="task_type_hide" name="scPalnTask.taskType"/>
        </form>
    </div>
</body>

<script type="text/javascript">
var operate = "${r'${operate}'}";//操作,新增还是修改
var taskId = "${r'${taskId}'}";//操作,新增还是修改
var ttype = "${r'${ttype}'}";//分类
var api = frameElement ? frameElement.api:null;
var W = api ? api.opener : window;
;(function(){

    // webuploader 首页图
    var spinfosyimg = SCTaskWebUploader("spinfo_syimg",taskId,"taskID","important");
    // 上传中
    spinfosyimg.on('uploadProgress', function(file, percentage) {
        var progressNum = EnSp.progressNumberFormatter(percentage);
        // 判断当前浏览器是否支持HTML5，若支持则显示进度条
        if(EnSp.checkhHtml5Support()){
			${r'$(".ul21progressH5")'}.css("display","block");
            ${r'$("#ul21progressing")'}.val(progressNum);
			${r'$("#ul21progressNum")'}.text(progressNum + "%");

            if(percentage >= 1){
                ${r'$(".ul21progressH5")'}.css("display","none");
            }
            // 不支持H5标准时，则只显示当前进度百分数
        }else{
			${r'$(".ul21progressNoH5")'}.css("display","block");
            ${r'$("#ul21progressNum")'}.text(progressNum + "%");

            if(percentage >= 1){
				${r'$(".ul21progressNoH5")'}.css("display","none");
            }
        }
    });
    //上传成功
    spinfosyimg.on('uploadSuccess', function(file, response) {
        var fileName = file.name;
        var str = response._raw;
        var fileId = "";
        if(str){
            fileId = str.split("⊙")[1];
            ${r'$("#syTag")'}.append("<li id = file"+fileId+"><input hidden='hidden' name='fileIds' value="+fileId+"><a style='color: blue' href='javascript:downloadAttachment(\""+fileId+"\")' title='"+fileName+"'>"+fileName+"</a><a class='modifyAtt' href='javascript:void(0)' onclick='scdeleteAttachment(\""+fileId+"\")'><img src='"+ctx+"/EasyUI/themes/icons/cancel.png'></a><span>&nbsp;&nbsp;&nbsp;&nbsp;上传成功！</span></li>");
        }
    });
    // 上传失败
    spinfosyimg.on('uploadError', function(file, json) {
		${r'$("#syTag")'}.append("<li>"
            +	"<span>上传失败！</span>"
            +"</li>"
        );
    });
    // 文件验证失败
    spinfosyimg.on('error', function(type) {
        // 格式验证失败
        if (type == "Q_TYPE_DENIED") {
            W.$.dialog.alert("请上传doc,docx,xls,xlsx,pdf,ppt,pptx,jpg,jpeg,zip,rar,png格式文件！","error");
            // 文件大小验证失败
        } else if ("Q_EXCEED_SIZE_LIMIT" == type || "F_EXCEED_SIZE" == type) {
            W.$.dialog.alert("文件大小不能超过50M！","error");
            // 文件个数失败
        }else if("Q_EXCEED_NUM_LIMIT" == type){
        	W.$.dialog.alert("最多上传5个文件！","error");
        }else {
            W.$.dialog.alert("上传出错！请检查后重新上传！错误代码 ---> " + type,"error");
        }
    });
	//文件超过5个就禁止上传
    spinfosyimg.on('beforeFileQueued', function (file) {
    	var fileNum = $("#syTag li").length;
    	if(fileNum >= 5){
    		W.$.dialog.alert("最多上传5个文件！","error");
    		return false;
    	}else{
    		return true;
    	}
    });
	/**
     * 加入通知分类
     */
	/* $("#task_type").combotree({
		url : '${r'${ctx}'}/major/scplan/commonPlan!getDictionaryInfo.action?dictCode=STORETYPE',
		multiple : false,
		cascadeCheck : false,
		onSelect : function(node) {
			//返回树对象
			var tree = $(this).tree;
			//选中的节点是否为叶子节点,如果不是叶子节点,清除选中
			var isLeaf = tree('isLeaf', node.target);
			if (!isLeaf) {
				W.$.dialog.alert("请选择最末一级节点");
					return false;
			}
			$("#task_type_hide").val(node.id);				
		}
	}); */

	/**
	 * 如果为修改，则进入页面后加载表单
	 */
    if(operate=="edit" || operate=="look" ){
        $.ajax({
            async : false,
            cache : false,
            //data : $('#inputTask_Form').serialize(),
            type : 'POST',
            dataType : 'text',
            url : '${r'${ctx}'}/major/scplan/mPlan!getTaskInfo.action?operate='+operate+'&taskId='+taskId,
            success : function(data) {
                data = $.parseJSON(data);
                $("#task_name").attr('value',data.taskName);
                $("#task_year").attr('value',data.taskYear);
                /* $("#task_type").combotree('setValue',data.taskType);
                $("#task_type_hide").val(data.taskType); */
                $("#task_desc").val(data.taskDesc);
                $("#task_start_time").attr('value',formatDate(data.startTIME,"yyyy-MM-dd"));
                $("#task_end_time").attr('value',formatDate(data.endTIME,"yyyy-MM-dd"));
                $("#task_pub_time").attr('value',formatDate(data.publicStartTIME,"yyyy-MM-dd"));
                $("#task_id").attr('value',data.id);
                $("#version").attr('value',data.version);

                var tag="taskID";
                loadTaskFile(taskId,tag);
            },
            error : function(data) {//请求失败处理函数
                message = eval("(" + data + ")");
                $.dialog.alert(message, "error");
            }
        });
    }

    /**
	 * 加载附件
	 */
    function loadTaskFile(taskId,taskID){
        $.ajax({
            async : false,
            cache : false,
            type : 'GET',
            dataType : 'text',
            url : '${r'${ctx}'}/major/scplan/mPlan!getAttachmentsByProId?params.projectId='+ taskId,
            error : function () {//请求失败处理函数
                $.dialog.alert('编辑失败！',"error");
            },
            success : function(data){
                var data = $.parseJSON(data);
                for(var i=0;i<data.fileList.length;i++){
                    //
                    if(data.fileList[i].secondRelateBillId == "taskID"){
                        //附件id
                        var id = data.fileList[i].id;
                        $("#syTag").append("<li id=file"+id+"><a style='color: blue' title='点击可下载' href='javascript:downloadAttachment(\""+id+"\")'>"+data.fileList[i].name+"</a><a class='modifyAtt editAtt' href='javascript:void(0)' onclick='scdeleteAttachment(\""+id+"\")'><img src='${r'${ctx}'}/EasyUI/themes/icons/cancel.png'></a></li>");
                    }
				}
			}
		})
	}
	
	/**
    * 保存通知信息
    */
	MplanSp.saveTaskInfo = function() {
		var message = "";
		//通知分布时间大于等于通知开始时间大于通知结束时间验证
		var taskPubTime = $("#task_pub_time").val();
		var taskStartTime = $("#task_start_time").val();
		var taskEndTime = $("#task_end_time").val();
		var task_name = $("#task_name").val();
		var task_year = $("#task_year").val();
		var task_desc = $("#task_desc").val();
		/* var task_type = $("#task_type_hide").val(); */

		if(task_name == "" || task_name == null || task_name == undefined){
           	//$.dialog.alert('请填写通知名称', "warning");
            message = '{"code":"error" , "msg":"请填写通知名称!"}';
            return message;
        }
        
        if(task_year == "" || task_year == null || task_year == undefined){
            //$.dialog.alert('请填写通知年度', "warning");
            message = '{"code":"error" , "msg":"请填写通知年度!"}';
            return message;
        }
        if(taskEndTime == "" || taskEndTime == null || taskEndTime == undefined){
            //$.dialog.alert('请填写通知年度', "warning");
            message = '{"code":"error" , "msg":"请填写通知截止时间!"}';
            return message;
        }
        if(task_desc.length>200){
        	message = '{"code":"error" , "msg":"通知描述不能大于200字!"}';
            return message;
        }
       /* if(task_type == "" || task_type == null || task_type == undefined){
        	message = '{"code":"error" , "msg":"请填写通知类型!"}';
            //$.dialog.alert('请填写通知类型', "warning");
            return message;
        } */ 

        // if(taskStartTime !="" && taskEndTime!=""){
        //     if (taskStartTime > taskEndTime) {
        //         message = '{"code":"error" , "msg":"通知开始时间不能小于结束时间!"}';
        //         return message;
        //     }
        // }
		taskStatus=0;
        var formData=$('#inputTask_Form').serialize();
        var responseText;
        responseText = $.ajax({
			async : false,
			cache : false,
			data : $('#inputTask_Form').serialize(),
			type : 'POST',
			dataType : 'text',
			url : '${r'${ctx}'}/major/scplan/mPlan!saveOrUpdate.action?taskStatus='+taskStatus+'&ttype='+ttype+'&operate='+operate,
			error : function(data) {

			},
			success : function(data) {
			}
		}).responseText;
        return responseText;
	};

})();
</script>
</html>
