<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.dao.${parentModule}.${classNameLowerCase}.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.strongit.iss.common.GDMajorConstants;
import com.strongit.iss.common.DaoContextDaMeng;
import com.strongit.iss.dao.major.scplan.inter.IMPlanDao;
import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.InvestProject;
import com.strongit.iss.entity.User;
import com.strongit.iss.entity.sc.ComPalnTask;
import com.strongit.iss.entity.sc.ComPalnTaskPub;
import com.strongit.iss.hibernate.Page;
import com.strongit.iss.hibernate.QueryParameter;
import com.strongit.iss.newdao.hibernate.HibernateDAO;
import com.strongit.iss.util.StringUtil;
import com.strongit.iss.utils.MajorUtil;


/**
 <#include "/java_description.include">
 */
@Repository
public class ${className}DaoImpl extends HibernateDAO<ComPalnTaskPub, String> implements I${className}Dao {
    @Resource
    protected DaoContextDaMeng dao;

    /**
     * 
     */
    @Override
    public Page<Map<String, Object>> getScPalnTaskPageByCondition(Map<String, String> params, QueryParameter dq) throws Exception {
        //sql语句
        StringBuilder sql = new StringBuilder();

        // 处理分页对象
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(Integer.valueOf(dq.getPageSize()),dq.isAutoCount());
        page.setPageNo(dq.getPageNo()); // 设置当前需要获取数据的页码

        //修改   伍红林
        
        //sql.append("SELECT t.*,st.*,items.item_value ");
        sql.append("SELECT t.id as pubid,st.id as task_id, t.t_status ,t.pub_fid ,t.task_id ,t.lock_status,t.taskpub_type, st.*");
        sql.append("FROM com_paln_task_pub t ");
        sql.append("LEFT JOIN com_paln_task st ON t.task_id = st.id ");

        // where条件<任务下发区，查询自己创建的任务。>
        sql.append(" WHERE t.taskpub_type = 1 ");
        sql.append(" AND t.t_status NOT IN ('5')");
        if(!StringUtil.isAllNullOrEmpty(params.get("ttype"))){
            sql.append(" AND st.ttype= "+params.get("ttype")+" ");          
        }else{
        	new Exception("分类错误。");
        }

        /**
         * 如果是自己创建的，则可以查看撤回状态的数据，如果不是自己创建的则不能查看
         *
         */
        if(!StringUtil.isAllNullOrEmpty(params.get("taskStatus"))){
        	//-2查询全部
        	if(!params.get("taskStatus").equals("-2")){
        		sql.append(" AND t.t_status= '"+params.get("taskStatus")+"' ");
        	}
        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("deptid"))){
            sql.append(" AND t.dept_id= '"+params.get("deptid")+"' ");
        }

        if(!StringUtil.isAllNullOrEmpty(params.get("taskName"))&&!params.get("taskName").equals("全部通知")){
            sql.append(" AND st.task_name like '%"+params.get("taskName")+"%' ");
        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("taskYear"))){
            sql.append(" AND st.task_year like '%"+params.get("taskYear")+"%' ");
        }

        if(!StringUtil.isAllNullOrEmpty(params.get("sTime"))){
            sql.append(" AND st.start_time > '"+params.get("sTime")+" 00:00:00' ");
        }

        if(!StringUtil.isAllNullOrEmpty(params.get("eTime"))){
            sql.append(" AND st.start_time < '"+params.get("eTime")+" 23:59:59' ");
        }

//        if(!StringUtil.isAllNullOrEmpty(params.get("taskTypeCode"))){
//            sql.append(" AND st.task_type = '"+params.get("taskTypeCode")+"' ");
//        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("storeLevel"))){
            sql.append(" AND st.store_level = '"+params.get("storeLevel")+"' ");
        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("deptName"))){
            sql.append(" AND st.create_dept_name = '"+params.get("deptName")+"' ");
        }

        //排序
        sql.append(" ORDER BY st.task_year desc,st.create_time desc ");

        // 执行检索
        page = this.dao.findBySql(page, sql.toString());
        return page;
    }
    @Override
    public Page<Map<String, Object>> getProjectPageByCondition(Map<String, String> params, QueryParameter dq) throws Exception {
        //sql语句
        StringBuilder sql = new StringBuilder();

        // 处理分页对象
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(Integer.valueOf(dq.getPageSize()),dq.isAutoCount());
        page.setPageNo(dq.getPageNo()); // 设置当前需要获取数据的页码

        //修改   伍红林
        
        //sql.append("SELECT t.*,st.*,items.item_value ");
        sql.append("SELECT id, pro_name, pro_status, responsible_man, industry, company, investment_purposes, investment_scale, memo, check_status, is_deleted, create_user_id, create_user_name, create_time, update_user_id, update_user_name, update_time, new_field3 newField3, new_field4 newField4, new_field5 newField5, new_field6 newField6, new_field7 newField7, new_field8 newField8, new_field9 newField9, new_field1 newField1, new_field2 newField2, pro_introduction");
        sql.append(" FROM invet_project t ");

        // where条件<任务下发区，查询自己创建的任务。>
        sql.append(" WHERE 1 = 1  and is_deleted = 0 ");
        if(!StringUtil.isAllNullOrEmpty(params.get("ttype"))){
        	if("0".equals(params.get("ttype"))&&!"ADMIN".equalsIgnoreCase(params.get("uid"))){
        		sql.append(" AND t.check_status= "+params.get("ttype")+" and t.create_user_id = '"+params.get("uid")+"'");
        	}else if("3".equals(params.get("ttype"))&&!"ADMIN".equalsIgnoreCase(params.get("uid"))){
        		sql.append(" AND t.check_status in('2','1') and t.create_user_id = '"+params.get("uid")+"'");
			}else if("3".equals(params.get("ttype"))&&"ADMIN".equalsIgnoreCase(params.get("uid"))){
        		sql.append(" AND t.check_status in('2','1') ");
			}else {
				sql.append(" AND t.check_status= "+params.get("ttype")+" ");
			}
        }else{
        	new Exception("分类错误。");
        }

        /**
         * 如果是自己创建的，则可以查看撤回状态的数据，如果不是自己创建的则不能查看
         *
         */
        if(!StringUtil.isAllNullOrEmpty(params.get("proStatus"))){
        		sql.append(" AND t.pro_status= '"+params.get("proStatus")+"' ");
        }
        
        /*if(!StringUtil.isAllNullOrEmpty(params.get("deptid"))){
            sql.append(" AND t.dept_id= '"+params.get("deptid")+"' ");
        }*/

        if(!StringUtil.isAllNullOrEmpty(params.get("proName"))){
            sql.append(" AND t.pro_name like '%"+params.get("proName")+"%' ");
        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("investmentPurposes"))){
            sql.append(" AND t.investment_purposes like '%"+params.get("investmentPurposes")+"%' ");
        }

        if(!StringUtil.isAllNullOrEmpty(params.get("sTime"))){
            sql.append(" AND t.start_time > '"+params.get("sTime")+" 00:00:00' ");
        }

        if(!StringUtil.isAllNullOrEmpty(params.get("eTime"))){
            sql.append(" AND t.start_time < '"+params.get("eTime")+" 23:59:59' ");
        }

//        if(!StringUtil.isAllNullOrEmpty(params.get("taskTypeCode"))){
//            sql.append(" AND st.task_type = '"+params.get("taskTypeCode")+"' ");
//        }
        
        if(!StringUtil.isAllNullOrEmpty(params.get("industry"))){
            sql.append(" AND t.industry = '"+params.get("industry")+"' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField1"))){
            sql.append(" AND t.new_field1 like '%"+params.get("newField1")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField2"))){
            sql.append(" AND t.new_field2 like '%"+params.get("newField2")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField3"))){
            sql.append(" AND t.new_field3 like '%"+params.get("newField3")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField4"))){
            sql.append(" AND t.new_field4 like '%"+params.get("newField4")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField5"))){
            sql.append(" AND t.new_field5 like '%"+params.get("newField5")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField6"))){
            sql.append(" AND t.new_field6 like '%"+params.get("newField6")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField7"))){
            sql.append(" AND t.new_field7 like '%"+params.get("newField7")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField8"))){
            sql.append(" AND t.new_field8 like '%"+params.get("newField8")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("newField9"))){
            sql.append(" AND t.new_field9 like '%"+params.get("newField9")+"%' ");
        }
        if(!StringUtil.isAllNullOrEmpty(params.get("deptName"))){
            sql.append(" AND t.create_dept_name = '"+params.get("deptName")+"' ");
        }

        //排序
        sql.append(" ORDER BY t.create_time desc ");

        // 执行检索
        page = this.dao.findBySql(page, sql.toString());
        return page;
    }
    @Override
    public List<Map<String, Object>> getTypeTaskList(String typecode, String deptId,String ttype,String taskId,Map<String, String> params) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        String t_status=params.get("t_status");
        

        sql.append("SELECT  ");
        sql.append(" st.task_name, ");
        sql.append(" t.task_id,  ");
        sql.append(" t.lock_status, ");
        sql.append(" t.pub_fid, ");
        sql.append(" t.id  ");
        sql.append("FROM com_paln_task_pub t ");
        sql.append("LEFT JOIN com_paln_task st ON t.task_id = st.id ");
        
        if(!StringUtil.isAllNullOrEmpty(t_status)){
        	sql.append(" where t.t_status = "+t_status+" ");
        }else{
        	sql.append(" where t.t_status in (0,1,2)  ");
        }
        
        if(!StringUtil.isAllNullOrEmpty(deptId)){
            sql.append(" AND t.dept_id='"+deptId+"'  ");
        }
        if(!StringUtil.isAllNullOrEmpty(typecode)){
            sql.append(" AND st.task_type='"+typecode+"'");
        }
        if(!StringUtil.isAllNullOrEmpty(ttype)){
            sql.append(" AND st.ttype='"+ttype+"' ");
        }
        if(!StringUtil.isAllNullOrEmpty(taskId)){
            sql.append(" AND t.task_id != '"+taskId+"' ");
        }
        List<Map<String, Object>> list=this.dao.findBySql(sql.toString());
        return list;
    }

	@Override
	public void saveScPalnTaskPub(ComPalnTaskPub scPalnTaskPub) throws Exception {
		
		
	}

    @Override
    public ComPalnTask findByID(String id) throws Exception {
        String hql = "from ComPalnTask where id= :id";
        Map<String, Object> values = Maps.newHashMap();
        values.put("id", id);
        return findUnique(hql, values);
    }
    @Override
    public InvestProject findProByID(String id) throws Exception {
        String hql = "from InvestProject where id= :id";
        Map<String, Object> values = Maps.newHashMap();
        values.put("id", id);
        return findUnique(hql, values);
    }
    @Override
    public ComPalnTaskPub findByid(String id) throws Exception {
        String hql = "from ComPalnTaskPub where id= :id";
        Map<String, Object> values = Maps.newHashMap();
        values.put("id", id);
        return findUnique(hql, values);
    }
    @Override
    public Page<Map<String, Object>> getTaskProjectPage(Map<String, String> params, QueryParameter queryParameter,Department department) throws Exception {
        // 申明SQL变量
        StringBuilder SQL = new StringBuilder();
        // 传参集合
        List<String> listValues = new ArrayList<>();
        // 处理分页对象
        Page<Map<String, Object>> page = new Page<Map<String, Object>>(Integer.valueOf(queryParameter.getPageSize()),queryParameter.isAutoCount());
        // 设置当前需要获取数据的页码
        page.setPageNo(queryParameter.getPageNo());

        // 模块分类（<1重点、2三年、3年度>）
        String ttype = params.get("ttype");
        //任务类型
        String taskType = params.get("taskType") ;
        //任务名称
        String taskName = params.get("taskName") ;

        String bitDeptID = params.get("bitDeptID") ;

        SQL.append(" SELECT ");
        SQL.append("    st.task_name, ");
        SQL.append("    items.item_value, ");
        SQL.append("    st.task_desc, ");
        SQL.append("    st.start_time, ");
        SQL.append("    st.end_time, ");
        SQL.append("    st.id task_id ");
        SQL.append("FROM ");
        SQL.append("    com_paln_task_pub t ");
        SQL.append("LEFT JOIN ");
        SQL.append("    com_paln_task st ");
        SQL.append("ON ");
        SQL.append("    t.task_id = st.id ");
        SQL.append("LEFT JOIN ");
        SQL.append("    dictionary_items items ");
        SQL.append("ON ");
        SQL.append("    st.task_type=items.item_key ");
        SQL.append("WHERE  ");
        SQL.append("    t.t_status ='3' ");
        SQL.append("    AND t.pub_fid is not null  ");
        SQL.append("    AND t.taskpub_type = 1  ");//查询库中的任务时，只需创建创建的任务即可。

        /*模块分类*/
        if (!StringUtil.isAllNullOrEmpty(ttype)) {
            SQL.append(" and st.ttype = ? ");
            listValues.add(ttype);
        }
        /*任务类型（计划管理中的任务分类）*/
        if (!StringUtil.isAllNullOrEmpty(taskType)) {
            SQL.append(" and st.task_type = ? ");
            listValues.add(taskType);
        }


        //点击的部门id
        if (!StringUtil.isAllNullOrEmpty(bitDeptID)) {
            //String fgwcode   = department.getFgwcode();
            //SQL.append(" and t.fgwcode = '"+fgwcode+"' ");
        }else{
            //为空，则表示本级任务
            SQL.append(" and t.dept_id = ? ");
            listValues.add(department.getDepartmentGuid());
        }




        /*任务名称*/
        if (!StringUtil.isAllNullOrEmpty(taskName)) {
            SQL.append(" and st.task_name LIKE '%");
            taskName = MajorUtil.getStrWithoutSQLInjection(taskName.trim());//获取转义后的查询条件
            SQL.append(taskName);
            SQL.append("%' ESCAPE '/' ");
        }

        SQL.append(" order by t.create_time desc ");

        try {
            page = dao.findBySql(page, SQL.toString(), listValues.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> getProjectMsg(User user, Map<String, String> params, String taskId, String loginDeptID) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	
    	String pubtaskID = params.get("pubtaskid");
    	
    	String nowOrhisTag = params.get("hisOrNow");
    	
    	String taskpubType = params.get("taskpubType");
    	
    	String ttype = params.get("ttype");
    	
    	String tableName=MajorUtil.getTableNameByttype(params.get("ttype"));
    	
        if(StringUtil.isAllNullOrEmpty(taskId)){
            throw new Exception("任务ID(taskId)为空，请检查");
        }

        sql.append("select sum(t.project_total_fund) total ");
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" ,sum(p.gj_investment_total) money ");
        }
        sql.append(" FROM com_paln_task_project t ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task SC ");
        sql.append(" ON");
        sql.append("    T.task_id=SC.ID ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task_pub PT ");
        sql.append(" ON");
        sql.append("    T.pubtask_id=PT.ID ");
        if(nowOrhisTag.equals("advice")){
        	sql.append(" LEFT JOIN");
            sql.append(" 	com_paln_task_IDEA SPTI");
            sql.append(" ON SPTI.PEOJECT_ID = t.PROJECT_ID");
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" LEFT JOIN "+tableName+" p on t.project_id = p.id     ");
        }/*else{
        	sql.append(" LEFT JOIN com_project_info s on t.project_id = s.id     ");
        }*/
        sql.append(" WHERE t.task_id = '"+taskId+"'  ");

    	if(!nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS) && !"report".equals(nowOrhisTag)){
    		if(!StringUtil.isAllNullOrEmpty(pubtaskID)){
                sql.append(" AND t.pubtask_id = '"+pubtaskID+"'  ");
            }
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
        	if(!nowOrhisTag.equals("advice")){
        		sql.append(" and p.type != 2 ");
        	}
        	
            if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and p.k_status in (1,2) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id = '"+user.getDepartment().getDepartmentAreaId()+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and p.k_status in (3,4) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    //sql.append(" AND p.receive_dep_id != p.create_department_guid ");
                    
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                    
                    /*if("1".equals(taskpubType)){
                        //关联自己的任务。
//                        SQL.append(" and PT.dept_id = ? ");
//                        listValues.add(Dept.getDepartmentGuid());
                    	sql.append(" and t.sc_level = 1 ");
                        
                	}else if("2".equals(taskpubType)){
                		
                    	//sql.append(" and PT.taskpub_type = 1 ");
                		sql.append(" and t.s_level = 1 ");
                		sql.append(" and t.sc_level != 1 ");
                		
                		StringBuilder sql0 = new StringBuilder();
                		StringBuilder sql1 = new StringBuilder();
                		sql0.append("select id from com_paln_task_pub where taskpub_type=2 and dept_id='"+user.getDepartment().getDepartmentGuid()+"' and task_id='"+taskId+"'");
                		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                		list = this.dao.findBySql(sql0.toString());
                		List<String> list1 = new ArrayList<>();
                		List<String> pubid = new ArrayList<>();
                		for(Map<String, Object> map : list){
                			String fid = map.get("id").toString();
                			list1.add(fid);
                			pubid.add(fid);
                		}
                		
                		if(list1.size() >0){
                			for(String id : list1){
                    			sql1.append("select id from com_paln_task_pub where taskpub_type=3 and task_id='"+taskId+"' and pub_fid='"+id+"'");
                    			List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
                        		listmap = this.dao.findBySql(sql1.toString());
                        		for(Map<String, Object> map : listmap){
                        			String fid = map.get("id").toString();
                        			pubid.add(fid);
                        		}
                    		}
                		}
                		
                		sql.append(" and t.pubtask_id in (");
                		for(int i=0;i<pubid.size();i++){
                			String id = pubid.get(i);
                			sql.append("'"+id+"'");
                			if(i < pubid.size()-1){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                		sql.append(" and P.create_department_guid != '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                	}else if("3".equals(taskpubType)){
                		sql.append(" and PT.dept_id = '"+user.getDepartment().getDepartmentGuid()+"'");

                        //编制区，自己查看自己的项目
                		sql.append(" and t.creatd_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                		sql.append(" AND SC.create_dept_id = '"+user.getDepartment().getDepartmentGuid()+"' ");
                        
                	}*/
                    
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and p.k_status = 8 ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals("report")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append("  and p.k_status in(3,4,8,9) ");
                	String SQL = "";
                	if(ttype.equals(GDMajorConstants.ttype_1)){
                		//获取上一级部门
                		SQL="select distinct relate_bill_id from s_to_deal_log where type=3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
                		SQL="select distinct relate_bill_id from s_year_to_deal_log where type = 3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                    }
                	List<Map<String,Object>> proList = this.dao.findBySql(SQL.toString());
                	
                	
                	if(proList != null && proList.size()>0){
                		sql.append(" and p.id in (");
                		int i=0;
                		for(Map<String,Object> map : proList){
                			i++;
                			sql.append("'"+map.get("relate_bill_id")+"'");
                			if(i<proList.size()){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                	}
                	
                }
                if(nowOrhisTag.equals("advice")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append(" and SPTI.DEPT_ID = '"+user.getDepartment().getDepartmentGuid()+"' ");
                }

            }else{
            	sql.append(" AND p.k_status != 2  ");
            }
        }

        List<Map<String, Object>> list=this.dao.findBySql(sql.toString());
        
        String num = getProjectNum(user,params,taskId);
        
        list.get(0).put("num", num);
        
        return list;
    }
    
    public String getProjectNum(User user, Map<String, String> params, String taskId) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	
    	String pubtaskID = params.get("pubtaskid");
    	
    	String nowOrhisTag = params.get("hisOrNow");
    	
    	String taskpubType = params.get("taskpubType");
    	
    	String ttype = params.get("ttype");
    	
    	String tableName=MajorUtil.getTableNameByttype(params.get("ttype"));
    	
        if(StringUtil.isAllNullOrEmpty(taskId)){
            throw new Exception("任务ID(taskId)为空，请检查");
        }

        sql.append("select sum(t.project_total_fund) total,count(t.id) num  ");
        sql.append(" FROM com_paln_task_project t ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task SC ");
        sql.append(" ON");
        sql.append("    T.task_id=SC.ID ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task_pub PT ");
        sql.append(" ON");
        sql.append("    T.pubtask_id=PT.ID ");
        if(nowOrhisTag.equals("advice")){
        	sql.append(" LEFT JOIN");
            sql.append(" 	com_paln_task_IDEA SPTI");
            sql.append(" ON SPTI.PEOJECT_ID = t.PROJECT_ID");
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" LEFT JOIN "+tableName+" p on t.project_id = p.id     ");
        }/*else{
        	sql.append(" LEFT JOIN com_project_info s on t.project_id = s.id     ");
        }*/
        sql.append(" WHERE t.task_id = '"+taskId+"'  ");

    	if(!nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS) && !"report".equals(nowOrhisTag)){
    		if(!StringUtil.isAllNullOrEmpty(pubtaskID)){
                sql.append(" AND t.pubtask_id = '"+pubtaskID+"'  ");
            }
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
        	sql.append(" and (p.bundled_parent_id = '' or p.bundled_parent_id is null)");
        	
            if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and p.k_status in (1,2) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id = '"+user.getDepartment().getDepartmentAreaId()+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and p.k_status in (3,4) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    //sql.append(" AND p.receive_dep_id != p.create_department_guid ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                    
                    /*if("1".equals(taskpubType)){
                        //关联自己的任务。
//                        SQL.append(" and PT.dept_id = ? ");
//                        listValues.add(Dept.getDepartmentGuid());
                    	sql.append(" and t.sc_level = 1 ");
                        
                	}else if("2".equals(taskpubType)){
                		
                    	//sql.append(" and PT.taskpub_type = 1 ");
                		sql.append(" and t.s_level = 1 ");
                		sql.append(" and t.sc_level != 1 ");
                		
                		StringBuilder sql0 = new StringBuilder();
                		StringBuilder sql1 = new StringBuilder();
                		sql0.append("select id from com_paln_task_pub where taskpub_type=2 and dept_id='"+user.getDepartment().getDepartmentGuid()+"' and task_id='"+taskId+"'");
                		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                		list = this.dao.findBySql(sql0.toString());
                		List<String> list1 = new ArrayList<>();
                		List<String> pubid = new ArrayList<>();
                		for(Map<String, Object> map : list){
                			String fid = map.get("id").toString();
                			list1.add(fid);
                			pubid.add(fid);
                		}
                		
                		if(list1.size() >0){
                			for(String id : list1){
                    			sql1.append("select id from com_paln_task_pub where taskpub_type=3 and task_id='"+taskId+"' and pub_fid='"+id+"'");
                    			List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
                        		listmap = this.dao.findBySql(sql1.toString());
                        		for(Map<String, Object> map : listmap){
                        			String fid = map.get("id").toString();
                        			pubid.add(fid);
                        		}
                    		}
                		}
                		
                		sql.append(" and t.pubtask_id in (");
                		for(int i=0;i<pubid.size();i++){
                			String id = pubid.get(i);
                			sql.append("'"+id+"'");
                			if(i < pubid.size()-1){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                		sql.append(" and P.create_department_guid != '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                	}else if("3".equals(taskpubType)){
                		sql.append(" and PT.dept_id = '"+user.getDepartment().getDepartmentGuid()+"'");

                        //编制区，自己查看自己的项目
                		sql.append(" and t.creatd_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                		sql.append(" AND SC.create_dept_id = '"+user.getDepartment().getDepartmentGuid()+"' ");
                        
                	}*/
                    
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and p.k_status = 8 ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals("report")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append("  and p.k_status in(3,8,9) ");
                	String SQL = "";
                	if(ttype.equals(GDMajorConstants.ttype_1)){
                		//获取上一级部门
                		SQL="select distinct relate_bill_id from s_to_deal_log where type=3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
                		SQL="select distinct relate_bill_id from s_year_to_deal_log where type = 3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                    }
                	List<Map<String,Object>> proList = this.dao.findBySql(SQL.toString());
                	
                	
                	if(proList != null && proList.size()>0){
                		sql.append(" and p.id in (");
                		int i=0;
                		for(Map<String,Object> map : proList){
                			i++;
                			sql.append("'"+map.get("relate_bill_id")+"'");
                			if(i<proList.size()){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                	}
                	
                }
                if(nowOrhisTag.equals("advice")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append(" and SPTI.DEPT_ID = '"+user.getDepartment().getDepartmentGuid()+"' ");
                }

            }else{
            	sql.append(" AND p.k_status != 2  ");
            }
        }

        List<Map<String, Object>> list=this.dao.findBySql(sql.toString());
        
        String num = "0";
        num = list.get(0).get("num").toString();
        
        return num;
    }
    /**
     * 
     */
	@Override
	public List<Map<String, Object>> getAllProject(User user, String taskId,String pubtaskID,String tableName,String nowOrhisTag, String taskpubType, String ttype) throws Exception {
		StringBuilder sql = new StringBuilder();
        if(StringUtil.isAllNullOrEmpty(taskId)){
            throw new Exception("任务ID(taskId)为空，请检查");
        }

        sql.append("select sum(t.project_total_fund) total  ");
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" ,sum(p.gj_investment_total) money ");
        }
        sql.append(" FROM com_paln_task_project t ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task SC ");
        sql.append(" ON");
        sql.append("    T.task_id=SC.ID ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task_pub PT ");
        sql.append(" ON");
        sql.append("    T.pubtask_id=PT.ID ");
        if(nowOrhisTag.equals("advice")){
        	sql.append(" LEFT JOIN");
            sql.append(" 	com_paln_task_IDEA SPTI");
            sql.append(" ON SPTI.PEOJECT_ID = t.PROJECT_ID");
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" LEFT JOIN "+tableName+" p on t.project_id = p.id     ");
        }/*else{
        	sql.append(" LEFT JOIN s_project_info s on t.project_id = s.id     ");
        }*/
        sql.append(" WHERE t.task_id = '"+taskId+"'  ");

    	if(!nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS) && !"report".equals(nowOrhisTag)){
    		if(!StringUtil.isAllNullOrEmpty(pubtaskID)){
                sql.append(" AND t.pubtask_id = '"+pubtaskID+"'  ");
            }
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
        	
        	sql.append(" and p.type != 2 ");
        	
            if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and p.k_status in (1,2) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id = '"+user.getDepartment().getDepartmentAreaId()+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and p.k_status in (3,4) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    //sql.append(" AND p.receive_dep_id != p.create_department_guid ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                    
                    /*if("1".equals(taskpubType)){
                        //关联自己的任务。
//                        SQL.append(" and PT.dept_id = ? ");
//                        listValues.add(Dept.getDepartmentGuid());
                        sql.append(" and t.sc_level = 1 ");
                        
                	}if("2".equals(taskpubType)){
                		
                    	//sql.append(" and PT.taskpub_type = 1 ");
                    	sql.append("  and t.s_level = 1 ");
                    	sql.append(" and t.sc_level != 1 ");
                		
                		StringBuilder sql0 = new StringBuilder();
                		StringBuilder sql1 = new StringBuilder();
                		sql0.append("select id from com_paln_task_pub where taskpub_type=2 and dept_id='"+user.getDepartment().getDepartmentGuid()+"' and task_id='"+taskId+"'");
                		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                		list = this.dao.findBySql(sql0.toString());
                		List<String> list1 = new ArrayList<>();
                		List<String> pubid = new ArrayList<>();
                		for(Map<String, Object> map : list){
                			String fid = map.get("id").toString();
                			list1.add(fid);
                			pubid.add(fid);
                		}
                		
                		if(list1.size() >0){
                			for(String id : list1){
                    			sql1.append("select id from com_paln_task_pub where taskpub_type=3 and task_id='"+taskId+"' and pub_fid='"+id+"'");
                    			List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
                        		listmap = this.dao.findBySql(sql1.toString());
                        		for(Map<String, Object> map : listmap){
                        			String fid = map.get("id").toString();
                        			pubid.add(fid);
                        		}
                    		}
                		}
                		
                		sql.append(" and t.pubtask_id in (");
                		for(int i=0;i<pubid.size();i++){
                			String id = pubid.get(i);
                			sql.append("'"+id+"'");
                			if(i < pubid.size()-1){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                		sql.append(" and P.create_department_guid != '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                	}else if("3".equals(taskpubType)){
                		sql.append(" and PT.dept_id = '"+user.getDepartment().getDepartmentGuid()+"'");

                        //编制区，自己查看自己的项目
                		sql.append(" and t.creatd_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                		sql.append(" AND SC.create_dept_id = '"+user.getDepartment().getDepartmentGuid()+"' ");
                        
                	}*/
                    
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and p.k_status = 8 ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                //已报区
                if(nowOrhisTag.equals("report")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append("  and p.k_status in(3,4,8,9) ");
                	String SQL = "";
                	if(ttype.equals(GDMajorConstants.ttype_1)){
                		//获取上一级部门
                		SQL="select distinct relate_bill_id from s_to_deal_log where type=3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
                		SQL="select distinct relate_bill_id from s_year_to_deal_log where type = 3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                    }
                	List<Map<String,Object>> proList = this.dao.findBySql(SQL.toString());
                	
                	if(proList != null && proList.size()>0){
                		sql.append(" and p.id in (");
                		int i=0;
                		for(Map<String,Object> map : proList){
                			i++;
                			sql.append("'"+map.get("relate_bill_id")+"'");
                			if(i<proList.size()){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                	}
                	
                }
                if(nowOrhisTag.equals("advice")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append(" and SPTI.DEPT_ID = '"+user.getDepartment().getDepartmentGuid()+"' ");
                }

            }else{
            	sql.append(" AND p.k_status != 2  ");
            }
        }/*else{
        	if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and s.status in (1,2) ");
                }
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and s.status = 3 ");
                }
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and s.status = 8 ");
                }

            }
        }*/
        

        List<Map<String, Object>> list=this.dao.findBySql(sql.toString());
        
        String num = getAllProjectNum(user,taskId,pubtaskID,tableName,nowOrhisTag,taskpubType,ttype);
        
        list.get(0).put("num", num);
        
        return list;
	}

	
	public String getAllProjectNum(User user, String taskId,String pubtaskID,String tableName,String nowOrhisTag, String taskpubType, String ttype) throws Exception {
		StringBuilder sql = new StringBuilder();
        if(StringUtil.isAllNullOrEmpty(taskId)){
            throw new Exception("任务ID(taskId)为空，请检查");
        }

        sql.append("select sum(t.project_total_fund) total,count(t.id) num  ");
        sql.append(" FROM com_paln_task_project t ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task SC ");
        sql.append(" ON");
        sql.append("    T.task_id=SC.ID ");
        sql.append(" LEFT JOIN");
        sql.append("     com_paln_task_pub PT ");
        sql.append(" ON");
        sql.append("    T.pubtask_id=PT.ID ");
        if(nowOrhisTag.equals("advice")){
        	sql.append(" LEFT JOIN");
            sql.append(" 	com_paln_task_IDEA SPTI");
            sql.append(" ON SPTI.PEOJECT_ID = t.PROJECT_ID");
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
            sql.append(" LEFT JOIN "+tableName+" p on t.project_id = p.id     ");
        }/*else{
        	sql.append(" LEFT JOIN s_project_info s on t.project_id = s.id     ");
        }*/
        sql.append(" WHERE t.task_id = '"+taskId+"'  ");

    	if(!nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS) && !"report".equals(nowOrhisTag)){
    		if(!StringUtil.isAllNullOrEmpty(pubtaskID)){
                sql.append(" AND t.pubtask_id = '"+pubtaskID+"'  ");
            }
    	}
        
        if(!StringUtil.isAllNullOrEmpty(tableName)){
        	
        	sql.append(" and (p.bundled_parent_id = '' or p.bundled_parent_id is null) ");
        	
            if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and p.k_status in (1,2) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id = '"+user.getDepartment().getDepartmentAreaId()+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and p.k_status in (3,4) ");
                    sql.append("  AND PT.t_status IN (0,1,2,3) ");
                    sql.append(" and p.k_receive_dep_id ='"+user.getDepartment().getDepartmentGuid()+"'");
                    //sql.append(" AND p.receive_dep_id != p.create_department_guid ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                    
                    /*if("1".equals(taskpubType)){
                        //关联自己的任务。
//                        SQL.append(" and PT.dept_id = ? ");
//                        listValues.add(Dept.getDepartmentGuid());
                        sql.append(" and t.sc_level = 1 ");
                        
                	}if("2".equals(taskpubType)){
                		
                    	//sql.append(" and PT.taskpub_type = 1 ");
                    	sql.append("  and t.s_level = 1 ");
                    	sql.append(" and t.sc_level != 1 ");
                		
                		StringBuilder sql0 = new StringBuilder();
                		StringBuilder sql1 = new StringBuilder();
                		sql0.append("select id from com_paln_task_pub where taskpub_type=2 and dept_id='"+user.getDepartment().getDepartmentGuid()+"' and task_id='"+taskId+"'");
                		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                		list = this.dao.findBySql(sql0.toString());
                		List<String> list1 = new ArrayList<>();
                		List<String> pubid = new ArrayList<>();
                		for(Map<String, Object> map : list){
                			String fid = map.get("id").toString();
                			list1.add(fid);
                			pubid.add(fid);
                		}
                		
                		if(list1.size() >0){
                			for(String id : list1){
                    			sql1.append("select id from com_paln_task_pub where taskpub_type=3 and task_id='"+taskId+"' and pub_fid='"+id+"'");
                    			List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
                        		listmap = this.dao.findBySql(sql1.toString());
                        		for(Map<String, Object> map : listmap){
                        			String fid = map.get("id").toString();
                        			pubid.add(fid);
                        		}
                    		}
                		}
                		
                		sql.append(" and t.pubtask_id in (");
                		for(int i=0;i<pubid.size();i++){
                			String id = pubid.get(i);
                			sql.append("'"+id+"'");
                			if(i < pubid.size()-1){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                		sql.append(" and P.create_department_guid != '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                	}else if("3".equals(taskpubType)){
                		sql.append(" and PT.dept_id = '"+user.getDepartment().getDepartmentGuid()+"'");

                        //编制区，自己查看自己的项目
                		sql.append(" and t.creatd_department_guid = '"+user.getDepartment().getDepartmentGuid()+"' ");
                		
                		sql.append(" AND SC.create_dept_id = '"+user.getDepartment().getDepartmentGuid()+"' ");
                        
                	}*/
                    
                }
                if(nowOrhisTag.equals(GDMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and p.k_status = 8 ");
                    /*if(user.getDepartment().getStoreLevel() == 1){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.qx_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 2){
                    	sql.append(" AND t.area_id like '"+user.getDepartment().getDepartmentAreaId().substring(0,4)+"%' ");
                    	sql.append("  and t.s_level = 1 ");
                    }else if(user.getDepartment().getStoreLevel() == 3){
                    	sql.append("  and t.sc_level = 1 ");
                    }*/
                }
                if(nowOrhisTag.equals("report")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append("  and p.k_status in(3,4,8,9) ");
                	String SQL = "";
                	if(ttype.equals(GDMajorConstants.ttype_1)){
                		//获取上一级部门
                		SQL="select distinct relate_bill_id from s_to_deal_log where type=3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                	}else if(ttype.equals(GDMajorConstants.ttype_3)){
                		SQL="select distinct relate_bill_id from s_year_to_deal_log where type = 3 and cur_department_guid='"+user.getDepartment().getDepartmentGuid()+"'";
                    }
                	List<Map<String,Object>> proList = this.dao.findBySql(SQL.toString());
                	
                	
                	if(proList != null && proList.size()>0){
                		sql.append(" and p.id in (");
                		int i=0;
                		for(Map<String,Object> map : proList){
                			i++;
                			sql.append("'"+map.get("relate_bill_id")+"'");
                			if(i<proList.size()){
                				sql.append(",");
                			}
                		}
                		sql.append(") ");
                	}
                	
                }
                if(nowOrhisTag.equals("advice")){
                	//sql.append(" and PT.taskpub_type = 1 ");
                	sql.append(" and SPTI.DEPT_ID = '"+user.getDepartment().getDepartmentGuid()+"' ");
                }

            }else{
            	sql.append(" AND p.k_status != 2  ");
            }
        }/*else{
        	if(!StringUtil.isAllNullOrEmpty(nowOrhisTag)){
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_NOW)){
                    sql.append("  and s.status in (1,2) ");
                }
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_CHECK)){
                    sql.append("  and s.status = 3 ");
                }
                if(nowOrhisTag.equals(SCMajorConstants.TAG_PLAN_HIS)){
                    sql.append("  and s.status = 8 ");
                }

            }
        }*/
        

        List<Map<String, Object>> list=this.dao.findBySql(sql.toString());
        
        String num="0";
        
        num = list.get(0).get("num").toString();
        
        return num;
	}
	@Override
	public List<Map<String, Object>> getTaskNameSelection(
			Map<String, String> params) {
		List<Map<String, Object>> result = new ArrayList<>();
		String ttype = params.get("ttype");
        //sql语句
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT st.task_name ");
        sql.append("FROM com_paln_task_pub t ");
        sql.append("LEFT JOIN com_paln_task st ON t.task_id = st.id ");
        // where条件<任务下发区，查询自己创建的任务。>
        sql.append(" WHERE t.taskpub_type = 1 ");
        sql.append(" AND t.t_status NOT IN ('5')");
        sql.append(" AND st.ttype= "+ttype+" ");
        sql.append(" AND t.dept_id= '"+params.get("deptId")+"' ");

        //排序
        sql.append(" ORDER BY st.task_year desc ");
        result = this.dao.findBySql(sql.toString());
		return result;
	}
}

