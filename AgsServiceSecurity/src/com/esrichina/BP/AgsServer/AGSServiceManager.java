package com.esrichina.BP.AgsServer;

import java.io.IOException;
import java.net.UnknownHostException;

import com.esri.arcgis.carto.IMSDHelper;
import com.esri.arcgis.carto.MSDHelper;
import com.esri.arcgis.system.IStringArray;
import com.esrichina.BP.Util.FileUtil;
import com.esrichina.BP.request.HttpRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AGSServiceManager {
	
	
	public static JSONObject getAllFolderResources(String servicesUrl, String token){
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		JSONObject resultObj = new JSONObject();
		String result = HttpRequest.postRequest(servicesUrl, params.toString(), "POST");
		resultObj = JSONObject.fromObject(result);
		return resultObj;
	}
	
	public static JSONArray getAllServices(String servicesUrl, String token){
		JSONObject params = getAllFolderResources(servicesUrl, token);
		JSONArray services = params.getJSONArray("services");
		JSONArray serviceNames = new JSONArray();
		for(int i=0;i<services.size();i++){
			serviceNames.add(JSONObject.fromObject(services.getString(i)).get("serviceName"));
		}
		return serviceNames;
	}
	
	
	/**获得给定的url下的所有目录信息
	 * @param servicesUrl
	 * @param token
	 * @return
	 */
	public static JSONArray getAllFolders(String servicesUrl, String token){
		JSONObject params = getAllFolderResources(servicesUrl, token);
		return params.getJSONArray("folders");
	}
	
	public static JSONArray getOracleInfo(String servicesUrl, String token){
		JSONArray oraInfos = new JSONArray();
		String msdpath = getServiceMsdPath(servicesUrl, token);
		FileUtil f = new FileUtil(msdpath.trim());
		msdpath = f.getNonVariablePath();
		AGSConnection.init();
		try {
			IMSDHelper msd = new MSDHelper();
			msd.open(msdpath);
			IStringArray maps = msd.getMaps();
			for(int i=0;i<maps.getCount();i++){
				IStringArray layers = msd.getLayers(maps.getElement(i));
				for(int j=0;j<layers.getCount();j++){
					String ws = msd.getWorkspaceConnectionStringFromLayer(layers.getElement(j));
					String dataset = msd.getDataset(layers.getElement(j));
					System.out.println(ws);
					if(ws.contains("sde:oracle")){
						JSONObject conObj = fromString(ws, ";", "=");
						conObj.put("dataset", dataset);
						oraInfos.add(conObj);
					}
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return oraInfos;
	}
	
	private static JSONObject fromString(String str, String attrRegex, String attrValueRegex){
		JSONObject obj = new JSONObject();
		String[] arrs = str.split(attrRegex);
		for(String arr: arrs){
			String[] kv = arr.split(attrValueRegex);
			obj.accumulate(kv[0], kv[1]);
		}
		return obj;
	}
	
	private static String getServiceMsdPath(String servicesUrl, String token){
		String path = "";
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		JSONObject resultObj = new JSONObject();
		String result = HttpRequest.postRequest(servicesUrl, params.toString(), "POST");
		resultObj = JSONObject.fromObject(result);
		JSONObject properties = resultObj.getJSONObject("properties");
		path = properties.getString("filePath");
		return path;
	}
	
	public static void start(String servicesUrl, String token){
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		HttpRequest.postRequest(servicesUrl+"/start", params.toString(), "POST");
	}
	
	public static void stop(String servicesUrl, String token){
		JSONObject params = new JSONObject();
		params.accumulate("token", token);
		HttpRequest.postRequest(servicesUrl+"/stop", params.toString(), "POST");
	}

}
