package com.esrichina.BP.Tree;

public class RootTreeNodeBeanImpl extends TreeNodeBeanBase{
	
	
	String adminUser;
	String adminPassword;
	String user;
	String password;
	DatasourceBase ds;
	
	public DatasourceBase getDs() {
		return ds;
	}
	public void setDs(DatasourceBase ds) {
		this.ds = ds;
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
	
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	
}
