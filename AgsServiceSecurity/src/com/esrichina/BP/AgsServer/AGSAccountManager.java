package com.esrichina.BP.AgsServer;

import com.esrichina.BP.request.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AGSAccountManager {

	String securityUrl = null;
	int userSize = 200;
	String token = null;
	
	public AGSAccountManager(String securl, String tok){
		token = tok;
		securityUrl = securl;
	}
	
	public JSONArray getAllUsers(){
		JSONArray allUsers = new JSONArray();
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		params.accumulate("f", "json");
		JSONObject resultObj = new JSONObject();
		int i=0;
		int j=userSize;
		do{
			params.put("startIndex", i);
			params.put("pageSize", j);
			String result = HttpRequest.postRequest(securityUrl+"/users/getUsers", params.toString(), "POST");
			resultObj = JSONObject.fromObject(result);
			JSONArray users = new JSONArray();
			users = (JSONArray) resultObj.get("users");
			for(int k=0;k<users.size();k++){
				allUsers.add(((JSONObject)users.get(k)).get("username"));
			}
			i = i + userSize;
			j = j + userSize;
		}while(Boolean.parseBoolean(resultObj.get("hasMore").toString()));
		return allUsers;
	}
	
	public JSONArray getAllRoles(){
		JSONArray allRoles = new JSONArray();
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		params.accumulate("f", "json");
		JSONObject resultObj = new JSONObject();
		int i=0;
		int j=userSize;
		do{
			params.put("startIndex", i);
			params.put("pageSize", j);
			String result = HttpRequest.postRequest(securityUrl+"/roles/getRoles", params.toString(), "POST");
			resultObj = JSONObject.fromObject(result);
			JSONArray roles = new JSONArray();
			roles = (JSONArray) resultObj.get("roles");
			for(int k=0;k<roles.size();k++){
				allRoles.add(((JSONObject)roles.get(k)).get("rolename"));
			}
			
			i = i + userSize;
			j = j + userSize;
		}while(Boolean.parseBoolean(resultObj.get("hasMore").toString()));
		return allRoles;
	}
}
