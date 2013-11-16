
require(["dojo/parser", "dojo/request","dojo/data/ItemFileWriteStore",
         "dojox/grid/EnhancedGrid", "dojox/grid/enhanced/plugins/Pagination", 
         "dojox/atom/io/model", "dojo/domReady!"], 
         function(aspect, request, ItemFileWriteStore, EnhancedGrid, Pagination, model){
			var loc = window.location.href;
			var queryText = loc.substring(loc.indexOf("querytext=", 0)+10);
			request.post("queryReportServlet",{data:{querytext:queryText}}).then(
				function(text){
					var resultObj = JSON.parse(text);
					var colnames = resultObj.colnames;
					var rows = resultObj.rows;
					var data = {
							identifier: 'id',
							items: []
				    };
					/*var data_list = [
					    {
					    	bpname: "",
				    		closedtime: null,
				    		dispatchtime: Object,
				    		id: 1,
				    		memname: "",
				    		message: "",
				    		mid: 17604,
				    		os: " ",
				    		pname: "xujinwen@longshine.com",
				    		posttime: Object,
				    		prodname: "ArcGIS Mobile",
				    		progress: 0,
				    		psub: "",
				    		subject: "",
				    		type: "ArcGIS Mobile",
				    		username: "xujinwen@longshine.com",
				    		version: "9.3"
					    }
				        { col1: "normal", col2: false, col3: model.util.unEscapeHtml('\&lt;div\&gt;text\&lt;p\&gt;aaa&lt;/p&gt;&lt;/div&gt;'), col4: 29.91},
				        { col1: "important", col2: false, col3: 'Because a % sign always indicates', col4: 9.33},
				        { col1: "important", col2: false, col3: 'Signs can be selectively', col4: 19.34}
				    ];*/
					var data_list = rows;
				    var rowcount = 200000;
				    if(rows.length<rowcount) rowcount =  rows.length;
				    for(var i=0; i<rowcount; i++){
				    	/*if(data_list[i]){
				    		for(var prop in data_list[i]){
				    			data_list[i][prop] = dojox.atom.io.model.util.unEscapeHtml(data_list[i][prop]);
					    	}
				    	}*/
				    	data.items.push(dojo.mixin({ id: i+1 }, data_list[i]));
				    }
				    var store = new dojo.data.ItemFileWriteStore({data: data});
				    /*set up layout*/
				    var layout = [];
				    var cols = [];
				    for(var i=0; i<colnames.length; i++){
				      var col = {name: colnames[i], field: colnames[i]};
				      cols.push(col);
				    };
				    layout.push(cols);
				    /*create a new grid:*/
				    var grid = new dojox.grid.EnhancedGrid({
				        id: 'grid',
				        store: store,
				        structure: layout,
				        rowSelector: '20px',
				        escapeHTMLInData: true,
				        autoHeight:true,
				        autoWidth:true,
				        plugins: {
				          pagination: {
				              pageSizes: ["25", "50", "100", "All"],
				              description: true,
				              sizeSwitch: true,
				              pageStepper: true,
				              gotoButton: true,
				                      /*page step to be displayed*/
				              maxPageStep: 4,
				                      /*position of the pagination bar*/
				              position: "bottom"
				          }
				        }
				    }, document.createElement('div'));
				    /*append the new grid to the div*/
				    dojo.byId("gridDiv").appendChild(grid.domNode);
				
				    /*Call startup() to render the grid*/
				    grid.startup();
			});
		});