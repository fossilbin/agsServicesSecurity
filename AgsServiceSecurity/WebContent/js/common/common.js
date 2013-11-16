	function pageChange(id, page, paras){
		var reportFrame = dojo.byId(id);
    	/*var parameter = "";
    	if(paras){
    		parameter = parameter + page;
    		var pa = "";
    		for(var prop in paras){
    			if(pa==""){
    				pa = pa + "?" + prop + "=" + paras[prop];
    			}else{
    				pa = pa + "&" + prop + "=" + paras[prop];
    			}
    		}
    		parameter = parameter + pa;
    	}*/
    	if(reportFrame){
    		var href = window.location.href;
    		reportFrame.src = href.substring(0, href.lastIndexOf("/")) + page + "?para=" + JSON.stringify(paras);
    	}
    }