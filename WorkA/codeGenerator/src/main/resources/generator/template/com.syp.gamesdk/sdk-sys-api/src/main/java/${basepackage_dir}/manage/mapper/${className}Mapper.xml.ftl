<#include "/macro.include"/>
<#assign className = table.className>   
<#assign classNameFirstLower = table.classNameFirstLower>
<#assign classNameLowerCase = className?lower_case>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">

<#macro mapperEl value>${r"#{"}${value}}</#macro>
<#macro namespace>${className}</#macro>

<!-- 不使用namespace的话sql搜索定位会比较方便 -->
<mapper namespace="${basepackage}.manage.mapper.${className}Mapper">

	<resultMap id="RM${className}" type="${basepackage}.manage.pojo.${className}">
        <#list table.columns as column>
        <result property="${column.columnNameLower}" column="${column.sqlName}"/>
		</#list>
	</resultMap>
	
	<!-- 用于select查询公用抽取的列 -->
	<sql id="<@namespace/>columns">
	    <![CDATA[
		<#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list>
	    ]]>
	</sql>

	<!-- useGeneratedKeys="true" keyProperty="xxx" for sqlserver and mysql -->
	<insert id="insert" useGeneratedKeys="true" keyProperty="${table.idColumn.columnNameFirstLower}">
    <![CDATA[
        INSERT INTO ${table.sqlName} (
        <#list table.columns as column> ${column.sqlName}<#if column_has_next>,</#if></#list>
        ) VALUES (
        <#list table.columns as column> <@mapperEl column.columnNameFirstLower/><#if column_has_next>,</#if></#list>        
        )
    ]]>
	</insert>

<#if 'java.lang.Long'==table.idColumn.javaType>
<#elseif 'java.lang.String'==table.idColumn.javaType>
    <insert id="insertOrUpdate" useGeneratedKeys="true" keyProperty="${table.idColumn.columnNameFirstLower}">
        INSERT INTO ${table.sqlName} (
        <#list table.columns as column> ${column.sqlName}<#if column_has_next>,</#if></#list>
        ) VALUES (
        <#list table.columns as column> <@mapperEl column.columnNameFirstLower/><#if column_has_next>,</#if></#list>
        )
        ON DUPLICATE KEY
        UPDATE
        <trim suffixOverrides=",">
        <#list table.notPkColumns as column>
        <#if column.columnNameFirstLower!='createdTime'>
            <if test="@Ognl@isNotEmpty(${column.columnNameFirstLower})">
            ${column.sqlName} = <@mapperEl column.columnNameFirstLower/><#if column_has_next>,</#if>
            </if>
        </#if>
        </#list>
        </trim>
    </insert>
</#if>

	<update id="update" >
        UPDATE ${table.sqlName}
        <set>
	    <#list table.notPkColumns as column>
			<if test="@Ognl@isNotEmpty(${column.columnNameFirstLower})">
	        ${column.sqlName} = <@mapperEl column.columnNameFirstLower/><#if column_has_next>,</#if>
			</if>
	    </#list>
	    </set>
        WHERE 
        	<#list table.compositeIdColumns as column>${column.sqlName} = <@mapperEl column.columnNameLower/> <#if column_has_next> AND </#if> </#list>
	</update>

    <delete id="delete">
    <![CDATA[
        DELETE FROM ${table.sqlName} WHERE
        <#list table.compositeIdColumns as column>
        ${column.sqlName} = <@mapperEl 'id'/> <#if column_has_next> AND </#if>
		</#list>
    ]]>
    </delete>


	<update id="deleteWithTag">
		<![CDATA[
		UPDATE ${table.sqlName}
		SET DEL_TAG = '1'
		WHERE
			${table.idColumn.sqlName} = <@mapperEl 'id'/>
		]]>
	</update>
    
    <select id="getByKey" resultMap="RM${className}">
		SELECT <include refid="<@namespace/>columns" />
	    <![CDATA[
		    FROM ${table.sqlName} 
	        WHERE 
				<#list table.compositeIdColumns as column>
		        ${column.sqlName} = <@mapperEl column.columnNameFirstLower/> <#if column_has_next> AND </#if>
		        </#list>    
	    ]]>
	</select>
	
	<sql id="<@namespace/>findPageWhere">
		<!-- ognl访问静态方法的表达式 为@class@method(args),以下为调用rapid中的Ognl.isNotEmpty()方法,还有其它方法如isNotBlank()可以使用，具体请查看Ognl类 -->
		<where>	      				
	       <#list table.columns as column>
	       <#if column.isDateTimeColumn>
	       <if test="@Ognl@isNotEmpty(${column.columnNameFirstLower}Begin)">
				AND ${column.sqlName} >= <@mapperEl column.columnNameFirstLower+"Begin"/>
		   </if>
		   <if test="@Ognl@isNotEmpty(${column.columnNameFirstLower}End)">
				AND ${column.sqlName} &lt;= <@mapperEl column.columnNameFirstLower+"End"/>
		   </if>
	       <#else>
	       <if test="@Ognl@isNotEmpty(${column.columnNameFirstLower})">
				AND ${column.sqlName} = <@mapperEl column.columnNameFirstLower/>
			</if>
	       </#if>
	       </#list>			
		</where>
	</sql>
		
    <select id="findPageCount" resultType="int">
        SELECT count(*) FROM ${table.sqlName} 
		<include refid="<@namespace/>findPageWhere"/>
    </select>
    
    <!--
    	分页查询已经使用Dialect进行分页,也可以不使用Dialect直接编写分页
    	因为分页查询将传 offset,pageSize,lastRows 三个参数,不同的数据库可以根于此三个参数属性应用不同的分页实现
    -->
    <select id="findPage" resultMap="RM${className}">
    	SELECT <include refid="<@namespace/>columns" />
	    FROM ${table.sqlName} 
		<include refid="<@namespace/>findPageWhere"/>
		
		<if test="@Ognl@isNotEmpty(sortColumns)">
			ORDER BY <@jspEl 'sortColumns'/>
		</if>
    </select>

    <#list table.columns as column>
    <#if column.unique && !column.pk>
    <select id="getBy${column.columnName}" resultMap="RM${className}" parameterType="${column.javaType}">
	    SELECT <include refid="<@namespace/>columns"/>
	    FROM ${table.sqlName} where ${column.sqlName} = <@mapperEl column.columnNameLower/>
    </select>
    </#if>
	</#list>
	
</mapper>

