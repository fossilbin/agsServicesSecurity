package com.esrichina.BP.security;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;


import com.esrichina.BP.DB.SQLModule;

public class OraSecurity {

	public static void grantUserObject(Connection con, String obj, String user){
		String sql = SQLModule.grantRoleAuthoritySQL;
		sql = sql.replaceFirst("\\?", obj.toUpperCase());
		sql = sql.replaceFirst("\\?", user.toUpperCase());
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void grantUserDBMS_RLS(Connection con, String user){
		String sql = SQLModule.grantDBMSRlsExecSQL;
		sql = sql.replaceFirst("\\?", user.toUpperCase());
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createShadowView(Connection con, String table){
		String sql = SQLModule.createViewSQL;
		sql = sql.replaceAll("\\?", table.toUpperCase());
		System.out.println(sql);
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createRowPolicyFunction(Connection con, String table, 
			String joinTable, String joinCol, String clauseCol){
		String sql = SQLModule.createRowSecurityFunctionSQL;
		sql = sql.replaceFirst("\\?", clauseCol.toUpperCase());
		sql = sql.replaceFirst("\\?", joinTable.toUpperCase());
		sql = sql.replaceFirst("\\?", joinCol.toUpperCase());
		System.out.println(sql);
		try {
			CallableStatement stmt = con.prepareCall(sql);
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addRowPolicy(Connection con, String table, 
			String schema){
		String sql = SQLModule.addRowSecruityPolicySQL;
		String tableSchema = "";
		String tableName = "";
		if(table.contains(".")){
			String[] splits = table.split("\\.");
			tableSchema = splits[0];
			tableName = splits[1];
		}else{
			try {
				tableSchema = con.getMetaData().getUserName();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tableName = table; 
		}
		sql = sql.replaceFirst("\\?", tableSchema.toUpperCase());
		sql = sql.replaceFirst("\\?", tableName.toUpperCase());
		sql = sql.replaceFirst("\\?", schema.toUpperCase());
		System.out.println(sql);
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void createColPolicyFunction(Connection con, String table, 
			String JSONusers){
		String sql = SQLModule.createColSecurityFunctionSQL;
		JSONArray userArray = JSONArray.fromObject(JSONusers);
		String userStr = "";
		for(int i=0;i<userArray.size();i++){
			userStr = userStr + userArray.getString(i) + ",";
		}
		userStr = userStr.substring(0, userStr.length()-1);
		sql = sql.replaceFirst("\\?", userStr.toUpperCase());
		System.out.println(sql);
		try {
			CallableStatement stmt = con.prepareCall(sql);
			stmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addColPolicy(Connection con, String table, 
			String schema, String JSONcols){
		
		String sql = SQLModule.addColSecurityPolicySQL;
		JSONArray colArray = JSONArray.fromObject(JSONcols);
		String colsStr = colArray.join(",");
		String tableSchema = "";
		String tableName = "";
		
		if(table.contains(".")){
			String[] splits = table.split("\\.");
			tableSchema = splits[0];
			tableName = splits[1];
		}else{
			try {
				tableSchema = con.getMetaData().getUserName();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tableName = table; 
		}
		sql = sql.replaceFirst("\\?", tableSchema.toUpperCase());
		sql = sql.replaceFirst("\\?", tableName.toUpperCase());
		sql = sql.replaceFirst("\\?", schema.toUpperCase());
		sql = sql.replaceFirst("\\?", colsStr.toUpperCase());
		System.out.println(sql);
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void dropPolicy(Connection con, String schema, String table, String policyName){
		String sql = SQLModule.dropPolicySQL;
		sql = sql.replaceFirst("\\?", schema.toUpperCase());
		sql = sql.replaceFirst("\\?", table.toUpperCase());
		sql = sql.replaceFirst("\\?", policyName.toUpperCase());
		try {
			Statement stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
