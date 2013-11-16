package com.esrichina.BP.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.esrichina.BP.file.fileAccess;

public class fileConfigManagerImpl extends configMngrBase {

	private fileAccess fa = null;
	private String filename = "";
	private String lineRegex = "\n";
	private String KVregex = "=";
	private HashMap<String,String> properties = new HashMap<String,String>();
	
	public void load(){
		try {
			String content = fa.readFileByLines();
			loadPropertie(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConfig();
	}
	
	public void loadPropertie(String content){
		if(content.indexOf(lineRegex)!=-1){
			String[] lines = content.split(lineRegex);
			for(String line:lines){
				if(line.indexOf(KVregex)!=-1){
					String[] pair = line.split(KVregex);
					properties.put(pair[0], pair[1]);
				}
			}
		}
	}
	
	@Override
	public void closeConfig(){
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
	public String getConfigProperty(String prop) {
		// TODO Auto-generated method stub
		return properties.get(prop);
	}

	@Override
	public void setConfigProperty(String prop, String value){
		// TODO Auto-generated method stub
		properties.put(prop, value);
	}

	public void save(){
		String content = "";
		try {
			if(!filename.equals("")){
				fa.createNew();
				Iterator<String> keysIt = properties.keySet().iterator();
				while(keysIt.hasNext()){
					content = content + keysIt.next() + KVregex + properties.get(keysIt.next());
					content = content + lineRegex;
				}
				fa.open(filename);
				fa.append(content, -1);
				fa.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void openConfig(String name){
		// TODO Auto-generated method stub
		filename = name;
		try {
			fa = new fileAccess();
			fa.open(filename);
			load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
