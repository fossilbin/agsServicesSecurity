package com.esrichina.BP.security;

import net.sf.json.JSONObject;

import com.esrichina.BP.request.HttpRequest;

public class AgsAdminSecurity {

	private void pushIdentityToDatabase(String rootUrl, String token, boolean sure){
		JSONObject params = new JSONObject();
		JSONObject properties = new JSONObject();
		properties.accumulate("PushIdentityToDatabase", sure);
		params.accumulate("token", token);
		params.accumulate("properties", properties.toString());
		HttpRequest.postRequest(rootUrl+"/system/properties/update", params.toString(), "POST");
	}
	
	public void setPushIdentityToDatabase(String rootUrl, String token){
		pushIdentityToDatabase(rootUrl, token, true);
	}
	
	public void cancelPushIdentityToDatabase(String rootUrl, String token){
		pushIdentityToDatabase(rootUrl, token, false);
	}
}
