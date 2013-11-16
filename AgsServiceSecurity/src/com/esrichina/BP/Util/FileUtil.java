package com.esrichina.BP.Util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil extends File {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileUtil(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	public String getNonVariablePath(){
		String NonVariablePath = super.getPath();
		while(NonVariablePath.indexOf("${")!=-1){
			NonVariablePath = replaceEnvVar(NonVariablePath);
		}
		return NonVariablePath;
	}
	
	private String replaceEnvVar(String path){
		Map<String,String> env = System.getenv();
		//用参数替换模板中的${}变量  
		Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(path);  
		  
		StringBuffer sb = new StringBuffer();  
		  
		while (m.find()) {  
		    String param = m.group(); //${xx}  
		    Object value = env.get( param.substring(2, param.length() - 1));
		    String b;
		    if(value==null){
		    	b = "";
		    }else{
		    	b = value.toString();
				b = b.replaceAll("\\\\", "\\\\\\\\");
		    }
			 System.out.println(b);
				m.appendReplacement(sb, b);
		   
			
		}  
		m.appendTail(sb);
		return sb.toString();
	}
	
	public String StringFilter(String str) {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	}

}
