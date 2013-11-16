package com.esrichina.BP.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBCon {

	Connection con = null;
	
	String sql_server = "ShiBin";
	String sql_port = "1433";
	String sql_database = "EPN";
	String sql_user = "support";
	String sql_password = "support";
	
	String pq_server = "127.0.0.1";
	String pq_port = "5432";
	String pq_database = "support";
	String pq_user = "postgres";
	String pq_password = "postgres";
	
	public Connection createOracleConnect(String ora_server, String ora_port, String ora_instance,
			String ora_user, String ora_password){
		String url = "jdbc:oracle:thin:@" + ora_server + ":" + ora_port + ":" + ora_instance ;
		try {
		    Class.forName("oracle.jdbc.OracleDriver");
		    con = DriverManager.getConnection(url, ora_user, ora_password);
		    System.out.println("Connected!");
		} catch (SQLException e) {
		    System.out.println("SQL Exception: "+ e.toString());
		} catch (ClassNotFoundException cE) {
		    System.out.println("Class Not Found Exception: "+ cE.toString());
		}
		return con;
	}
	
	public Connection createSqlServerConnect(){
		String url = "jdbc:sqlserver://"+sql_server+":"+sql_port+";DatabaseName=" + sql_database + ";user="+sql_user+";password="+sql_password;
		try {
		    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		    con = DriverManager.getConnection(url);
		    System.out.println("Connected!");
		} catch (SQLException e) {
		    System.out.println("SQL Exception: "+ e.toString());
		} catch (ClassNotFoundException cE) {
		    System.out.println("Class Not Found Exception: "+ cE.toString());
		}
		return con;
	}
	
	
	public Connection createPQServerConnect(){
		String url = "jdbc:postgresql://"+pq_server+":"+pq_port+"/" + pq_database;
		try {
		    Class.forName("org.postgresql.Driver");
		    con = DriverManager.getConnection(url, "postgres", "postgres");
		    System.out.println("Connected!");
		} catch (SQLException e) {
		    System.out.println("SQL Exception: "+ e.toString());
		} catch (ClassNotFoundException cE) {
		    System.out.println("Class Not Found Exception: "+ cE.toString());
		}
		return con;
	}
	
	public void close(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
