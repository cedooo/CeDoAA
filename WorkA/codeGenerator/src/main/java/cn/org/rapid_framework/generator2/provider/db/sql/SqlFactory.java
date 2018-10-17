package cn.org.rapid_framework.generator2.provider.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.generator2.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator2.util.StringHelper;
import cn.org.rapid_framework.generator2.util.sqlparse.NamedParameterUtils;
import cn.org.rapid_framework.generator2.util.sqlparse.ParsedSql;
import cn.org.rapid_framework.generator2.util.sqlparse.ResultSetMetaDataHolder;
import cn.org.rapid_framework.generator2.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator2.util.sqlparse.SqlParseHelper.NameWithAlias;
import cn.org.rapid_framework.generator2.util.typemapping.JdbcType;
/**
 * 
 * 根据SQL语句生成Sql对象,用于代码生成器的生成<br />
 * 
 * 示例使用：
 * <pre>
 * Sql sql = new SqlFactory().parseSql("select * from user_info where username=#username# and password=#password#");
 * </pre>
 * 
 * @author badqiu
 *
 */
public class SqlFactory {
    
    private List<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter> customParameters = new ArrayList<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter>();
    private List<cn.org.rapid_framework.generator2.provider.db.table.model.Column> customColumns = new ArrayList<cn.org.rapid_framework.generator2.provider.db.table.model.Column>();
    
    public SqlFactory() {
    }
    
	public SqlFactory(List<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter> customParameters, List<cn.org.rapid_framework.generator2.provider.db.table.model.Column> customColumns) {
        this.customParameters = customParameters;
        this.customColumns = customColumns;
    }

    public Sql parseSql(String sourceSql) {
    	String beforeProcessedSql = beforeParseSql(sourceSql);
    	
//    	String unscapedSourceSql = StringHelper.unescapeXml(beforeProcessedSql);
    	String namedSql = SqlParseHelper.convert2NamedParametersSql(beforeProcessedSql,":","");
        ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(namedSql);
        String executeSql = NamedParameterUtils.substituteNamedParameters(parsedSql);
        
        Sql sql = new Sql();
        sql.setSourceSql(sourceSql);
        sql.setExecuteSql(executeSql);
        cn.org.rapid_framework.generator2.util.GLogger.debug("\n*******************************");
        cn.org.rapid_framework.generator2.util.GLogger.debug("sourceSql  :"+sql.getSourceSql());
        cn.org.rapid_framework.generator2.util.GLogger.debug("namedSql  :"+namedSql);
        cn.org.rapid_framework.generator2.util.GLogger.debug("executeSql :"+sql.getExecuteSql());
        cn.org.rapid_framework.generator2.util.GLogger.debug("*********************************");
        
        Connection conn = cn.org.rapid_framework.generator2.provider.db.table.TableFactory.getInstance().getConnection();
        try {
        	conn.setReadOnly(true);
        	conn.setAutoCommit(false);
	        PreparedStatement ps = conn.prepareStatement(SqlParseHelper.removeOrders(executeSql));
	        ResultSetMetaData resultSetMetaData = executeForResultSetMetaData(executeSql,ps);
            sql.setColumns(new SelectColumnsParser().convert2Columns(sql,resultSetMetaData));
	        sql.setParams(new SqlParametersParser().parseForSqlParameters(parsedSql,sql));
	        
	        return afterProcessedSql(sql);
        }catch(Exception e) {
        	throw new RuntimeException("sql parse error,\nsourceSql:"+sourceSql+"\nnamedSql:"+namedSql+"\nexecutedSql:"+executeSql,e);
        }finally {
        	try {
	        	conn.rollback();
	        	conn.close();
        	}catch(Exception e) {
        		throw new RuntimeException(e);
        	}
        }
    }

    protected Sql afterProcessedSql(Sql sql) {
		return sql;
	}

	protected String beforeParseSql(String sourceSql) {
		return sourceSql;
	}

    private ResultSetMetaData executeForResultSetMetaData(String executeSql,PreparedStatement ps)throws SQLException {
		SqlParseHelper.setRandomParamsValueForPreparedStatement(SqlParseHelper.removeOrders(executeSql), ps);
		ps.setMaxRows(3);
        ps.setFetchSize(3);
        ps.setQueryTimeout(20);
        try {
			ResultSet rs = ps.executeQuery();
			return rs.getMetaData(); 
		} catch (Exception e) {
			return ps.getMetaData();
		}
	}
	
    public class SelectColumnsParser {
    
		private LinkedHashSet<cn.org.rapid_framework.generator2.provider.db.table.model.Column> convert2Columns(Sql sql, ResultSetMetaData metadata) throws SQLException, Exception {
			if(metadata == null) return new LinkedHashSet();
			LinkedHashSet<cn.org.rapid_framework.generator2.provider.db.table.model.Column> columns = new LinkedHashSet();
	        for(int i = 1; i <= metadata.getColumnCount(); i++) {
	        	cn.org.rapid_framework.generator2.provider.db.table.model.Column c = convert2Column(sql,metadata, i);
	        	if(c == null) throw new IllegalStateException("column must be not null");
				columns.add(c);
	        }
			return columns;
		}
	
		private cn.org.rapid_framework.generator2.provider.db.table.model.Column convert2Column(Sql sql, ResultSetMetaData metadata, int i) throws SQLException, Exception {
			ResultSetMetaDataHolder m = new ResultSetMetaDataHolder(metadata, i);
			if(StringHelper.isNotBlank(m.getTableName())) {
				//FIXME 如果表有别名,将会找不到表,如 inner join user_info t1, tableName将为t1,应该转换为user_info
				cn.org.rapid_framework.generator2.provider.db.table.model.Table table = foundTableByTableNameOrTableAlias(sql, m.getTableName());
				if(table == null) {
					return newColumn(m);
				}
			    cn.org.rapid_framework.generator2.provider.db.table.model.Column column = table.getColumnBySqlName(m.getColumnNameOrLabel());
			    if(column == null) {
			        //可以再尝试解析sql得到 column以解决 password as pwd找不到column问题
			    	//Table table, int sqlType, String sqlTypeName,String sqlName, int size, int decimalDigits, boolean isPk,boolean isNullable, boolean isIndexed, boolean isUnique,String defaultValue,String remarks
			        column = new cn.org.rapid_framework.generator2.provider.db.table.model.Column(table,m.getColumnType(),m.getColumnTypeName(),m.getColumnNameOrLabel(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
			        cn.org.rapid_framework.generator2.util.GLogger.trace("not found column:"+m.getColumnNameOrLabel()+" on table:"+table.getSqlName()+" "+ cn.org.rapid_framework.generator2.util.BeanHelper.describe(column));
			        //isInSameTable以此种判断为错误
			    }else {
			    	cn.org.rapid_framework.generator2.util.GLogger.trace("found column:"+m.getColumnNameOrLabel()+" on table:"+table.getSqlName()+" "+ cn.org.rapid_framework.generator2.util.BeanHelper.describe(column));
			    }
			    return column;
			}else {
			    return newColumn(m);
			}
		}

		private cn.org.rapid_framework.generator2.provider.db.table.model.Column newColumn(ResultSetMetaDataHolder m) {
			cn.org.rapid_framework.generator2.provider.db.table.model.Column column = new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,m.getColumnType(),m.getColumnTypeName(),m.getColumnNameOrLabel(),m.getColumnDisplaySize(),m.getScale(),false,false,false,false,null,null);
			cn.org.rapid_framework.generator2.util.GLogger.trace("not found on table by table emtpty:"+ cn.org.rapid_framework.generator2.util.BeanHelper.describe(column));
			return column;
		}

		private cn.org.rapid_framework.generator2.provider.db.table.model.Table foundTableByTableNameOrTableAlias(Sql sql, String tableNameId) throws Exception {
			try {
				return cn.org.rapid_framework.generator2.provider.db.table.TableFactory.getInstance().getTable(tableNameId);
			}catch(cn.org.rapid_framework.generator2.provider.db.table.TableFactory.NotFoundTableException e) {
				Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery(sql.getExecuteSql());
				for(NameWithAlias tableName : tableNames) {
					if(tableName.getAlias().equalsIgnoreCase(tableNameId)) {
						return cn.org.rapid_framework.generator2.provider.db.table.TableFactory.getInstance().getTable(tableName.getName());
					}
				}
			}
			return null;
		}
    }

	public class SqlParametersParser {
		Map<String, cn.org.rapid_framework.generator2.provider.db.table.model.Column> specialParametersMapping = new HashMap<String, cn.org.rapid_framework.generator2.provider.db.table.model.Column>();
		{
			specialParametersMapping.put("offset", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","offset",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("limit", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","limit",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageSize", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageSize",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageNo", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageNo",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("pageNumber", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","pageNumber",0,0,false,false,false,false,null,null));
            specialParametersMapping.put("page", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","page",0,0,false,false,false,false,null,null));
			
			specialParametersMapping.put("beginRow", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","beginRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("beginRows", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","beginRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("startRow", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","startRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("startRows", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","startRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("endRow", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","endRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("endRows", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","endRows",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("lastRow", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","lastRow",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("lastRows", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.INTEGER.TYPE_CODE,"INTEGER","lastRows",0,0,false,false,false,false,null,null));
			
			specialParametersMapping.put("orderBy", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","orderBy",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("orderby", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","orderby",0,0,false,false,false,false,null,null));
			specialParametersMapping.put("sortColumns", new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.VARCHAR.TYPE_CODE,"VARCHAR","sortColumns",0,0,false,false,false,false,null,null));
		}
		private LinkedHashSet<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter> parseForSqlParameters(ParsedSql parsedSql, Sql sql) throws Exception {
			LinkedHashSet<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter> result = new LinkedHashSet<cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter>();
			for(int i = 0; i < parsedSql.getParameterNames().size(); i++) {
				String paramName = parsedSql.getParameterNames().get(i);
				cn.org.rapid_framework.generator2.provider.db.table.model.Column column = findColumnByParamName(parsedSql, sql, paramName);
				if(column == null) {
					column = specialParametersMapping.get(paramName);
					if(column == null) {
						//FIXME 不能猜测的column类型
						column = new cn.org.rapid_framework.generator2.provider.db.table.model.Column(null,JdbcType.UNDEFINED.TYPE_CODE,"UNDEFINED",paramName,0,0,false,false,false,false,null,null);
					}
				}
				cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter param = new cn.org.rapid_framework.generator2.provider.db.sql.model.SqlParameter(column);
				
				param.setParamName(paramName);
				if(isMatchListParam(sql.getSourceSql(), paramName)) { //FIXME 只考虑(:username)未考虑(#inUsernames#) and (#{inPassword}),并且可以使用 #inUsername[]#
					param.setListParam(true);
				}
				result.add(param);			
			}
			return result;
	    
		}

		public boolean isMatchListParam(String sql, String paramName) {
			return 
			    sql.matches("(?s).*\\([:#\\$&]\\{?"+paramName+"\\}?[$#}]?\\).*") // match (:username) (#username#)
			    || sql.matches(".*[#$]"+paramName+"\\[]\\.?\\w*[#$].*"); //match #user[]# $user[]$ #user[].age#
		}
	
		private cn.org.rapid_framework.generator2.provider.db.table.model.Column findColumnByParamName(ParsedSql parsedSql, Sql sql, String paramName) throws Exception {
			cn.org.rapid_framework.generator2.provider.db.table.model.Column column = sql.getColumnByName(paramName);
			if(column == null) {
				//FIXME 还未处理 t.username = :username的t前缀问题
				column = findColumnByParseSql(parsedSql, SqlParseHelper.getColumnNameByRightCondition(parsedSql.toString(), paramName) );
			}
			if(column == null) {
				column = findColumnByParseSql(parsedSql, paramName);
			}
			return column;
		}
	
		private cn.org.rapid_framework.generator2.provider.db.table.model.Column findColumnByParseSql(ParsedSql sql, String paramName) throws Exception {
			Collection<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery(sql.toString());
			for(NameWithAlias tableName : tableNames) {
				cn.org.rapid_framework.generator2.provider.db.table.model.Table t = cn.org.rapid_framework.generator2.provider.db.table.TableFactory.getInstance().getTable(tableName.getName());
				if(t != null) {
					cn.org.rapid_framework.generator2.provider.db.table.model.Column column = t.getColumnByName(paramName);
					if(column != null) {
						return column;
					}
				}
			}
			return null;
		}
	}
    
    public static void main(String[] args) throws Exception {
    	// ? parameters
//    	SelectSqlMetaData t1 = new SqlQueryFactory().getByQuery("select * from user_info");
//    	SelectSqlMetaData t2 = new SqlQueryFactory().getByQuery("select user_info.username,password pwd from user_info where username=? and password =?");
//    	SelectSqlMetaData t3 = new SqlQueryFactory().getByQuery("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t4 = new SqlQueryFactory().getByQuery("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t5 = new SqlQueryFactory().getByQuery("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=? and password =?");
//    	SelectSqlMetaData t6 = new SqlQueryFactory().getByQuery("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=? and password =? limit ?,?");
//    	SelectSqlMetaData t7 = new SqlQueryFactory().getByQuery("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
//    
    	Sql n2 = new SqlFactory().parseSql("select user_info.username,password pwd from user_info where username=:username and password =:password");
    	Sql n3 = new SqlFactory().parseSql("select username,password,role.role_name,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n4 = new SqlFactory().parseSql("select count(*) cnt from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n5 = new SqlFactory().parseSql("select sum(age) from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password");
    	Sql n7 = new SqlFactory().parseSql("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = role.user_id group by username");
    	Sql n8 = new SqlFactory().parseSql("select username,password,count(role_desc) role_desc_cnt from user_info,role where user_info.user_id = :userId group by username");
    	new SqlFactory().parseSql("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password and birth_date between :birthDateBegin and :birthDateEnd");
    	new SqlFactory().parseSql("select username,password,role_desc from user_info,role where user_info.user_id = role.user_id and username=:username and password =:password and birth_date between :birthDateBegin and :birthDateEnd limit :offset,:limit");
    }
    
}
