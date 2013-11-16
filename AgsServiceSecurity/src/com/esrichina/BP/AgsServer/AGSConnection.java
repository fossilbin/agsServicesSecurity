package com.esrichina.BP.AgsServer;

import net.sf.json.JSONObject;

import com.esri.arcgis.system.EngineInitializer;
import com.esrichina.BP.request.HttpRequest;

public class AGSConnection {

	/**获取ArcGIS Server的Token
	 * @param Url  服务根目录url
	 * @param user  ArcGIS Server的用户
	 * @param password   ArcGIS Server的密码
	 * @param client   验证方式：ip，referance，request IP
	 * @param IpOrUrl   验证用ip或者url
	 * @param expiration  token过期时间
	 * @return
	 */
	public static String getToken(String Url, String user, String password,
			String client, String IpOrUrl, String expiration){
		JSONObject params = new JSONObject();
		params.put("username", user);
		params.put("password", password);
		params.put("client", client);
		params.put(client.toLowerCase(), IpOrUrl);
		if(expiration!=null) params.put("expiration", expiration);
		String response = HttpRequest.postRequest(Url + "/admin/generateToken", params.toString(),"POST");
		JSONObject responseObj =  JSONObject.fromObject(response);
		String token = responseObj.getString("token");
		return token;
	}
	
	public static void init() {
	    try {
	    	EngineInitializer.initializeServer(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeArcServer);
	    } catch (Exception e) {e.printStackTrace();}
	}
}
