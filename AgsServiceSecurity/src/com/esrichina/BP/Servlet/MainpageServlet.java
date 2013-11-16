package com.esrichina.BP.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.esrichina.BP.AgsServer.AGSConnection;
import com.esrichina.BP.AgsServer.AGSServiceManager;
import com.esrichina.BP.Tree.CatlogTreeNodeBeanImpl;
import com.esrichina.BP.Tree.OracleDataSourceImpl;
import com.esrichina.BP.Tree.RootTreeNodeBeanImpl;
import com.esrichina.BP.Tree.ServiceTreeNodeBeanImpl;
import com.esrichina.BP.Tree.TreeNodeBeanBase;

/**
 * Servlet implementation class MainpageServlet
 */
public class MainpageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainpageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//读取post过来的参数，用户填写的server连接信息，包括服务根目录，管理员用户名密码
		//保存空间数据的oracle数据库的连接信息
		String url = (String)request.getAttribute("url");
		String adminUser = (String)request.getAttribute("adminUser");
		String adminPassword = (String)request.getAttribute("adminPassword");
		String dbServer = (String)request.getAttribute("dbServer");
		String dbPort = (String)request.getAttribute("dbPort");
		String dbInstance = (String)request.getAttribute("dbInstance");
		String dbUser = (String)request.getAttribute("dbUser");
		String dbPassword = (String)request.getAttribute("dbPassword");
		
		//构建dojoTree的数组结构
		JSONArray dojoTree = new JSONArray();
		
		//构建根节点信息
		RootTreeNodeBeanImpl root = new RootTreeNodeBeanImpl();
		
		if("".equals(url) || url == null || "null".equals(url.toLowerCase())){
			
			//从AgsConfig.json里读配置信息以及OraConfig.json的数据源信息
			url = "http://localhost:6080/arcgis";
			String username="arcgis";
			String password="arcgis";
			dbServer = "localhost";
			dbPort = "1521";
			dbInstance = "SDE";
			dbUser = "sde";
			dbPassword = "sde";
			
			//根据服务url，用户名和密码申请Token
			String token = AGSConnection.getToken(url, username, password, "ip", "127.0.0.1", null);
			root.setUrl(url+"/admin/services");
			root.setAdminUser(adminUser);
			root.setAdminPassword(adminPassword);
			root.setId("root");
			root.setType("root");
			root.setName("Server");
			
			//加入数据源信息
			OracleDataSourceImpl ds = new OracleDataSourceImpl();
			ds.setServer(dbServer);
			ds.setPort(dbPort);
			ds.setInstance(dbInstance);
			ds.setUser(dbUser);
			ds.setPassword(dbPassword);
			root.setDs(ds);
			
			dojoTree.add(root);
			
			getResource(dojoTree, root, token);
		}
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().append(JSONArray.fromObject(dojoTree).toString());
		response.getWriter().flush();
	}

	/**迭代读取给定的服务目录下所有的服务和目录
	 * @param url
	 * @param token
	 * @return
	 */
	protected void getResource(JSONArray tree, TreeNodeBeanBase dir, String token){
		
		//查看根目录下面所有的目录和服务
		JSONObject res = AGSServiceManager.getAllFolderResources(dir.getUrl(), token);
		
		//记录id值
		int id=0;
		
		//获得目录下的所有目录信息
		JSONArray folders = new JSONArray();
		if(res.containsKey("folders")) folders = res.getJSONArray("folders");
		
		List<CatlogTreeNodeBeanImpl> foldersList = new ArrayList<CatlogTreeNodeBeanImpl>();
		for(int i=0;i<folders.size();i++){
			String folder = folders.getString(i);
			CatlogTreeNodeBeanImpl catlogNode = new CatlogTreeNodeBeanImpl();
			catlogNode.setUrl(dir.getUrl() + "/" + folder );
			catlogNode.setId(dir.getId()+"_" + i);
			catlogNode.setName(folder);
			catlogNode.setParent(dir.getId());
			getResource(tree, catlogNode, token);
			foldersList.add(catlogNode);
			id++;
		}
		
		//获得根目录下所有的服务信息
		JSONArray services = new JSONArray();
		if(res.containsKey("services")) services = res.getJSONArray("services");
		List<ServiceTreeNodeBeanImpl> serviceList = new ArrayList<ServiceTreeNodeBeanImpl>();
		for(int i=0;i<services.size();i++){
			JSONObject service = services.getJSONObject(i);
			ServiceTreeNodeBeanImpl serviceNode = new ServiceTreeNodeBeanImpl();
			serviceNode.setUrl(dir.getUrl() + "/" + service.getString("serviceName") + "." + service.getString("type"));
			serviceNode.setId(dir.getId()+"_" + id);
			serviceNode.setName(service.getString("serviceName") + "(" + service.getString("type") + ")");
			serviceNode.setServiceType(service.getString("type"));
			serviceNode.setParent(dir.getId());
			serviceNode.setType("node");
			serviceList.add(serviceNode);
			id++;
		}
		
		tree.addAll(serviceList);
		tree.addAll(foldersList);
	}

}
