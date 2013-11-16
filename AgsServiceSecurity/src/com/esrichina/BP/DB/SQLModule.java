package com.esrichina.BP.DB;

public class SQLModule {
	
	public static String queryAllUsersSQL = "SELECT USERNAME, CREATED, DEFAULT_TABLESPACE"+
											" FROM DBA_USERS D"+
											" WHERE USERNAME NOT IN"+
											" (SELECT NAME"+
													" FROM (SELECT SCHEMA#"+
															" FROM SYS.REGISTRY$"+
															" UNION ALL"+
															" SELECT B.SCHEMA#"+
															" FROM SYS.REGISTRY$ A, SYS.REGISTRY$SCHEMAS B"+
															" WHERE A.CID = B.CID) A,"+
															" SYS.USER$ B"+
															" WHERE A.SCHEMA# = B.USER#)"+
											" AND D.default_tablespace NOT IN('SYSAUX','SYSTEM')";
	
	public static String queryAllRolesSQL = "select * from dba_role_privs where grantee='?'";
	
	public static String createTableSpaceSQL = "";
	
	public static String createUserSQL = "CREATE USER ? IDENTIFIED BY ? DEFAULT TABLESPACE ? TABLESPACE ?";

	public static String grantUserAuthoritySQL = "GRANT connect, resource, create session TO ?;";
	
	public static String grantDBMSRlsExecSQL = "GRANT EXECUTE ON DBMS_RLS TO ?; ";
	
	public static String createViewSQL = "CREATE OR REPLACE VIEW ?_view as select * from ?";
	
	public static String createRoleSQL = "CREATE ROLE ? NOT IDENTIFIED;";
	
	public static String grantRoleAuthoritySQL = "GRANT SELECT ON ? TO ?";
	
	public static String createRowSecurityFunctionSQL = "create or replace function ags_RowSecurity("
													+"P_Schema In Varchar2,"
													+"P_Object  In Varchar2)"
													+"return Varchar2 as "
													+"v_res Varchar2(1000); "
													+"begin "
													+"select ? into v_res from ? where upper(?)=sys_context('userenv','current_user');"
													+"return v_res;"
													+"end ags_RowSecurity;";

	public static String createColSecurityFunctionSQL = "create or replace function ags_ColSecurity(" +
													"P_Schema In Varchar2," +
													"P_Object  In Varchar2)" +
													"return varchar2 is " +
													"Result varchar2(1000);" +
													"begin " +
													"  IF (SYS_CONTEXT('USERENV','SESSION_USER') in ('?')) THEN" +
													"    Result := NULL;" +
													"  ELSE" +
													"    Result := '1=2';" +
													"  END IF;" +
													"  return(Result);" +
													"end ags_ColSecurity;";
	
	public static String addRowSecruityPolicySQL = "declare "
													+"begin "
													+"dbms_rls.add_policy("
													+"object_schema =>'?',"
													+"object_name =>'?',"
													+"policy_name =>'AGSROWPOLICY',"
													+"function_schema =>'?',"
													+"policy_function =>'ags_RowSecurity');" 
													+"end;";
	
	public static String addColSecurityPolicySQL = "declare "
													+"begin "
													+"dbms_rls.add_policy("
													+"object_schema =>'?',"
													+"object_name =>'?',"
													+"policy_name =>'AGSCOLPOLICY',"
													+"function_schema =>'?',"
													+"policy_function =>'ags_ColSecurity'," 
													+"sec_relevant_cols => '?'," 
													+"sec_relevant_cols_opt => Dbms_Rls.ALL_ROWS);"
													+"end;";
	
	public static String dropPolicySQL = "DECLARE" +
									"	BEGIN" +
									"	  Dbms_Rls.drop_policy('?', " +  //要删除的Policy所在的Schema
									"	                       '?'," +    //要删除Policy的数据表(或视图)名称
									"	                       '?' " +    //要删除的Policy名称
									"	                       );" +
									"	end;";
	
}
