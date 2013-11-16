package com.esrichina.BP.Tree;

import java.util.List;

public class TreeNodeBeanBase {
	
	String id;
	String url;
	String name;
	List<String> allowRoles;
	String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getAllowRoles() {
		return allowRoles;
	}

	public void setAllowRoles(List<String> allowRoles) {
		this.allowRoles = allowRoles;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
