package com.esrichina.BP.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.beanutils.PropertyUtils;

import com.esrichina.BP.AgsServer.AGSAccountManager;
import com.esrichina.BP.AgsServer.AGSServiceManager;
import com.esrichina.BP.DB.DBCon;
import com.esrichina.BP.DB.OracleManager;
import com.esrichina.BP.DB.SQLModule;
import com.esrichina.BP.Tree.CatlogTreeNodeBeanBase;
import com.esrichina.BP.Tree.CatlogTreeNodeBeanImpl;
import com.esrichina.BP.Tree.RootTreeNodeBeanImpl;
import com.esrichina.BP.config.JsonConfigManagerImpl;
import com.esrichina.BP.security.AgsServiceSecurity;
import com.esrichina.BP.security.OraSecurity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class HttpRequest {

	public static String postRequest(String url, String paramstring, String postOrGet) {
		
		URL u = null;
		HttpURLConnection con = null;
		JSONObject params = JSONObject.fromObject(paramstring);
		params.put("f", "json");
		
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			Iterator a = params.keys();
			while (a.hasNext()) {
				String key = (String)a.next();
				sb.append(key);
				sb.append("=");
				sb.append(params.get(key));
				sb.append("&");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
//		System.out.println("send_url:" + url);
//		System.out.println("send_data:" + sb.toString());
		try {
			u = new URL(url);
			if(url.toLowerCase().startsWith("https:")){
				try {
					// Create a trust manager that does not validate certificate chains
					TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
					    public X509Certificate[] getAcceptedIssuers(){return null;}
					    public void checkClientTrusted(X509Certificate[] certs, String authType){}
					    public void checkServerTrusted(X509Certificate[] certs, String authType){}
					}};
				
					// Install the all-trusting trust manager
				
				    SSLContext sc = SSLContext.getInstance("TLS");
				    sc.init(null, trustAllCerts, new SecureRandom());
				    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				
					con = (HttpURLConnection) u.openConnection();
		  		} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				con = (HttpURLConnection) u.openConnection();
			}
			con.setRequestMethod(postOrGet);
//			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con
					.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con
					.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}
	
	public static void main(String[] args){
		
		System.out.println(JSONArray.fromObject("[\"securitytest\",\"System\",\"Utilities\"]").toString());
		
		/*********ArcGIS Server申请Token*****************************/
		JSONObject params = new JSONObject();
		params.put("username", "arcgis");
		params.put("password", "arcgis");
		params.put("client", "ip");
		params.put("ip", "127.0.0.1");
		params.put("f", "json");
		System.out.println(params.toString());
		String response = HttpRequest.postRequest("http://localhost:6080/arcgis/admin/generateToken", params.toString(), "POST");
		JSONObject responseObj =  JSONObject.fromObject(response);
		String token = responseObj.getString("token");
		System.out.println(token);
		JSONObject params1 = new JSONObject();
		params1.put("f", "json");
		params1.put("token", token);
		response = HttpRequest.postRequest("http://localhost:6080/arcgis/admin/services", params1.toString(), "POST");
		JSONObject res = AGSServiceManager.getAllFolderResources("http://localhost:6080/arcgis/admin/services", token);
		System.out.println(response.toString());
        /*********ArcGIS Server账户管理*****************************/
//		AGSAccountManager account = new AGSAccountManager("http://localhost:6080/arcgis/admin/security", token);
//		JSONArray result = account.getAllUsers();
//		JSONArray resultr = account.getAllRoles();
		
		/*********ArcGIS Server服务管理*****************************/
//		JSONArray sresult = AGSServiceManager.getAllServices("http://localhost:6080/arcgis/admin/services/Utilities", token);
//		JSONObject sec = AgsServiceSecurity.getResourceSecurity("http://localhost:6080/arcgis/admin/services/SampleWorldCities.MapServer", token);
		
		/*********ArcGIS Server服务权限管理*****************************/
//		AgsServiceSecurity.addPermission("http://localhost:6080/arcgis/admin/services/Utilities", token, "asia_role");
//		JSONObject fsec = AgsServiceSecurity.getResourceSecurity("http://localhost:6080/arcgis/admin/services/Utilities", token);
//		AgsServiceSecurity.delPermission("http://localhost:6080/arcgis/admin/services/Utilities", token, "asia_role");
		
		/*********Oracle数据权限管理*****************************/
//		JSONArray oraInfos = AGSServiceManager.getOracleInfo("http://localhost:6080/arcgis/admin/services/quhuamian.MapServer", token);
//		System.out.println(oraInfos.toString());
//		DBCon conn = new DBCon();
//		Connection con = conn.createOracleConnect("localhost", "1521", "SDE", "sde", "sde");
//		OracleManager om = new OracleManager(con);
//		JSONArray allusers = om.getAllUsers();
//		JSONArray roles = om.getRolesOfUser("ASIA");
//		OraSecurity.grantUserObject(con, "sde.continent", "asia");
//		OraSecurity.createShadowView(con, "SDE.CONTINENT1");
//		OraSecurity.createRowPolicyFunction(con, "CONTINENT1", "SDE.USERCLAUSE", "USERNAME", "CONDITION");
//		OraSecurity.addRowPolicy(con, "SDE.CONTINENT1", "SDE");
		
		
//		JSONArray JSONusers = new JSONArray();
//		JSONusers.add(new String("Asia"));
//		OraSecurity.createColPolicyFunction(con, "SDE.CONTINENT1", JSONusers.toString());
//		JSONArray JSONcols = new JSONArray();
//		JSONcols.add("objectid");
//		JSONcols.add("continent");
//		OraSecurity.addColPolicy(con, "SDE.CONTINENT1", "SDE", JSONcols.toString());
//		OraSecurity.dropPolicy(con, "SDE", "CONTINENT1", "AGSCOLPOLICY");
//		System.out.println(roles.toString());
//		conn.close();
		
		
		/*************配置文件管理*********************/
		/*JsonConfigManagerImpl jf = new JsonConfigManagerImpl();
		jf.openConfig(System.getProperty("user.dir")+"/src/com/esrichina/BP/config/AGSConfig.json");
		
		System.out.println(AGSServiceManager.getAllFolders("http://localhost:6080/arcgis/rest/services/securitytest", token));
		
		RootTreeNodeBeanImpl root = new RootTreeNodeBeanImpl();
		String rootUrl = "http://localhost:6080/arcgis/rest/services";
		root.setUrl(rootUrl);
		root.setAdminUser("arcgis");
		root.setAdminPassword("arcgis");
		CatlogTreeNodeBeanImpl catlog = new CatlogTreeNodeBeanImpl();
		catlog.setUrl(rootUrl+"System");
		JSONArray arr = new JSONArray();
		arr.add("asia");
		catlog.setAllowRoles(arr);
//		root.setAllowRoles(arr);
		JSONArray arr1 = new JSONArray();
		arr1.add("System");
		root.setDirs(arr1);
		JSONArray arr2 = new JSONArray();
		arr2.add(catlog);
		root.setDirs(arr2);
		jf.setConfigProperty("root", JSONObject.fromObject(root).toString());
		
		System.out.println(((JSONObject)jf.getConfigProperty("root")).toString());
		RootTreeNodeBeanImpl trn = new RootTreeNodeBeanImpl();
		JSONObject rootObj = (JSONObject)jf.getConfigProperty("root");
		JsonConfig config = new JsonConfig();
		
		RootTreeNodeBeanImpl tr = (RootTreeNodeBeanImpl)JSONObject.toBean(rootObj,  RootTreeNodeBeanImpl.class);
		System.out.println(tr.getAllowRoles().size());
		
		jf.save();
		jf.closeConfig();*/
	}
}
