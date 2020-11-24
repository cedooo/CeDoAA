<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameFirstLower = className?uncap_first>
<#assign classNameLowerCase = className?lower_case>
<#assign pkJavaType = table.idColumn.javaType>
package ${basepackage}.dao.${parentModule}.${classNameLowerCase}.inter;

import com.strongit.iss.entity.Department;
import com.strongit.iss.entity.InvestProject;
import com.strongit.iss.entity.User;
import com.strongit.iss.entity.sc.ComPalnTask;
import com.strongit.iss.entity.sc.ComPalnTaskPub;
import com.strongit.iss.hibernate.Page;
import com.strongit.iss.hibernate.QueryParameter;

import java.util.List;
import java.util.Map;


/**
 <#include "/java_description.include">
 */
public interface I${className}Dao {



    /**
     　* @Description: 查询
     　* @param
     　* @return
     　* @author cedo
     　* @date ${now?string('yyyy-MM-dd HH:mm')}
     　*/
    Page<Map<String, Object>> getScPalnTaskPageByCondition(Map<String, String> params, QueryParameter queryParameter) throws Exception;
    
    /**
    　* @Description: 根据分类编码和部门id查询任务
    　* @author cedo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}
    　*/
    List<Map<String, Object>> getTypeTaskList(String typecode, String deptId,String ttype,String taskId,Map<String, String> params)throws Exception;
    
    /**
   　* @Description: 根据分类编码和部门id查询任务
   　* @author cedo
   　* @date ${now?string('yyyy-MM-dd HH:mm')}
   　
     * @return */
    void saveScPalnTaskPub(ComPalnTaskPub scPalnTaskPub)throws Exception;

    /**
     * 根据id查询
     * @param id
     * @return
     * @throws Exception
     */
    ComPalnTask findByID(String id) throws Exception;

    /**
    　* @Description: 查询分类项目信息
    　* @author cedo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}
    　*/
    Page<Map<String, Object>> getTaskProjectPage(Map<String, String> params, QueryParameter queryParameter,Department department) throws Exception;


    /**
    　* @Description: 查询任务下的项目信息
    　* @author cedo
    　* @date ${now?string('yyyy-MM-dd HH:mm')}
    　
     * @param user 
     * @param params */
    List<Map<String, Object>> getProjectMsg(User user, Map<String, String> params, String taskId, String loginDeptID)throws Exception;
    
    
    /**
   　* @Description: 查询任务下所有项目
   　* @author leixiao
   　* @date ${now?string('yyyy-MM-dd HH:mm')}
   　
     * @param user 
     * @param nowOrhisTag 
     * @param taskpubType 
     * @param ttype */
   List<Map<String, Object>> getAllProject(User user, String taskId,String pubtaskID,String tableName, String nowOrhisTag, String taskpubType, String ttype)throws Exception;
	/**
	 * 查询任务名称下拉选项
	 */
	List<Map<String, Object>> getTaskNameSelection(Map<String, String> params);

	ComPalnTaskPub findByid(String id) throws Exception;

	InvestProject findProByID(String id) throws Exception;

	Page<Map<String, Object>> getProjectPageByCondition(Map<String, String> params, QueryParameter queryParameter) throws Exception;
}
