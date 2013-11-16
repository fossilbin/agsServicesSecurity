package com.esrichina.BP.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;

public class OracleManager {

	private static Connection con = null;
	
	public OracleManager(Connection conn){
		if(con==null) con = conn;
	}
	
	public JSONArray getAllUsers(){
		JSONArray allusers = new JSONArray();
		try {
			Statement stmt = con.createStatement();
			System.out.println(SQLModule.queryAllUsersSQL);
			ResultSet rs = stmt.executeQuery(SQLModule.queryAllUsersSQL);
			while(rs.next()){
				allusers.add(rs.getString("USERNAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allusers;
	}
	
	public JSONArray getRolesOfUser(String user){
		JSONArray allroles = new JSONArray();
		try {
			Statement stmt = con.createStatement();
			String sql = SQLModule.queryAllRolesSQL.replace("?", user.toUpperCase());
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				allroles.add(rs.getString("GRANTED_ROLE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allroles;
	}
}
