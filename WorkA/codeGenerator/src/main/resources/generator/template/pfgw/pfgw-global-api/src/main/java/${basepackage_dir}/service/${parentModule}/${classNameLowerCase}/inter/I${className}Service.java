<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.service.${parentModule}.${classNameLowerCase}.inter;

import com.strongit.iss.common.ResultInfo;
import com.strongit.iss.dto.TreeVO;
import com.strongit.iss.dto.TreescVO;
import com.strongit.iss.entity.InvestProject;
import com.strongit.iss.entity.User;
import com.strongit.iss.entity.sc.ComPalnTask;
import com.strongit.iss.entity.sc.ComPalnTaskPub;
import com.strongit.iss.entity.sc.keypoint.ComPalnTaskIdea;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.hibernate.Page;
import com.strongit.iss.hibernate.QueryParameter;
import com.strongit.iss.service.BusinessService;

import java.util.List;
import java.util.Map;


/**
 <#include "/java_description.include">
 */
public interface I${className}Service extends BusinessService {

    /**
    　* @Description: 新增，修改
    　* @param
    　* @return  
    　* @throws
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')} 
    　*/
    ResultInfo save(ComPalnTask ComPalnTask) throws BusinessServiceException;

    /**
    　* @Description: 更新task状态
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')} 
    　*/
    //int updateTaskStatus(ComPalnTask ComPalnTask) throws BusinessServiceException;

    /**
    　* @Description: 更新taskpub状态
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    int updateTaskPubStatus(String tstatus,String id) throws BusinessServiceException;

    /**
    　* @Description: 查询
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')} 
    　*/
    ComPalnTask findByID(String id) throws BusinessServiceException;


    /**
    　* @Description: 查询
    　* @param
    　* @return 
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    Page<Map<String, Object>> getComPalnTaskPageByCondition(Map<String, String> params, QueryParameter queryParameter) throws BusinessServiceException;

    /**
    　* @Description: 获取分类，并组合为tree
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    List<TreescVO> getTypeTree(String deptid,String ttype,String taskId,Map<String, String> params)throws BusinessServiceException;

    /**
    　* @Description: 根据任务id查询
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    List<ComPalnTaskPub> getComPalnTaskPub(String taskid,int taskpubType)throws BusinessServiceException;


    /**
    　* @Description: 保存下发或者分发的数据。
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')} 
    　*/
    ResultInfo saveTaskToDept(List<String> list,String taskid,int tStatus,User user,String pubid) throws BusinessServiceException;
    /**
   　* @Description: 保存或者更新的任务数据。
   　* @author cedoo
   　* @date ${now?string('yyyy-MM-dd HH:mm')}  
   　*/
    ComPalnTask saveOrUpdateComPalnTask(ComPalnTask ComPalnTask,String ttype,User user);

    /**
    　* @Description: 保持任务下发表
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    ComPalnTaskPub saveOrUpdateComPalnTaskPub(String taskid,User user);
    /**
   　* @Description: 更新的任务数据。
   　* @author cedoo
   　* @date ${now?string('yyyy-MM-dd HH:mm')}  
   　*/
	ResultInfo updateTask(ComPalnTask ComPalnTask,User user);


    /**
    　* @Description: 撤回
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    ResultInfo resetCallTask(List<String> callMsgsList,Map<String, String> params) throws BusinessServiceException;

    /**
    　* @Description: 获取本级和辖区库的任务信息
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}  
    　*/
    Page<Map<String, Object>> getTaskProjectPage(Map<String, String> params, QueryParameter queryParameter,User user) throws BusinessServiceException;
    /**
     * @Description: 删除所选项目信息
     * @author cedoo
 	* @date ${now?string('yyyy-MM-dd HH:mm')}  
 	*/
	ResultInfo recallProject(List<String> projectIdsList,String ttype,User user) throws BusinessServiceException;

    /**
    　* @Description: 删除新任务
    　* @author cedoo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}
    　*/
    ResultInfo deleteTask(Map<String, String> params) throws BusinessServiceException;
    /**
   　* @Description: 保存意见
   　* @author cedoo
   　* @date ${now?string('yyyy-MM-dd HH:mm')} 
   　*/
	ResultInfo saveAdvice(Map<String, String> params, User loginUser,String ttype);

	void updateExclusiveLabel(String project_id, String label_status);
    /**
     * 加载历史选择
     * @throws Exception
     */
	List<TreeVO> getTaskNameSelection(Map<String, String> params) throws BusinessServiceException;
    /**
     * 验证部门是否被挑选
     * @throws Exception
     */
	ResultInfo isSelectDepartment(Map<String, String> params);
	/**
     * 获取已征求意见部门
     * @throws Exception
     */
	List<Map<String, Object>> getAdviceDept(Map<String, String> params,
			User user);
	/**
     * 提交到终点项目库
	 * @param projectIdsList 
     * @throws Exception
     */
	ResultInfo importToStore(Map<String, String> params, User user, List<String> projectIdsList);

	int updateTaskPubStatusStop(String tstatus, String id) throws BusinessServiceException;

	List<Map<String, Object>> getTaskYear(Map<String, String> params, User user);

	ResultInfo saveTransfer(Map<String, String> params, User loginUser, String ttype)throws Exception;

	ComPalnTaskIdea getAdviceContent(String taskID, String projectId, String deptId);

	ComPalnTaskIdea getAdviceContentById(String string);

	ResultInfo recallProject1(List<String> projectIdsList, String ttype, User user) throws BusinessServiceException;

	ResultInfo updateProject(InvestProject investProject, User user);

	InvestProject saveOrUpdateProject(InvestProject investProject, User user);

	InvestProject findProByID(String taskId);

	Page<Map<String, Object>> getProjectPageByCondition(Map<String, String> params, QueryParameter queryParameter);

	ResultInfo deleteProject(Map<String, String> params) throws BusinessServiceException;

	int updateProStatus(String tstatus, String id) throws BusinessServiceException;
	
}
