<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.action.${parentModule}.${classNameLowerCase};


import com.strongit.iss.action.BaseActionSupport;
import com.strongit.iss.common.*;
import com.strongit.iss.entity.User;
import com.strongit.iss.entity.sc.ComPalnTask;
import com.strongit.iss.entity.sc.ComPalnTaskPub;
import ${basepackage}.service.${parentModule}.${classNameLowerCase}.inter.I${className}Service;
import org.apache.struts2.config.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.*;

/**
 <#include "/java_description.include">
 */
@Controller
@Scope("prototype")
@ParentPackage("default")
public class ${className}Action extends BaseActionSupport<Object> {

    @Resource
    private I${className}Service ${classNameLowerCase}Service;
    
    protected int rows = 20;
    protected int page = 1;

    //分类（<1重点、2三年、3年度>）
    private String ttype;

    /** 创建集合，获取前台的查询条件 */
    private Map<String, String> params;

    private String taskStatus;

	//任务ID
    private String taskId;

    //任务发布表id
    private String pubid;

    //选择的部门id集合
    private String deptIds;
    //选择的类型
    private String type;
    //类型，分发还是下发
    private String sendType;
    //操作，新增还是修改
    private String operate;

    private String callMsgs;

    //当前登录人部门id
    private String localDeptid;

    /**
     * 附件id
     */
    private String fileIds;

	/**
     * 列表
     */
	@Override
    public String list() throws Exception{
        /**
         * 当前登录人信息
         */
        User user = getCurrentUser();
        String deptid=user.getDeptId();
        getRequest().setAttribute("localDeptid", deptid);
        return "list";
    }

    /**
     * 编辑/新增 
     * @return
     * @throws Exception
     */
    @Override
    public String input() throws Exception{  	
    	getRequest().setAttribute("operate", operate);
    	getRequest().setAttribute("taskId", taskId);
        return "input";
    }

    /**
     * 新增/编辑任务
     * 1、新增
     * -第一步：保存任务表
     * -第二步：保存任务下发包，设置当前创建人信息到下发表
     * @throws Exception
     */
	public void saveOrUpdate() throws Exception{
    	User user = getCurrentUser();
    	ResultInfo info = new ResultInfo(ResultCodeType.Success,"处理成功");
    	try{
            if(scPalnTask.getTaskName().equals("")){
                info = new ResultInfo(ResultCodeType.Error,"任务名称不能为空");
                Struts2Utils.renderText(info);
                return ;
            }

            //修改
            if(operate.equals("edit")){
                info = ${classNameLowerCase}Service.updateTask(scPalnTask,user);

                //保存附件
                //ResultInfo res=commonPlanService.saveAttachment(scPalnTask.getId(),fileIds,SCMajorConstants.ATT_TYPE_TASK);
            }else{
                ComPalnTask _scPalnTask = ${classNameLowerCase}Service.saveOrUpdateComPalnTask(scPalnTask,ttype,user);
                String taskid=_scPalnTask.getId();

                ComPalnTaskPub scPalnTaskPub = ${classNameLowerCase}Service.saveOrUpdateComPalnTaskPub(taskid,user);

                //保存附件
                commonPlanService.saveAttachment(taskid,fileIds,GDMajorConstants.ATT_TYPE_TASK);
            }
        }catch (Exception e){
    	    e.printStackTrace();
            info.setCode(ResultCodeType.Error);
            info.setMsg(e.getMessage());
        }finally {
            Struts2Utils.renderText(info);
        }
    }


    /**
     　* @Description: 删除任务
     　* @author cedoo
     　* @date  ${now?string('yyyy-MM-dd HH:mm')}
     　*/
    public void deleteRecord() throws Exception{
            ResultInfo resultInfo = ${classNameLowerCase}Service.deleteTask(params);
            Struts2Utils.renderText(resultInfo);
    }

    public String getTtype() {
        return ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
    

}
