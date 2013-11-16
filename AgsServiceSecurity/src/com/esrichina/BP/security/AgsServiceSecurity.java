package com.esrichina.BP.security;

import com.esrichina.BP.request.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AgsServiceSecurity {

	public static JSONObject getResourceSecurity(String ServiceUrl, String token){
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		JSONObject resultObj = new JSONObject();
		String result = HttpRequest.postRequest(ServiceUrl+"/permissions", params.toString(), "POST");
		resultObj = JSONObject.fromObject(result);
		return resultObj;
	}
	
	private static void changePermission(String url, String token, String role, boolean add){
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		params.accumulate("principal", role);
		params.accumulate("isAllowed", add);
		HttpRequest.postRequest(url+"/permissions/add", params.toString(), "POST");
	}
	
	private static void addEsriEveryone(String url, String token){
		changePermission(url, token, "esriEveryone", true);
	}
	
	private static void delEsriEveryone(String url, String token){
		changePermission(url, token, "esriEveryone", false);
	}
	
	public static void addPermission(String url, String token, String role){
		changePermission(url, token, role, true);
		delEsriEveryone(url,token);
	}
	
	public static void delPermission(String url, String token, String role){
		changePermission(url, token, role, false);
		JSONObject perm = getResourceSecurity(url, token);
		if(((JSONArray)perm.get("permissions")).size()==0) addEsriEveryone(url, token);
	}
}
