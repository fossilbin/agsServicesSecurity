require(["dijit/form/DateTextBox","dijit/form/Select"]);
require(["dojo/parser", "dojo/request","dojo/dom",
         "dojo/query", "dojo/dom-construct", "dijit/form/CheckBox" ,"dojo/domReady!"], 
         function(aspect, request, dom, query, domConstruct, CheckBox){
				var cols_validate;		
				request.post("dynTableServlet",{data:{ReportName:"support.rptlibrary"}}).then(
			        function(text){
			            dsList = JSON.parse(text); 
			            var select = dijit.byId('dslist');
			            select.addOption({label:"   ",value:"null"});
			            for(var i=0;i<dsList.length;i++){
			            	select.addOption({ label:dsList[i].displayname, value: dsList[i].name });
			            }
			            select.on('change', function(evt) {
			            	
			            	var select = dijit.byId('dslist');
			            	request.post("datetimeCols",{data:{dsname:select.value}}).then(
			            	        function(text){
			            	            cols_validate = JSON.parse(text);
			            	            var colsdiv = dom.byId("datecols");
					        			colsdiv.innerHTML = "";
					        			colsdiv.style.display = "none";
			            	            //包含类型是Datetime类型的列
			            	            cols = cols_validate.datetime;
			            	            if (cols==undefined || cols==null || cols.length==0){
						        			dijit.byId("startdate").set("disabled",true);
						        			dijit.byId("startdate").set("value",null);
						        			dijit.byId("enddate").set("disabled",true);
						        			dijit.byId("enddate").set("value",null);
						        			
						        		}else{
						        			dijit.byId("startdate").set("disabled",false);
						        			dijit.byId("enddate").set("disabled",false);
						        			for(var i=0;i<cols.length;i++){
						        				var inp = dojo.create("input", { type:"checkbox",id: "col"+i, checked:false });
						        				var text = dojo.create("textnode", { innerHTML: "&nbsp;"+cols[i]+"&nbsp;&nbsp;&nbsp;&nbsp;" });
						        				colsdiv.appendChild(inp);
						        				colsdiv.appendChild(text);
						        				if (i == cols.length-1)  colsdiv.style.display = "inline";
						        			}
						        		}
			            	            
			            	            //包含省市分公司信息的列
			            	            pos_cols = cols_validate.pos;
			            	            var provinces = cols_validate.provinces;
			            	            var citys = cols_validate.citys;
			            	            var areas = cols_validate.areas;
			            	            var provinceselect = dijit.byId("provincelist");
			            	            var cityselect = dijit.byId("citylist");
			            	            var areaselect = dijit.byId("area");
			            	            if (pos_cols==undefined || pos_cols==null || pos_cols.length==0){
			            	            	provinceselect.set("disabled",true);
			            	            	provinceselect.set("value",null);
			            	            	cityselect.set("disabled",true);
			            	            	cityselect.set("value",null);
			            	            	areaselect.set("disabled",true);
			            	            	areaselect.set("value",null);
						        		}else{
						        			provinceselect.set("disabled",false);
						        			cityselect.set("disabled",false);
						        			areaselect.set("disabled",false);
						        			provinceselect.reset();
						        			cityselect.reset();
						        			areaselect.reset();
						        			for(var province in provinces){
						        				provinceselect.addOption({ label:provinces[province], value: provinces[province] });
						        			}
						        			for(var city in citys){
						        				cityselect.addOption({ label:citys[city], value: citys[city] });
						        			}
						        			for(var area in areas){
						        				areaselect.addOption({ label:areas[area], value: areas[area] });
						        			}
						        		}
			            	            
			            	            //用户类型
			            	            usertypes = cols_validate.usertypes;
			            	            var utselect = dijit.byId("usertype");
			            	            if (usertypes==undefined || usertypes==null || usertypes.length==0){
			            	            	utselect.set("disabled",true);
			            	            	utselect.set("value",null);
			            	            }else{
			            	            	utselect.set("disabled",false);
			            	            	utselect.reset();
						        			for(var usertype in usertypes){
						        				utselect.addOption({ label:usertypes[usertype], value: usertypes[usertype] });
						        			}
			            	            }
			            	        }
			            	);
			        		
			            });
			            
			          //查询按钮事件
						var subButton = dijit.byId("sbButton");
						subButton.on("click",function(e){
							var filterText = "";
							var dsname = dijit.byId("dslist").get("value");
							var provinceSelect = dijit.byId("provincelist");
							if(!provinceSelect.disabled && provinceSelect.get("value")!=null && provinceSelect.get("value")!='') filterText = filterText + " and province='" + provinceSelect.get("value") + "'";
							var citySelect = dijit.byId("citylist");
							if(!citySelect.disabled && citySelect.get("value")!=null && citySelect.get("value")!='') filterText = filterText + " and city='" + citySelect.get("value") + "'";
							var subcomSelect = dijit.byId("area");
							if(!subcomSelect.disabled && subcomSelect.get("value")!=null && subcomSelect.get("value")!='') filterText = filterText + " and area='" +subcomSelect.get("value") + "'";
							var bptype = dijit.byId("usertype");
							if(!bptype.disabled && bptype.get("value")!=null && bptype.get("value")!='') filterText = filterText + " and customertype='" +bptype + "'";
							
							var startdateSelect = dijit.byId("startdate");
							var enddateSelect = dijit.byId("enddate");
							for(var i=0;i<cols_validate.datetime.length;i++){
								var check = dojo.byId("col"+i);
								if(check.checked){
									if(!startdateSelect.disabled && startdateSelect.get("value")!=null && startdateSelect.get("value")!='') filterText = filterText + " and " + cols_validate.datetime[i] + ">='" + startdateSelect.get("displayedValue") + "'";
									if(!enddateSelect.disabled && enddateSelect.get("value")!=null && enddateSelect.get("value")!='') filterText = filterText + " and " + cols_validate.datetime[i] + "<='" + enddateSelect.get("displayedValue") + "'";
								}
							}
							
							
							var query = cols_validate.queryText;
							if(query!=null && query!=""){
								var pos = query.lastIndexOf("1=1");
								if(pos!=-1) query = query.substring(0,pos+4) + filterText + " " + query.substring(pos+4); 
							}
//							window.location.href = "queryresult.html";
							window.location.href = "queryresult.html?querytext=" + encodeURI(query);
							
						});
			            
			        }
			);
			
         }

);

