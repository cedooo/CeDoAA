<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.service.${parentModule}.${classNameLowerCase}.impl;

import antlr.StringUtils;

import com.google.common.collect.Maps;
import com.strongit.iss.common.DaoContextDaMeng;
import com.strongit.iss.common.ResultCodeType;
import com.strongit.iss.common.ResultInfo;
import com.strongit.iss.common.GDMajorConstants;
import com.strongit.iss.commons.MajorConstants;
import com.strongit.iss.dao.major.scplan.inter.IMPlanDao;
import com.strongit.iss.dao.major.scplan.inter.IScPlanTaskIdeaDao;
import com.strongit.iss.dao.major.store.inter.IStoreProjectProgressDAO;
import com.strongit.iss.dto.TreeVO;
import com.strongit.iss.dto.TreescVO;
import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.DictionaryItems;
import com.strongit.iss.entity.InvestProject;
import com.strongit.iss.entity.User;
import com.strongit.iss.entity.sc.ComPalnTask;
import com.strongit.iss.entity.sc.ComPalnTaskPub;
import com.strongit.iss.entity.sc.keypoint.ComPalnTaskIdea;
import com.strongit.iss.exception.BusinessServiceException;
import com.strongit.iss.hibernate.Page;
import com.strongit.iss.hibernate.QueryParameter;
import com.strongit.iss.service.auth.DepartmentService;
import com.strongit.iss.service.auth.DictionaryService;
import com.strongit.iss.service.impl.BaseService;
import com.strongit.iss.service.major.scplan.inter.IMPlanService;
import com.strongit.iss.service.major.scplan.inter.IMajorProjectLogService;
import com.strongit.iss.service.major.scplan.inter.ITaskProjectService;
import com.strongit.iss.service.major.scyear.inter.IYearProjectLogService;
import com.strongit.iss.utils.MajorUtil;
import com.strongit.iss.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 <#include "/java_description.include">
 */
@Service
public class ${className}ServiceImpl extends BaseService implements I${className}Service {


	@Resource
    protected DaoContextDaMeng dao;
	@Resource
	private IScPlanTaskIdeaDao scPlanTaskIdeaDao;
    @Resource
    private I${className}Dao ${classNameFirstLower}Dao;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private DepartmentService departmentService;

    @Resource
    private ITaskProjectService taskProjectService;
    
	@Resource
	private IMajorProjectLogService majorProjectLogService;
	
	@Resource
	private IYearProjectLogService yearProjectLogService;
	
	@Resource
    private IStoreProjectProgressDAO storeProjectProgressDAO;
	

    @Override
    public ResultInfo save(ComPalnTask scPalnTask) throws BusinessServiceException {
        ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
        try {
            this.dao.update(scPalnTask);
        }catch (Exception e){
            result.setCode(ResultCodeType.Warning);
            result.setMsg(e.getMessage());
            e.printStackTrace();
            throw new BusinessServiceException("更新失败", e);
        }
        return result;
    }

//    @Override
//    public int updateTaskStatus(ComPalnTask scPalnTask) throws BusinessServiceException {
//        String sql="UPDATE com_paln_task set task_status = ? WHERE id = ? ";
//        int num= this.dao.executeSql(sql,scPalnTask.getTaskStatus(),scPalnTask.getId());
//        return num;
//    }

    @Override
    public int updateTaskPubStatus(String tstatus,String id) throws BusinessServiceException {
        //String sqlpub="UPDATE com_paln_task_pub set t_status = ? WHERE task_id = ? AND dept_id = ? AND taskpub_type = 1 ";
        String sqlpub="UPDATE com_paln_task_pub set t_status = ? WHERE id = ?  ";
        int _num= this.dao.executeSql(sqlpub,tstatus,id);
        return _num;
    }
    @Override
    public int updateTaskPubStatusStop(String tstatus,String id) throws BusinessServiceException {
        //String sqlpub="UPDATE com_paln_task_pub set t_status = ? WHERE task_id = ? AND dept_id = ? AND taskpub_type = 1 ";
        String sqlpub="UPDATE com_paln_task_pub set t_status = ? WHERE task_id = ?  ";
        int _num= this.dao.executeSql(sqlpub,tstatus,id);
        return _num;
    }
    @Override
    public int updateProStatus(String tstatus,String id) throws BusinessServiceException {
        //String sqlpub="UPDATE com_paln_task_pub set t_status = ? WHERE task_id = ? AND dept_id = ? AND taskpub_type = 1 ";
    	Map<String, Object> values = new HashMap<String, Object>();
		String[] ID=id.split(",");
		values.put("ids", ID);
		values.put("tstatus", tstatus);
    	String sqlpub="UPDATE invet_project set check_status = :tstatus WHERE id in (:ids)  ";
        int _num= this.dao.executeSql(sqlpub,values);
        return _num;
    }
    @Override
    public ComPalnTask findByID(String id) throws BusinessServiceException {
        ComPalnTask scPalnTask=null;
        try{
            scPalnTask=mPlanDao.findByID(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return scPalnTask;
    }
    /**
     * liangchang
     */
    @Override
    public Page<Map<String, Object>> getComPalnTaskPageByCondition(Map<String, String> params, QueryParameter queryParameter) throws BusinessServiceException {
        Page<Map<String, Object>> page=null;
        try{
            page= mPlanDao.getScPalnTaskPageByCondition(params,queryParameter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }
    @Override
    public Page<Map<String, Object>> getProjectPageByCondition(Map<String, String> params, QueryParameter queryParameter) throws BusinessServiceException {
        Page<Map<String, Object>> page=null;
        try{
            page= mPlanDao.getProjectPageByCondition(params,queryParameter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }
    @Override
    public List<TreescVO> getTypeTree(String deptid,String ttype,String taskId,Map<String, String> params) throws BusinessServiceException {

        List<TreescVO> treeVoList = new ArrayList<TreescVO>();

        List<DictionaryItems> dictionaryItemsList = dictionaryService.getDictItemsByGroupKey(GDMajorConstants.DIC_KEY);

        for (DictionaryItems dictionaryItems :dictionaryItemsList){
            TreescVO vo = new TreescVO();
            vo.setName(dictionaryItems.getItemFullValue());
            vo.setId(dictionaryItems.getItemGuid());
            vo.setpId("-1");
            vo.setType("task");
            vo.setOpen("true");
            vo.setContent(dictionaryItems.getItemKey());//类型
            treeVoList.add(vo);

            //查询任务表。
            //name上加(锁)
            //根据登录人部门，去下发任务表中查询自己创建和下发的分类。

            try{
                List<Map<String, Object>> list=mPlanDao.getTypeTaskList(dictionaryItems.getItemKey(),deptid,ttype,taskId,params);
                for (Map<String, Object> map :list){
                    String task_name=map.get("task_name").toString();
                    String task_id=map.get("task_id").toString();
                    String lock_status=map.get("lock_status").toString();
                    String pub_fid=map.get("pub_fid").toString();//
                    String id=map.get("id").toString();//

                    if(lock_status.equals("1")){
                        task_name =task_name+"(锁)";
                    }

                    TreescVO svo = new TreescVO();
                    svo.setName(task_name);
                    svo.setId(task_id);
                    svo.setpId(dictionaryItems.getItemGuid());
                    svo.setType("subtask");
                    svo.setOpen("false");
                    svo.setContent(lock_status);//（锁定/未锁定）
                    svo.setPubfid(pub_fid);//任务发布表父id
                    svo.setPubid(id);//任务发布表id
                    treeVoList.add(svo);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return treeVoList;
    }

    @Override
    public List<ComPalnTaskPub> getComPalnTaskPub(String taskid,int taskpubType) throws BusinessServiceException {
        List<ComPalnTaskPub> result = null;
        String hql = "from ComPalnTaskPub where taskId = ?  AND  taskpubType = ? ";
        result=dao.find(hql,taskid,taskpubType);

        return result;
    }

    @Override
    @Transactional
    public ResultInfo saveTaskToDept(List<String> list,String taskid,int tStatus,User user,String pubid) throws BusinessServiceException {
        ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
        try {
            if(list == null){
                return result = new ResultInfo(ResultCodeType.Error ,"数据验证错误:没有选择部门.");
            }
            if(StringUtil.isAllNullOrEmpty(taskid)){
                return result = new ResultInfo(ResultCodeType.Error, "数据验证错误:任务ID为空.");
            }
            if(tStatus == 0){
                return result = new ResultInfo(ResultCodeType.Error, "数据验证错误:处理类型错误(下发/分发).");
            }
            System.out.println(list.get(0));
            System.out.println(list.size());
            System.out.println(list);
            if(list!=null&&list.size()!=0&&!"".equals(list.get(0).trim())){
                String deptid=user.getDeptId();
                //当前登录人的部门
                Department department=departmentService.getById(deptid);

                int taskpubType=0; //类型（创建=1 、接收=2 、分发=3）
                if(tStatus == 1){//下发按钮
                    taskpubType =2;
                }

                if(tStatus == 2){//分发发按钮
                    taskpubType=3;
                }

                for (String str:list){
                    if(deptid.equals(str)==false){
                        ComPalnTaskPub scPalnTaskPub=new ComPalnTaskPub();

                        scPalnTaskPub.setTaskId(taskid);

                        //分发-下发，的上级任务发布表id
                        scPalnTaskPub.setPubFid(pubid);
                        
                        Department dept=departmentService.getById(str);

                        scPalnTaskPub.setDeptId(str);
                        scPalnTaskPub.setTaskpubType(taskpubType);//类型（创建=1 、接收=2 、分发=3）
/*                      scPalnTaskPub.setFgwcode(dept.getFgwcode());
                        scPalnTaskPub.setFgwcscode(dept.getFgwcscode());*/
                        scPalnTaskPub.setDepAreaId(dept.getDepartmentAreaId());//设置部门区域编码
                        
                        scPalnTaskPub.setTaskpubStatus(0);//新增都属于 0=当前
                        scPalnTaskPub.setLockStatus(0);//（0未锁定 、 1锁定）
                        scPalnTaskPub.settStatus(0);//对于下发的接受单位，无论是下发的还是分发的，都属于新任务=0

                        scPalnTaskPub.setCreateUserId(user.getEmployeeGuid());
                        scPalnTaskPub.setCreateUserName(user.getEmployeeFullname());
                        scPalnTaskPub.setCreateTime(new Date());
                        scPalnTaskPub.setUpdateUserId(user.getEmployeeGuid());
                        scPalnTaskPub.setUpdateUserName(user.getEmployeeFullname());
                        scPalnTaskPub.setUpdateTime(new Date());
                        scPalnTaskPub.setVersion(1);//新增时版本号为1
                        scPalnTaskPub.setIsReceive(0);

                        this.dao.saveOrUpdate(scPalnTaskPub);
                    }else{
                        //下发或者分发，不保存自己
                    }
                }
            }else {
            	
            	List<Department> ds = departmentService.getAllCs2();
            	String deptid=user.getDeptId();
                //当前登录人的部门
                Department department=departmentService.getById(deptid);

                int taskpubType=0; //类型（创建=1 、接收=2 、分发=3）
                if(tStatus == 1){//下发按钮
                    taskpubType =2;
                }

                if(tStatus == 2){//分发发按钮
                    taskpubType=3;
                }

                for (Department de:ds){
                    if(deptid.equals(de.getDepartmentGuid())==false){
                        ComPalnTaskPub scPalnTaskPub=new ComPalnTaskPub();

                        scPalnTaskPub.setTaskId(taskid);

                        //分发-下发，的上级任务发布表id
                        scPalnTaskPub.setPubFid(pubid);
                        
                        Department dept=departmentService.getById(de.getDepartmentGuid());

                        scPalnTaskPub.setDeptId(de.getDepartmentGuid());
                        scPalnTaskPub.setTaskpubType(taskpubType);//类型（创建=1 、接收=2 、分发=3）
/*                      scPalnTaskPub.setFgwcode(dept.getFgwcode());
                        scPalnTaskPub.setFgwcscode(dept.getFgwcscode());*/
                        scPalnTaskPub.setDepAreaId(dept.getDepartmentAreaId());//设置部门区域编码
                        
                        scPalnTaskPub.setTaskpubStatus(0);//新增都属于 0=当前
                        scPalnTaskPub.setLockStatus(0);//（0未锁定 、 1锁定）
                        scPalnTaskPub.settStatus(0);//对于下发的接受单位，无论是下发的还是分发的，都属于新任务=0

                        scPalnTaskPub.setCreateUserId(user.getEmployeeGuid());
                        scPalnTaskPub.setCreateUserName(user.getEmployeeFullname());
                        scPalnTaskPub.setCreateTime(new Date());
                        scPalnTaskPub.setUpdateUserId(user.getEmployeeGuid());
                        scPalnTaskPub.setUpdateUserName(user.getEmployeeFullname());
                        scPalnTaskPub.setUpdateTime(new Date());
                        scPalnTaskPub.setVersion(1);//新增时版本号为1
                        scPalnTaskPub.setIsReceive(0);

                        this.dao.saveOrUpdate(scPalnTaskPub);
                    }else{
                        //下发或者分发，不保存自己
                    }
			}
           }
        }catch (Exception e){
            result.setCode(ResultCodeType.Error);
            result.setMsg(e.getMessage());
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
        }
        return result;
    }

    
    /**liangchang
     * 保存计划任务
     */
	@Override
    @Transactional
	public ComPalnTask saveOrUpdateComPalnTask(ComPalnTask scPalnTask,String ttype,User user) throws BusinessServiceException {
		try {
			//scPalnTask.setTaskStatus(0);//新建
			scPalnTask.setTtype(Integer.parseInt(ttype));
	    	scPalnTask.setCreateDeptId(user.getDeptId());
	    	scPalnTask.setCreateDeptName(user.getDeptName());
	    	scPalnTask.setCreateUserId(user.getEmployeeGuid());
	    	scPalnTask.setCreateUserName(user.getEmployeeFullname());
	    	scPalnTask.setCreateTime(new Date());
	    	scPalnTask.setUpdateUserId(user.getEmployeeGuid());
	    	scPalnTask.setUpdateUserName(user.getEmployeeFullname());
	    	scPalnTask.setUpdateTime(new Date());
	    	scPalnTask.setVersion(1);//新增时版本号为1
	    	
	    	scPalnTask.setStoreLevel(user.getDepartment().getStoreLevel());//任务层级 -- 伍红林
            //任务发布时间，和任务开始时间去掉
            scPalnTask.setPublicStartTIME(new Date());
            scPalnTask.setStartTIME(new Date());
  
			//保存任务计划
			this.dao.save(scPalnTask);
		}catch (Exception e){
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
        }
		return scPalnTask;
	}
    /**
     * liangchang
     */
    @Override
    @Transactional
    public ComPalnTaskPub saveOrUpdateComPalnTaskPub(String taskid, User user) {
        ComPalnTaskPub scPalnTaskPub = new ComPalnTaskPub();
	    try {
            Department department=departmentService.getById(user.getDeptId());
            scPalnTaskPub.setTaskId(taskid);
            scPalnTaskPub.setDeptId(user.getDeptId());
            scPalnTaskPub.setPubFid("-1");//新增时，设置为-1
/*            scPalnTaskPub.setFgwcode(department.getFgwcode());
            scPalnTaskPub.setFgwcscode(department.getFgwcscode());*/
            scPalnTaskPub.setDepAreaId(department.getDepartmentAreaId());//设置部门编码ID
            scPalnTaskPub.setTaskpubType(1);
            scPalnTaskPub.setTaskpubStatus(0);//新增都属于 0=当前
            scPalnTaskPub.setLockStatus(0);//（0未锁定 、 1锁定）
            scPalnTaskPub.settStatus(-1);//表示创建
            scPalnTaskPub.setCreateUserId(user.getEmployeeGuid());
            scPalnTaskPub.setCreateUserName(user.getEmployeeFullname());
            scPalnTaskPub.setCreateTime(new Date());
            scPalnTaskPub.setUpdateUserId(user.getEmployeeGuid());
            scPalnTaskPub.setUpdateUserName(user.getEmployeeFullname());
            scPalnTaskPub.setUpdateTime(new Date());
            scPalnTaskPub.setVersion(1);//新增时版本号为1

            this.dao.save(scPalnTaskPub);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
        }
        return scPalnTaskPub;
    }
	
    /**lianchang
     * 更新计划任务
     */
    @Override
    @Transactional
	public ResultInfo updateTask(ComPalnTask scPalnTask,User user) {
		ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		try {
            ComPalnTask _upscPalnTask =findByID(scPalnTask.getId());

            _upscPalnTask.setVersion(_upscPalnTask.getVersion()+1);
            _upscPalnTask.setTaskName(scPalnTask.getTaskName());
            _upscPalnTask.setTaskYear(scPalnTask.getTaskYear());
           // _upscPalnTask.setTaskType(scPalnTask.getTaskType());    //伍红林
            _upscPalnTask.setTaskDesc(scPalnTask.getTaskDesc());
            _upscPalnTask.setPublicStartTIME(scPalnTask.getPublicStartTIME());
            _upscPalnTask.setStartTIME(scPalnTask.getStartTIME());
            _upscPalnTask.setEndTIME(scPalnTask.getEndTIME());
            _upscPalnTask.setUpdateUserId(user.getEmployeeGuid());
            _upscPalnTask.setUpdateUserName(user.getEmployeeFullname());
            _upscPalnTask.setUpdateTime(new Date());

            //任务发布时间，和任务开始时间去掉
            _upscPalnTask.setPublicStartTIME(new Date());
            _upscPalnTask.setStartTIME(new Date());

			this.dao.saveOrUpdate(_upscPalnTask);
		}catch (Exception e){
            result.setCode(ResultCodeType.Error);
            result.setMsg(e.getMessage());
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
        }
		return result;
	}
    /**
     * liangchang
     */
    @Override
    @Transactional
    public ResultInfo resetCallTask(List<String> callMsgsList,Map<String, String> params) throws BusinessServiceException {
        ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
        if(callMsgsList != null){
            try{
                /**
                 * 对下发/任务进行撤回操作：
                 *
                 * 1-更新当前任务，在下发任务表中的状态为撤回id=pubid
                 * 2-删除除开自己的下发任务表的数据，
                 *      tStatus=1（下发）删除除开自己的下发任务[pub_fid=-1]
                 *      tStatus=2（分发）删除除自己任务的 子任务
                 * 3-删除下发项目表中的数据
                 * 4-删除实际库中的数据，根据ttype来获取表
                 */

                String ttype=params.get("ttype");
                if(StringUtil.isAllNullOrEmpty(ttype)){
                    return new ResultInfo(ResultCodeType.Error, "模块标识(ttype)为空.");
                }

                for(int i=0;i< callMsgsList.size();i++){
                    String[] callMsgs=callMsgsList.get(i).split("#");//taskid#pubFid#tStatus#pubid
                    String taskid=callMsgs[0];
                    String pubFid=callMsgs[1];
                    String tStatus=callMsgs[2];
                    String pubid=callMsgs[3];

                    if(StringUtil.isAllNullOrEmpty(taskid)){
                        return new ResultInfo(ResultCodeType.Error, "任务ID(com_paln_task :id)为空.");
                    }
                    if(StringUtil.isAllNullOrEmpty(pubFid)){
                        return new ResultInfo(ResultCodeType.Error, "下发任务父ID(com_paln_task_pub :pub_fid)为空.");
                    }
                    if(StringUtil.isAllNullOrEmpty(tStatus)){
                        return new ResultInfo(ResultCodeType.Error, "任务状态(com_paln_task_pub :t_status)为空.");
                    }

                    if(StringUtil.isAllNullOrEmpty(pubid)){
                        return new ResultInfo(ResultCodeType.Error, "下发任务ID(com_paln_task_pub :id)为空.");
                    }


                    //1-更新当前任务表中的状态为撤回 id=pubid
                    StringBuilder SQL = new StringBuilder();
                    SQL.append("UPDATE com_paln_task_pub SET t_status = 4 WHERE id = '"+pubid+"' ");
                    this.dao.executeSql(SQL.toString());

                    //2-1  tStatus=1（下发）删除除开自己的下发任务[pub_fid=-1]
                    if(tStatus.equals("6") && pubFid.equals("-1")){
                        StringBuilder SQL_ = new StringBuilder();
                        SQL_.append("DELETE from com_paln_task_pub WHERE task_id='"+taskid+"' AND taskpub_type != 1 ");
                        this.dao.executeSql(SQL_.toString());
                    }

                    //2-2 tStatus=2（分发）删除除自己任务的 子任务
                    if(tStatus.equals("2") && pubFid.equals("-1")==false){
                            StringBuilder SQL_ = new StringBuilder();
                            SQL_.append("DELETE from com_paln_task_pub WHERE pub_fid='"+pubid+"' ");
                            this.dao.executeSql(SQL_.toString());
                    }


                    String tableName=MajorUtil.getTableNameByttype(ttype);
                    if(!StringUtil.isAllNullOrEmpty(tableName)){
                        StringBuilder SQL_Q = new StringBuilder();
                        SQL_Q.append("SELECT project_id FROM com_paln_task_project  WHERE task_id = '"+taskid+"' ");
                        if(tStatus.equals("2")){
                            //SQL_Q.append(" AND pubtask_id = '"+pubid+"'");
                            SQL_Q.append(" AND pubtask_id IN (SELECT task_id FROM com_paln_task WHERE pub_fid =' "+pubid+" ') ");
                        }


                        //3-删除下发项目表中的数据
                        StringBuilder SQL_project = new StringBuilder();
                        SQL_project.append("update "+tableName+" set c_keypoint = 0,k_status = null,k_receive_dep_id = null WHERE id in (" +SQL_Q.toString()+ ")");
                        this.dao.executeSql(SQL_project.toString());

                        //4-删除实际库中的数据
                        StringBuilder SQL_pubtask = new StringBuilder();
                        SQL_pubtask.append("DELETE com_paln_task_project WHERE task_id = '"+taskid+"'");
                        if(tStatus.equals("2")){
                            //SQL_pubtask.append(" AND pubtask_id = '"+pubid+"'");
                            SQL_pubtask.append(" AND pubtask_id IN (SELECT task_id FROM com_paln_task WHERE pub_fid =' "+pubid+" ') ");
                        }

                        this.dao.executeSql(SQL_pubtask.toString());
                    }
                }
            }catch (Exception e){
                result.setCode(ResultCodeType.Error);
                result.setMsg(e.getMessage());
                e.printStackTrace();
                throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
            }
        }
        return result;
    }

    @Override
    public Page<Map<String, Object>> getTaskProjectPage(Map<String, String> params, QueryParameter queryParameter,User user) throws BusinessServiceException {
        Page<Map<String, Object>> page=null;
        try{
            Department department=null;
            String bitDeptID = params.get("bitDeptID") ;
            if (!StringUtil.isAllNullOrEmpty(bitDeptID)) {
                department=departmentService.getById(bitDeptID);//获取当前点击的部门ID
            }else{
                department=departmentService.getById(user.getDeptId());//当前登录人部门信息
            }

            if(department == null){
                throw new BusinessServiceException("部门信息获取有误，请检查。");
            }

            page =mPlanDao.getTaskProjectPage(params,queryParameter,department);

            /**
             * 根据任务id查询出任务的项目个数。
             */
            List<Map<String,Object>> resultList = page.getResult();
            //个数和金额统计
            resultList=taskProjectService.getTaskProjectNumAndTotal(user,resultList,"1",params.get("ttype"),null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }
    /**
     * liangchang
     */
	@Override
	@Transactional
	public ResultInfo recallProject(List<String> projectIdsList,String ttype,User user) throws BusinessServiceException {
		//执行结果
		 ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		 String tablename = MajorUtil.getTableNameByttype(ttype);
		 if(projectIdsList != null){
	            try{
	                for(int i=0;i< projectIdsList.size();i++){             	
	                	String projectId = projectIdsList.get(i);
	                	String SQL = "";
	                	if(ttype.equals(GDMajorConstants.ttype_1)){
	                		//获取上一级部门
	                		SQL="select cur_department_guid from s_to_deal_log where (type = 3 or type = 5) and (next_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' or next_department_guid is null) and relate_bill_id = '"+projectId+"' order by create_time desc";
	                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
	                		SQL="select cur_department_guid from s_year_to_deal_log where (type = 3 or type = 5) and (next_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' or next_department_guid  is null) and relate_bill_id = '"+projectId+"' order by create_time desc";
		                }
	                	List<Map<String,Object>> list = this.dao.findBySql(SQL.toString());
	                	//查找项目的创建单位
	                	String findCreateDeptSql = "select k_create_department_guid from "+tablename+" where id  = ?";
	                	String createDepartmentGuid="";
	                	List<Map<String,Object>> createDepts = this.dao.findBySql(findCreateDeptSql, projectId);
	                	if(createDepts!=null && createDepts.size()>0){
	                		createDepartmentGuid =(String) createDepts.get(0).get("k_create_department_guid");
	                	}
	                	if(list != null && list.size()>0){
	                		String deptId = list.get(0).get("cur_department_guid").toString();
	                		//创建单位等于退回到上一级部门  则状态为2(编制区退回)  否则状态为4(审核区退回)
	                		if(createDepartmentGuid.equals(deptId)){
	    	                	//更新状态
	    	                	String updateSql="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_BACK+"',k_receive_dep_id = '"+deptId+"' WHERE id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql);
	    	                	
	    	                	String updateSql2="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_BACK+"',k_receive_dep_id = '"+deptId+"' WHERE bundled_parent_id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql2);
	                		}else{
	    	                	//更新状态
	    	                	String updateSql="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_AUDITBACK+"',k_receive_dep_id = '"+deptId+"' WHERE id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql);
	    	                	
	    	                	String updateSql2="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_AUDITBACK+"',k_receive_dep_id = '"+deptId+"' WHERE bundled_parent_id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql2);
	                		}
	                	}
	                	
	                	Map<String, String> params = new HashMap<>();
	                	if(ttype.equals(GDMajorConstants.ttype_1)){
            				params.put("projectId", projectId);
            				majorProjectLogService.addOperationLog(params, GDMajorConstants.S_TO_DEAL_LOG_BACK, user);
            			}else if(ttype.equals(GDMajorConstants.ttype_3)){
            				params.put("projectId", projectId);
            				yearProjectLogService.addOperationLog(params, GDMajorConstants.S_YEAR_TO_DEAL_LOG_BACK, user);
            			}
	                }
	            }catch (Exception e){
	                result.setCode(ResultCodeType.Error);
	                result.setMsg(e.getMessage());
	                e.printStackTrace();
	                throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
	            }
	        }
	        return result;
	}
	@Override
	@Transactional
	public ResultInfo recallProject1(List<String> projectIdsList,String ttype,User user) throws BusinessServiceException {
		//执行结果
		 ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		 String tablename = MajorUtil.getTableNameByttype(ttype);
		 if(projectIdsList != null){
	            try{
	                for(int i=0;i< projectIdsList.size();i++){             	
	                	String projectId = projectIdsList.get(i);
	                	String SQL = "";
	                	if(ttype.equals(GDMajorConstants.ttype_1)){
	                		//获取上一级部门
	                		SQL="select cur_department_guid from s_to_deal_log where type = 3 and next_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' and relate_bill_id = '"+projectId+"' order by create_time desc";
	                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
	                		SQL="select cur_department_guid from s_year_to_deal_log where type = 3 and next_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' and relate_bill_id = '"+projectId+"' order by create_time desc";
		                }
	                	List<Map<String,Object>> list = this.dao.findBySql(SQL.toString());
	                	//查找项目的创建单位
	                	String findCreateDeptSql = "select k_create_department_guid from "+tablename+" where id  = ?";
	                	String createDepartmentGuid="";
	                	List<Map<String,Object>> createDepts = this.dao.findBySql(findCreateDeptSql, projectId);
	                	if(createDepts!=null && createDepts.size()>0){
	                		createDepartmentGuid =(String) createDepts.get(0).get("k_create_department_guid");
	                	}
	                	if(list != null && list.size()>0){
	                		String deptId = list.get(0).get("cur_department_guid").toString();
	                		//创建单位等于退回到上一级部门  则状态为2(编制区退回)  否则状态为4(审核区退回)
	                		if(createDepartmentGuid.equals(deptId)){
	    	                	//更新状态
	    	                	String updateSql="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_BACK+"',k_receive_dep_id = '"+deptId+"' WHERE id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql);
	    	                	
	    	                	String updateSql2="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_BACK+"',k_receive_dep_id = '"+deptId+"' WHERE bundled_parent_id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql2);
	                		}else{
	    	                	//更新状态
	    	                	String updateSql="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_AUDITBACK+"',k_receive_dep_id = '"+deptId+"' WHERE id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql);
	    	                	
	    	                	String updateSql2="UPDATE "+tablename+" set K_status = '"+GDMajorConstants.STORE_PROJECT_STATUS_AUDITBACK+"',k_receive_dep_id = '"+deptId+"' WHERE bundled_parent_id = '"+projectId+"' ";
	    	                	this.dao.executeSql(updateSql2);
	                		}
	                	}
	                	
	                	Map<String, String> params = new HashMap<>();
	                	if(ttype.equals(GDMajorConstants.ttype_1)){
            				params.put("projectId", projectId);
            				majorProjectLogService.addOperationLog(params, GDMajorConstants.S_TO_DEAL_LOG_BACK, user);
            			}else if(ttype.equals(GDMajorConstants.ttype_3)){
            				params.put("projectId", projectId);
            				yearProjectLogService.addOperationLog(params, GDMajorConstants.S_YEAR_TO_DEAL_LOG_BACK, user);
            			}
	                }
	            }catch (Exception e){
	                result.setCode(ResultCodeType.Error);
	                result.setMsg(e.getMessage());
	                e.printStackTrace();
	                throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
	            }
	        }
	        return result;
	}
	/**
	 * liangchang
	 * 删除任务
	 */
    @Override
    public ResultInfo deleteTask(Map<String, String> params) throws BusinessServiceException {
        ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
        String taskids=params.get("taskids");


        if(StringUtil.isAllNullOrEmpty(taskids)){
            return new ResultInfo(ResultCodeType.Error, "任务ID(sc_paln :id)为空.");
        }

        try{
            String[] _taskids=taskids.split(",");
            for(String str:_taskids){
                /**
                 * 删除任务发布表
                 */
                StringBuilder SQL = new StringBuilder();
                SQL.append("DELETE from com_paln_task_pub WHERE task_id='"+str+"';  ");
                int num1=this.dao.executeSql(SQL.toString());

                /**
                 * 删除附件表
                 */
                StringBuilder SQLatt = new StringBuilder();
                SQLatt.append("DELETE from attachment WHERE relate_bill_id='"+str+"'; ");
                int num2=this.dao.executeSql(SQLatt.toString());

                /**
                 * 删除附件表
                 */
                StringBuilder SQLtask = new StringBuilder();
                SQLtask.append("DELETE from com_paln_task WHERE id='"+str+"'; ");
                int num3=this.dao.executeSql(SQLtask.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            result = new ResultInfo(ResultCodeType.Error, e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除项目
     * @param params
     * @return
     * @throws BusinessServiceException
     */
    @Override
    public ResultInfo deleteProject(Map<String, String> params) throws BusinessServiceException {
        ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
        String taskids=params.get("taskids");


        if(StringUtil.isAllNullOrEmpty(taskids)){
            return new ResultInfo(ResultCodeType.Error, "项目id为空.");
        }

        try{
            String[] _taskids=taskids.split(",");
            for(String str:_taskids){
                /**
                 * 删除任务发布表
                 */
                StringBuilder SQL = new StringBuilder();
                //SQL.append("DELETE from invet_project WHERE id='"+str+"';  ");
                SQL.append("update invet_project set is_deleted = 1 WHERE id='"+str+"';  ");
                int num1=this.dao.executeSql(SQL.toString());

            }
        }catch (Exception e){
            e.printStackTrace();
            result = new ResultInfo(ResultCodeType.Error, e.getMessage());
        }
        return result;
    }
    /*liangchang
     * 保存或者更新意见反馈
     * */
	@Override
	public ResultInfo saveAdvice(Map<String, String> params, User loginUser,String ttype) {
		 ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		 String projectIds = params.get("projectIds");
		 String taskID = params.get("taskID");
		 String deptId = params.get("deptId");
		 String content = params.get("content");
		 String status = params.get("status");
		 Date date = new Date();
		 if(StringUtil.isAllNullOrEmpty(projectIds)){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("项目Id为空");
			 return result;
		 }
		 if(StringUtil.isAllNullOrEmpty(taskID)){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("任务Id为空");
			 return result;
		 }
		 if(StringUtil.isAllNullOrEmpty(deptId)&&status.equals("select")){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("资源部门Id为空");
			 return result;
		 }
		
		 String[] IdArr = projectIds.split(",");
		 String[] deptIdArr = {};
		 if(!StringUtil.isAllNullOrEmpty(deptId)){
			 deptIdArr= deptId.split(",");
		 }
		 for(String projectId : IdArr){
			//挑选征求意见部门
			if(status.equals("select")){
				for(String deptID:deptIdArr){
					//向部门征求意见
					ComPalnTaskIdea idea = new ComPalnTaskIdea();
					ComPalnTaskIdea advice = getAdvice(taskID,projectId,deptID);
					if(advice==null){
						idea.setTaskId(taskID);
						idea.setPeojectId(projectId);
						idea.setDeptId(deptID);
						idea.setCreateUserId(loginUser.getEmployeeGuid());
						idea.setCreateUserName(loginUser.getUsername());
						idea.setCreateTime(date);
						idea.setUpdateUserId(loginUser.getEmployeeGuid());
						idea.setUpdateUserName(loginUser.getUsername());
						idea.setUpdateTime(date);
						idea.setVersion(0);
						idea.setTaskIdeaStatus(0);
						this.dao.saveOrUpdate(idea);
					}
				}
		 	}
			if(status.equals("content")){
				ComPalnTaskIdea ideaUp = getAdvice(taskID,projectId,loginUser.getDeptId());
				if(ideaUp!=null){
					//填意见
					if(!StringUtil.isAllNullOrEmpty(content)){
						ideaUp.setContent(content);
					}else{
						result.setCode(ResultCodeType.Warning);
						result.setMsg("内容为空！");
						return result;
					}
					ideaUp.setTaskIdeaStatus(1);
					ideaUp.setUpdateUserId(loginUser.getEmployeeGuid());
					ideaUp.setUpdateUserName(loginUser.getUsername());
					ideaUp.setUpdateTime(date);
					this.dao.update(ideaUp); 
				}
			 }
		 }
		return result;
	}
	@Override
	public ResultInfo saveTransfer(Map<String, String> params, User loginUser,String ttype) throws Exception {
		 ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		 String projectIds = params.get("projectIds");
		 String taskID = params.get("taskID");
		 String deptId = params.get("deptId");
		 String pubfid = params.get("pubfid");
		 String content = params.get("content");
		 String status = params.get("status");
		 Date date = new Date();
		 if(StringUtil.isAllNullOrEmpty(projectIds)){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("项目Id为空");
			 return result;
		 }
		 if(StringUtil.isAllNullOrEmpty(taskID)){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("任务Id为空");
			 return result;
		 }
		 if(StringUtil.isAllNullOrEmpty(deptId)&&status.equals("select")){
			 result.setCode(ResultCodeType.Warning);
			 result.setMsg("资源部门Id为空");
			 return result;
		 }
		
		 String[] IdArr = projectIds.split(",");
		 String[] deptIdArr = {};
		 if(!StringUtil.isAllNullOrEmpty(deptId)){
			 deptIdArr= deptId.split(",");
		 }
		 for(String projectId : IdArr){
			//挑选征求意见部门
			if(status.equals("select")){
				for(String deptID:deptIdArr){
					//向部门征求意见
					//ComPalnTaskIdea idea = new ComPalnTaskIdea();
					//ComPalnTaskIdea advice = getAdvice(taskID,projectId,deptID);
					ComPalnTaskPub transfer = getTransfer(taskID,deptID);
					ComPalnTaskPub scPalnTaskPub=new ComPalnTaskPub();
	                    if(loginUser.getDeptId().equals(deptID)==false){
	                    	if(transfer!=null){
	                    		scPalnTaskPub = transfer;
	    					}
	                        scPalnTaskPub.setTaskId(taskID);

	                        //分发-下发，的上级任务发布表id
	                        scPalnTaskPub.setPubFid(pubfid);
	                        
	                        Department dept=departmentService.getById(deptID);

	                        scPalnTaskPub.setDeptId(deptID);
	                        scPalnTaskPub.setTaskpubType(2);//类型（创建=1 、接收=2 、分发=3）
	/*                      scPalnTaskPub.setFgwcode(dept.getFgwcode());
	                        scPalnTaskPub.setFgwcscode(dept.getFgwcscode());*/
	                        scPalnTaskPub.setDepAreaId(dept.getDepartmentAreaId());//设置部门区域编码
	                        
	                        scPalnTaskPub.setTaskpubStatus(0);//新增都属于 0=当前
	                        scPalnTaskPub.setLockStatus(0);//（0未锁定 、 1锁定）
	                        scPalnTaskPub.settStatus(0);//对于下发的接受单位，无论是下发的还是分发的，都属于新任务=0

	                        scPalnTaskPub.setCreateUserId(loginUser.getEmployeeGuid());
	                        scPalnTaskPub.setCreateUserName(loginUser.getEmployeeFullname());
	                        scPalnTaskPub.setCreateTime(new Date());
	                        scPalnTaskPub.setUpdateUserId(loginUser.getEmployeeGuid());
	                        scPalnTaskPub.setUpdateUserName(loginUser.getEmployeeFullname());
	                        scPalnTaskPub.setUpdateTime(new Date());
	                        scPalnTaskPub.setVersion(1);//新增时版本号为1
	                        scPalnTaskPub.setIsReceive(0);

	                        this.dao.saveOrUpdate(scPalnTaskPub);
	                        StringBuilder sql2 = new StringBuilder();
                        	sql2.append("update com_project_info set k_receive_dep_id = '"+deptID+"' where id = '"+projectId+"'");
                        	this.dao.executeSql(sql2.toString());
                        	params.put("projectId", projectId);
                        	params.put("nextDeptId", deptID);
                        	majorProjectLogService.addOperationLog(params, GDMajorConstants.S_TO_DEAL_LOG_REPORT, loginUser);
	                    }else{
	                        //下发或者分发，不保存自己
	                    }
				}
		 	}
			if(status.equals("content")){
				ComPalnTaskIdea ideaUp = getAdvice(taskID,projectId,loginUser.getDeptId());
				if(ideaUp!=null){
					//填意见
					if(!StringUtil.isAllNullOrEmpty(content)){
						ideaUp.setContent(content);
					}else{
						result.setCode(ResultCodeType.Warning);
						result.setMsg("内容为空！");
						return result;
					}
					ideaUp.setTaskIdeaStatus(1);
					ideaUp.setUpdateUserId(loginUser.getEmployeeGuid());
					ideaUp.setUpdateUserName(loginUser.getUsername());
					ideaUp.setUpdateTime(date);
					this.dao.update(ideaUp); 
				}
			 }
		 }
		return result;
	}
	/**
	 * liangchang
	 * @param taskID
	 * @param projectId
	 * @param deptId
	 * @return
	 */
	//判断部门是否征求了意见
	public String checkAdvice(String taskID,String projectId,String deptId){
		StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT id from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='"+deptId+"'");
        List<Map<String, Object>> result=this.dao.findBySql(SQL.toString());
        String id = "";
        if(!result.isEmpty()){
        	id=(String) result.get(0).get("id");
        }
		return id;
	}
	/**
	 * liangchang
	 * @param taskID
	 * @param projectId
	 * @param deptId
	 * @return
	 */
	//判断部门是否征求了意见
	public ComPalnTaskIdea getAdvice(String taskID,String projectId,String deptId){
		StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT id from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='"+deptId+"'");
		//SQL.append("select * from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='SCHB10001'");
        List<Map<String, Object>> result=this.dao.findBySql(SQL.toString());
        if(!result.isEmpty()){
        	return scPlanTaskIdeaDao.get((String)result.get(0).get("id"));
        }
		return null;
	}
	@Override
	public ComPalnTaskIdea getAdviceContent(String taskID,String projectId,String deptId){
		StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT id from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='"+deptId+"'");
		//SQL.append("select * from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='SCHB10001'");
        List<Map<String, Object>> result=this.dao.findBySql(SQL.toString());
        if(!result.isEmpty()){
        	return scPlanTaskIdeaDao.get((String)result.get(0).get("id"));
        }
		return null;
	}
	@Override
	public ComPalnTaskIdea getAdviceContentById(String ID){
        	return scPlanTaskIdeaDao.get(ID);
	}
	public ComPalnTaskPub getTransfer(String taskID,String deptId) throws Exception{
		StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT id from com_paln_task_Pub WHERE task_id='"+taskID+"'  and dept_id ='"+deptId+"'");
		//SQL.append("select * from com_paln_task_idea WHERE task_id='"+taskID+"' and peoject_id='"+projectId+"' and dept_id ='SCHB10001'");
        List<Map<String, Object>> result=this.dao.findBySql(SQL.toString());
        if(!result.isEmpty()){
        	return mPlanDao.findByid((String)result.get(0).get("id"));
        }
		return null;
	}
	@Override
	public void updateExclusiveLabel(String project_id, String label_status) throws BusinessServiceException {
		// TODO Auto-generated method stub
		String sqlpub="UPDATE COM_project_info set exclusive_label = ? WHERE id = ?  ";
        int num = this.dao.executeSql(sqlpub,label_status,project_id);
	}
	/**
	 * 查询任务名称下拉选项
	 */
	@Override
	public List<TreeVO> getTaskNameSelection(Map<String, String> params) throws BusinessServiceException {
		 List<TreeVO> treeVoList = new ArrayList<TreeVO>();
		String ttype = params.get("ttype");
		String deptId = params.get("deptId");
		if(StringUtil.isAllNullOrEmpty(ttype)){
			throw new BusinessServiceException("模块标志ttype错误!");
		}
		if(StringUtil.isAllNullOrEmpty(deptId)){
			throw new BusinessServiceException("获取当前用户部门Id错误!");
		}
		try{
			List<Map<String,Object>> results= mPlanDao.getTaskNameSelection(params);
			int i=0;
	        for (Map<String,Object> result :results){
	            TreeVO vo = new TreeVO();
	            vo.setName((String)result.get("task_name"));
	            vo.setId(""+i);
	            vo.setpId("-1");
	            treeVoList.add(vo);
	            i=i+1;
	        }
		}catch (Exception e) {
			throw new BusinessServiceException("查询任务名称失败！");
		}
		return treeVoList;
	}

	@Override
	public ResultInfo isSelectDepartment(Map<String, String> params) {
		ResultInfo resultInfo = new ResultInfo(ResultCodeType.Success);
		String departments = params.get("selectDepentments");
		String taskId = params.get("taskId");
		if(StringUtil.isAllNullOrEmpty(departments)){
			resultInfo.setCode(ResultCodeType.Warning);
			resultInfo.setMsg("所选部门为空!");
			return resultInfo;
		}
		if(StringUtil.isAllNullOrEmpty(taskId)){
			resultInfo.setCode(ResultCodeType.Warning);
			resultInfo.setMsg("任务标识为空!");
			return resultInfo;
		}
		String sql = "select dept_id from com_paln_task_pub where task_id = :taskId";
		Map<String,Object> values = new HashMap<>();
		values.put("taskId", taskId);
		List<Map<String,Object>> results=this.dao.findBySql(sql, values);
		List<String> departmentRepeat = new ArrayList<>();
		for(Map<String,Object> result:results){
			String deptId=(String) result.get("dept_id");
			int num = departments.indexOf(deptId+",");
			if(num>=0){
				resultInfo.setCode(ResultCodeType.ParamError);
				departments=StringUtil.replace(departments, deptId+",", "");
				String dept = departmentService.getById(deptId).getDepartmentFullname();
				departmentRepeat.add(dept);
			}
		}
		resultInfo.setResultData(departmentRepeat);
		return resultInfo;
	}
	/**
	 * liangchang
	 */
	@Override
	public List<Map<String, Object>> getAdviceDept(Map<String, String> params,
			User user) {
		StringBuilder SQL = new StringBuilder();
		String project_id = params.get("project_id");
		String taskid = params.get("taskid");
		
		SQL.append(" select ");
		SQL.append(" d.department_fullname ");
		SQL.append(" from ");
		SQL.append(" com_paln_task_idea s ");
		SQL.append(" left join department d ");
		SQL.append(" on d.department_guid = s.dept_id ");
		SQL.append(" where ");
		SQL.append(" s.peoject_id = '"+project_id+"' ");
		SQL.append(" and s.task_id = '"+taskid+"' ");
		/*SQL.append(" select ");
		SQL.append(" d.department_fullname ");
		SQL.append(" from department d ");
		SQL.append(" where ");
		SQL.append(" d.type = 'DEPT' and store_level = 3");*/
		
		
		List<Map<String, Object>> list = this.dao.findBySql(SQL.toString());
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getTaskYear(Map<String, String> params,
			User user) {
		StringBuilder SQL = new StringBuilder();
		String project_id = params.get("project_id");
		String taskid = params.get("taskid");
		
		SQL.append(" select distinct task_year as id,task_year as text from com_paln_task ");
		List<Map<String, Object>> list = this.dao.findBySql(SQL.toString());
		return list;
	}
	/**
     * 提交到终点项目库
     * @throws Exception
     */
	@Override
	@Transactional
	public ResultInfo importToStore(Map<String, String> params, User user, List<String> projectIdsList) {
		ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		
        String pubid = params.get("pubid");
        String task_id = params.get("task_id");
        String ttype = params.get("ttype");
		
		String tableName=MajorUtil.getTableNameByttype(ttype);
        if (StringUtil.isAllNullOrEmpty(tableName)) {
            try {
				throw new Exception("参数(ttype="+ttype+")传入错误，导致获取表名错误,请检查。");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        //校验合规性
        if(!StringUtil.isAllNullOrEmpty(params.get("project_ids"))){
            List<Map<String,Object>> szdProjectList = storeProjectProgressDAO.getProjectInfoTable(params.get("project_ids"),tableName);
            //result = storeProjectDraftService.finishCheck(szdProjectList);
            if (result.getCode().equals(ResultCodeType.Error)) {
                return result;
            }
            //result = storeProjectDraftService.investmentCheck(szdProjectList);
            if (result.getCode().equals(ResultCodeType.Error)) {
                return result;
            }
        }
        
        try{
            
            if(projectIdsList != null && projectIdsList.size() > 0){
            	
                for(String id : projectIdsList){
                	
                	//查找打捆子项目
                	List<Map<String, Object>> dataResul = new ArrayList<>();
                	String sql1 = "select id as proid from "+tableName+" where bundled_parent_id = '"+id+"' ";
                	dataResul = this.dao.findBySql(sql1);
                	
                	//修改子项目状态
                	if(dataResul.size()>0 && dataResul !=null){
                		for(Map<String, Object> map : dataResul){
                			String proid = map.get("proid").toString();
                			String sql2 = "update "+tableName+" set K_status = '"+GDMajorConstants.Store_PROJECT_STATUS_PUSH+"' where id = '"+proid+"' ";
                			this.dao.executeSql(sql2);
                		}
                	}
                	
                	//修改项目状态
                	String sql3 = "update "+tableName+" set K_status = '"+GDMajorConstants.Store_PROJECT_STATUS_PUSH+"' where id = '"+id+"' ";
        			this.dao.executeSql(sql3);
                	
                }
                
                
            }else{
            	result.setMsg("没有项目");
            }
            
            
        }catch (Exception e){
            result.setCode(ResultCodeType.Error);
            result.setMsg(e.getMessage());
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PLAN_FAIL, e);
        }
		return result;
	}

	@Override
    @Transactional
	public ResultInfo updateProject(InvestProject investProject, User user) {
		ResultInfo result = new ResultInfo(ResultCodeType.Success, MajorConstants.ADD_DATA_SUCCESS);
		try {
			InvestProject _upscPalnTask = get(InvestProject.class, investProject.getId());
            //_upscPalnTask.setVersion(_upscPalnTask.getVersion()+1);
            _upscPalnTask.setProName(investProject.getProName());
            _upscPalnTask.setIndustry(investProject.getIndustry());
           // _upscPalnTask.setTaskType(scPalnTask.getTaskType());    //伍红林
            _upscPalnTask.setInvestmentPurposes(investProject.getInvestmentPurposes());
            _upscPalnTask.setInvestmentScale(investProject.getInvestmentScale());
            _upscPalnTask.setIsDeleted(investProject.getIsDeleted());
            _upscPalnTask.setCompany(investProject.getCompany());
            _upscPalnTask.setCheckStatus(investProject.getCheckStatus());
            
            _upscPalnTask.setNewField1(investProject.getNewField1());
            _upscPalnTask.setNewField2(investProject.getNewField2());
            _upscPalnTask.setNewField3(investProject.getNewField3());
            _upscPalnTask.setNewField4(investProject.getNewField4());
            _upscPalnTask.setNewField5(investProject.getNewField5());
            _upscPalnTask.setNewField6(investProject.getNewField6());
            _upscPalnTask.setNewField7(investProject.getNewField7());
            _upscPalnTask.setNewField8(investProject.getNewField8());
            _upscPalnTask.setNewField9(investProject.getNewField9());
            
            _upscPalnTask.setMemo(investProject.getMemo());
            _upscPalnTask.setProStatus(investProject.getProStatus());
            _upscPalnTask.setResponsibleMan(investProject.getResponsibleMan());
            _upscPalnTask.setUpdateUserId(user.getEmployeeGuid());
            _upscPalnTask.setUpdateUserName(user.getEmployeeFullname());
            _upscPalnTask.setUpdateTime(new Date());
			this.dao.saveOrUpdate(_upscPalnTask);
		}catch (Exception e){
            result.setCode(ResultCodeType.Error);
            result.setMsg(e.getMessage());
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PROJECT_FAIL, e);
        }
		return result;
	}

	@Override
	@Transactional
	public InvestProject saveOrUpdateProject(InvestProject investProject, User user) {
		try {
			//scPalnTask.setTaskStatus(0);//新建
			investProject.setCreateUserId(user.getEmployeeGuid());
			investProject.setCreateUserName(user.getEmployeeFullname());
			investProject.setCreateTime(new Date());
			investProject.setUpdateUserId(user.getEmployeeGuid());
			investProject.setUpdateUserName(user.getEmployeeFullname());
			investProject.setUpdateTime(new Date());
			investProject.setIsDeleted(0);
			investProject.setCheckStatus(0);
			this.dao.save(investProject);
		}catch (Exception e){
            e.printStackTrace();
            throw new BusinessServiceException(MajorConstants.FILL_PROJECT_FAIL, e);
        }
		return investProject;
	}

	@Override
	public InvestProject findProByID(String id) {
		InvestProject scPalnTask=null;
        try{
            scPalnTask=mPlanDao.findProByID(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return scPalnTask;
	}
}
