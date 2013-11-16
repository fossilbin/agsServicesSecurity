package com.esrichina.BP.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class fileAccess {

	// 打开一个随机访问文件流，按读写方式 
	RandomAccessFile randomFile = null;
	String fileName = "";
	int line = 1;
		
		/**
	     * 以行为单位读取文件，常用于读面向行的格式化文件
	     
		 * @return 
		 * @throws IOException 
		 * */
		
		public void open(String name) throws IOException{
			fileName = name;
			randomFile = new RandomAccessFile(fileName, "rw");
		}
		
		public void close() throws IOException{
			fileName = "";
			randomFile.close();
		}
		
		public String readFileByLines() throws IOException{
			String tempString = null;
			String content = "";
			while((tempString = randomFile.readLine()) != null){
				System.out.println("line " + line + ": " + tempString);
				content += tempString;
				line++;
			}
			
			return content;
		}
		
		/** 
		* A方法追加文件：使用RandomAccessFile 
		* @param content 追加的内容 
		* @param content 追加的位置，-1表示末尾 
		 * @throws IOException 
		*/ 
		public void append(String content, long length) throws IOException{ 
			
			// 文件长度，字节数 
			if(length==-1) length = randomFile.length(); 
			//将写文件指针移到文件尾。 
			randomFile.seek(length); 
			randomFile.writeBytes(content); 
		}
		
		public void createNew() throws IOException{
			if(randomFile != null){
				randomFile.close();
			}
			if(!fileName.equals("")){
				File f = new File(fileName);
				FileWriter fw = new FileWriter(f);
				fw.write("");
				fw.close();
			}
		}
}
