package com.esrichina.BP.Tree;

public class OracleDataSourceImpl extends DatasourceBase{
	
	String server;
	String port;
	String instance;
	String user;
	String password;
	
	public String getServer() {
		return server;
	}


	public void setServer(String server) {
		this.server = server;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getInstance() {
		return instance;
	}


	public void setInstance(String instance) {
		this.instance = instance;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	
	
	
	public OracleDataSourceImpl(){
		this.type = DatasourceType.OracleType;
	}
}