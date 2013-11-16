
	require([
	    "dojo/aspect", "dojo/_base/window", "dojo/store/Memory", "dojo/store/Observable",
	    "dijit/Tree", "dijit/tree/ObjectStoreModel", "dijit/tree/dndSource",
	    "dojo/request","dojo/query","dojo/parser","dojo/domReady!"
	], function(aspect, win, Memory, Observable, Tree, ObjectStoreModel, dndSource, request, query){
//		require(["dojo/request"], function(request){
		    request.post("http://localhost:8080/AgsServiceSecurity/MainpageServlet").then(
		        function(text){
		            treeCatlog = JSON.parse(text);  

		            store = new Memory({
		                data: treeCatlog
		        	});
		            
//		            store = new Memory({
//		                data: [
//		                    { id: 'world', name:'The earth' ,parent: 'EG',type:'node'},
//		                    { id: 'root', name:'Africa',root:''},
//		        	            { id: 'EG', name:'Egypt', parent: 'root' },
//		                        { id: 'KE', name:'Kenya', parent: 'root' }
//		                ]
//		        	});
		            
		            store.getChildren = function(object){
		                // Add a getChildren() method to store for the data model where
		                // children objects point to their parent (aka relational model)
		                return this.query({parent: this.getIdentity(object)});
		            };
		            aspect.around(store, "put", function(originalPut){
		                // To support DnD, the store must support put(child, {parent: parent}).
		                // Since our store is relational, that just amounts to setting child.parent
		                // to the parent's id.
		                return function(obj, options){
		                    if(options && options.parent){
		                        obj.parent = options.parent.id;
		                    }
		                    return originalPut.call(store, obj, options);
		                }
		            });

		            // Wrap the store in Observable so that updates to the store are reflected to the Tree
		            store = new Observable(store);
		            
		            // Create the model and tree
		            model = new ObjectStoreModel({store: store, query: {id: "root"}});
		            
		            model.mayHaveChildren = function(item){
		            	if(item.root){
		            		return true;
		            	}else{
		            		return item.type!="node";
		            	}
		            };
		            
		            tree = new Tree({
		                model: model,
		                dndController: dndSource
		            }).placeAt("tree");
		            
		            tree.connect(tree, "onClick", function(item, node, evt){
		            	var page = "";
		            	if(item.type=="root"){
		            		page = "/serverSecurity.htm";
		            	}else{
		            		page = "/serviceSecurity.htm";
		            	}
		            	pageChange("report", page, item);
		            });
		        },
		        function(error){
		            console.log("An error occurred: " + error);
		        }
		    );
//		});
});
    
    