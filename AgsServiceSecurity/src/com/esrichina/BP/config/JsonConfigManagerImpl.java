package com.esrichina.BP.config;
/***
 * 说明：将配置文件写入到.json文件中，首先使用open方法打开配置文件并
 * 将配置文件中的json串载入到内存中，调用getConfigProperty获得相应
 * 属性值，
 * 其中的参数prop是一个对象路径，“对象1.[数组位置1].对象2.对象3.属性名”，方法返回匹配路径的属性值。
 * 
 */
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.esrichina.BP.file.fileAccess;

public class JsonConfigManagerImpl extends configMngrBase {

	private fileAccess fa = null;
	private String filename = "";
	private JSONObject properties = new JSONObject();
	
	@Override
	public void openConfig(String fn) {
		// TODO Auto-generated method stub
		filename = fn;
		try {
			fa = new fileAccess();
			fa.open(filename);
			String content = fa.readFileByLines();
			if(!content.equals("")) properties = JSONObject.fromObject(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void closeConfig() {
		// TODO Auto-generated method stub
		filename = "";
		try {
			fa.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Object getConfigProperty(String prop) {
		// TODO Auto-generated method stub
		return getItemByPath(prop);
	}
	
	private Object getItemByPath(String path){
		String[] dirs = path.split("\\.");
		Object obj = properties;
		for(String dir:dirs){
			obj = getItem(obj, dir);
		}
		return obj;
	}

	private Object getItem(Object obj, String prop){
		if((obj instanceof JSONArray && ((JSONArray)obj).contains(prop)) ||
				(obj instanceof JSONObject && ((JSONObject)obj).containsKey(prop))){
			if(prop.startsWith("[") && prop.endsWith("]")){
				int i = Integer.parseInt(prop.substring(prop.indexOf("[")+1, prop.indexOf("]")));
				return ((JSONArray)obj).get(i);
			}else{
				return ((JSONObject)obj).get(prop);
			}
		}
		return null;
	}

	public void setConfigProperty(Object obj, String prop, String value) {
		if(prop.startsWith("[") && prop.endsWith("]")){
			prop = prop.substring(prop.indexOf("[")+1, prop.indexOf("]"));
		}
		if(obj instanceof JSONArray){
			((JSONArray)obj).set(Integer.parseInt(prop), value);
		}else if(obj instanceof JSONObject){
			((JSONObject)obj).put(prop, value);
		}
	}
	
	@Override
	public void setConfigProperty(String prop, String value) {
		// TODO Auto-generated method stub
		if(prop.indexOf(".")==-1){
			properties.put(prop, value);
		}else{
			String dir = prop.substring(0, prop.lastIndexOf("."));
			Object obj = getItemByPath(dir);
			String p = prop.substring(prop.lastIndexOf(".")+1,prop.length());
			if(p.startsWith("[") && p.endsWith("]")){
				p = prop.substring(prop.indexOf("[")+1, prop.indexOf("]"));
			}
			setConfigProperty(obj, p, value);
		}
		/*if(prop.indexOf(".")==-1){
			properties.put(prop, value);
		}else{
			String[] dirs = prop.split("\\.");
			int i=0;
			String p = "";
			Object obj = properties;
			for(String dir:dirs){
				Object item = getItem(obj, dir);
				if(item!=null){
					i++;
					p = p + dir;
					if(dir.startsWith("[") && dir.endsWith("]")){
						obj = getConfigProperty(p);
					}else{
						obj = getConfigProperty(p);
					}
					if(i<=dirs.length-1) p = p + ".";
				}else{
					if(dir.startsWith("[") && dir.endsWith("]")){
						int ind = Integer.parseInt(prop.substring(prop.indexOf("[")+1, prop.indexOf("]")));
						if(ind==0){
							if(i>=dirs.length-1){
								if(obj instanceof JSONArray){
									((JSONArray)obj).add(value);
								}else{
									((JSONObject)obj).accumulate(dir, value);
								}
							}else{
								JSONArray arr = new JSONArray();
								p = p + ".";
								if(obj instanceof JSONArray){
									((JSONArray)obj).add(arr);
								}else{
									((JSONObject)obj).accumulate(dir, arr);
								}
								i++;
								obj = getItem(properties, dir);
							}
						}
					}else{
						if(i>=dirs.length-1){
							if(obj instanceof JSONArray){
								((JSONArray)obj).add(value);
							}else{
								((JSONObject)obj).accumulate(dir, value);
							}
						}else{
							JSONObject ob = new JSONObject();
							p = p + ".";
							if(obj instanceof JSONArray){
								((JSONArray)obj).add(ob);
							}else{
								((JSONObject)obj).accumulate(dir, ob);
							}
							i++;
							obj = getItem(properties, dir);
						}
						
					}
				}
			}
		}*/
	}

	public void save(){
		try {
			if(!filename.equals("")){
				fa.createNew();
				fa.open(filename);
				fa.append(properties.toString(), -1);
				
				fa.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
