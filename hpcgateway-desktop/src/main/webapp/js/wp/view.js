
app.views.BaseView = Backbone.View.extend({
	'options' : {},
	
	'parent' : undefined,
	
	'loading' : {
		'tpl'  : undefined,
		'i18n' : undefined,
		'acl'  : undefined
	},
	
	'getModuleName' : function()
	{
		return null;
	},
	
	'getId' : function() {
		return "";
	},
	'getName' : function() {
		return "";
	},
	
	'getTplName' : function()
	{
		return "template";
	},

	'loadI18nProperties' : function()
	{
		var me = this;
		var d = $.Deferred();
		var path = app.funcs.getModuleLanguageUrl(me.getModuleName())+"/";
		var language = app.language || "en_US";
		$.i18n.properties({
			 name: 'message',
			 path: path, 
			 mode: 'map', 
			 language: language,
			 cache: false,
			 async: true,
			 encoding: 'UTF-8',
			 callback: function() {
				d.resolve();
			 }
		}); 
		return d.promise();
	},
	
	'loadCss' : function()
	{
		var href = app.funcs.getModuleUrl("css",this.getModuleName());
		$("head").append(
				$("<link>").attr({
			         'rel'  : "stylesheet",
			         'type' : "text/css",
			         'href' : href,
			     })
		);
	},
	
	'loadJs' : function(files)
	{
		var moduleName = this.getModuleName();
		var rs = [];
		$.each(files, function(i,f){
			var url = app.funcs.getModuleUrl("js",moduleName) + "&f="+f;			
			var r = $.ajax({
				'url': url,
				'dataType' : "script",
				'method' : "get",
				'async' : false
			});
			rs.push(r);
		});
		return rs;
	},
	
	'loadTemplate' : function()
	{
		var name = this.getModuleName();
		var url = app.funcs.getModuleUrl("template",name);
		return $.ztmpl.load(name,url).done(function(html){
			//console.log(html);
		});
	},
	
//	'initialize' : function(opts)
//	{
//		this.options = opts;
//		var me = this;
//		if( opts )
//		{
//			opts["reload"] = function(){ 
//				if( me.load )
//				{
//					me.load.call(me);			
//				};
//			};
//		};
//	},
	
	'prompt' : function(msg)
	{
		var $win = $(this.el).closest("div.window").find("div.window_bottom");
		var $div = $win.find("div.prompt");
		if( $div.length == 0 )
		{
			$div = $("<div/>").css({"width":"100%","height":"100%","text-align":"left"}).addClass("prompt").appendTo($win);
		};
		$div.empty().append(msg);
	},

	/*
	 * display used to show 'html' into this.el
	 */
	'display' : function()
	{
	},
	
	/*
	 * bind used to bind event for element
	 */
	'bind' : function()
	{
        
	},
	/*
	 * load used to load some data into view
	 */
	'load' : function()
	{
		
	},
	
	/*
	 * setupLanguage used to setup locale string
	 */
	'setupLanguage' : function(map)
	{
		
	},

	/*
	 * setupUI used to control UI content
	 */
	'setupACL' : function(acl)
	{
		
	},

	'render' :function(win,opt)
	{
		var me = this;
		var view = $(this.el);
		me.parent = win;
		me.options = opt;
		
		me.loading.tpl = this.loadTemplate();
		me.loading.i18n = this.loadI18nProperties();
		
		var basin = win.find("div.basin");
		if( basin.length == 0 )
		{
			basin = win;
		};
		view.addClass("backbone_view").appendTo(basin.empty());
		
		me.loading.tpl.done(function(html){
			view.append.call(view,$(html));
			me.display.call(me);
			me.bind.call(me);
			me.load.call(me);
		}).fail(function(){
			view.empty().append("The UI contents were loaded failed");
		});

		$.when(me.loading.i18n,me.loading.tpl).done(function(){
			me.setupLanguage.call(me,$.i18n.map);
		});
	},
	
	'confirm' : function(obj) 
	{
		var tpl= '<div class="mark"></div>'+
					'<div class="alert_box">'+
					'<p><%=title%><span class="close">x</span></p>'+
					'  <p><%=message%></p>'+
					'  <p>'+
					'    <button class="btn_ok"><%=btn_ok%></button>'+
					'    <button class="btn_cancel"><%=btn_cancel%></button>'+
					'  </p>'+
					'</div>';

		var defopt = {"title":"Confirm","message":"Are you sure?","btn_ok":"OK","btn_cancel":"Cancel"};
		var opt = $.extend({},defopt,obj);
		//var win = $(app.wins[obj.win].el).closest(".window");
		var view = $(_.template(tpl,opt)).appendTo(obj["win"]);
		var close = function() {
			if( obj.cancel !== undefined ){
				   obj.cancel();
			};
			view.remove();
		};
		view.find("span.close").click(function(){
			close();
		});
		view.find("button.btn_ok").click(function() {
			if( obj.confirm !== undefined ){
		      obj.confirm();
		    };
		   view.remove();
		});
		view.find("button.btn_cancel").click(function() {
			close();
		});
	},
	
	'stopBubble' : function(e)
	{
		if( e && e.stopPropagation )
		{
			e.stopPropagation();
		}
		else
		{
			window.event.cancelBubble = true;
		}
	},
	
	'openView' : function(parent,name,opt)
	{
		app.funcs.openModule(parent,name,opt);
	},
	
	'getTreeObject' : function(name)
	{
		if( !name ) name = "ztree";
		var treeObj = $.fn.zTree.getZTreeObj(name);
		return treeObj;
	},
	
	'getSelectedTreeNodes' : function(treeObj)
	{
		if( treeObj )
		{
			return treeObj.getSelectedNodes();
		}
		else
		{
			return null;
		};
	},
	
	'reloadTreeNodes' : function(treeObj,nodes)
	{
		if( treeObj && nodes )
		{
			$.each(nodes,function(idx,node){
				treeObj.reAsyncChildNodes(node, "refresh");				
			});
		};
	},
	
	'createTreeWindow' : function(opt)
	{
		var me = this;
		var defcss = {"top":"0px","left":"0px","width":"320px","height":"240px","border":"1px solid #333","background":"#fff"};
		var css = $.extend({},defcss,opt.css);
		var $wrapper = $("<div/>",{"class":"wrapper_tree","style":"position:absolute;overflow:auto;"})
			.css(css).addClass("ztree")
			.appendTo(opt.parent)
			.click(function(e){
				me.stopBubble(e);
			});
		var setting = {
				'view' : {
					'showLine' : true,
					'showIcon' : true
				},
				'data' : {
					'simpleData' : {
						'enable' : true,
						'idKey' : "id",
						'pIdKey' : "parentId",
						'rootPId' : null
					}
				},
				'callback' : {
					'onClick' : function(event,treeId,treeNode,flag){
						//me.stopBubble(event);
						if( opt.selected !== undefined )
						{
							opt.selected(treeNode);
						};
					}
				}
		};

		$.fn.zTree.init($wrapper, setting, opt.data);
		$("body").one("click",function(e){
			$wrapper.remove();
		});
		return $wrapper;
	},
});



app.views.TableView = app.views.BaseView.extend({
	'getLineTpl' : function(){
		return "";
	},
	
	'getNoneTpl' : function() {
		return "";
	},
	
	'getUrl' : function()
	{
		return "";
	},
	
	'getTableName' : function() {
		return "#tbl_data";
	},
	
	'getPagerName' : function() {
		return "#pager";
	},
			
	'getQueryCondition' : function() {
		return {};
	},
	
	'create' : function()
	{
		var callback = this.options["create"];
		if( callback !== undefined && callback != null )
		{
			callback(this);
		};
	},
	
	'edit' : function(object)
	{
		var callback = this.options["edit"];
		if( callback !== undefined && callback != null )
		{
			callback(this,object);
		};
	},
	
	'detail' : function(object)
	{
		var callback = this.options["view"];
		if( callback !== undefined && callback != null )
		{
			callback(this,object);
		};
	},
	
	'remove' : function(object) 
	{
		var callback = this.options["remove"];
		if( callback !== undefined && callback != null )
		{
			callback(this,object);
		};
	},
	
	'bindLineEvent' : function(tr,item)
	{
		
	},
		
	'bindPager' : function(data)
	{
		var pager = data.pager;
		var p = $(this.el).find(this.getPagerName());
		p.pagination("updateItems",pager.count);
	},


	'bindResult' : function(data) {
		var list = data.list;

		var view = $(this.el);
		var me = this;
		var tbody = view.find(me.getTableName()+">tbody").empty();

		if (list.length == 0) {
			$(_.template(me.getNoneTpl(), {
				'noRecordStr' : "<label style='color:red;'>no records</label>"
			})).appendTo(tbody);
		} else {
			var tpl = _.template(me.getLineTpl());
			var map = $.i18n.map;
			$.each(list, function(idx, item) {
				item.seq = idx + 1;
				item.btn_view = map["query.view"] || "view";
				item.btn_edit = map["query.edit"] || "edit";
				item.btn_remove = map["query.remove"] || "remove";
				var s = tpl(item);
				var tr = $(s).appendTo(tbody);
				tr.find(".remove").click(function(e) {
					e.preventDefault();
					me.remove.call(me, item);
				});
				tr.find(".edit").click(function(e){
					e.preventDefault();
					me.edit.call(me,item);
				});
				tr.find(".view").click(function(e){
					e.preventDefault();				
					me.detail.call(me,item);
				});
				me.bindLineEvent.call(me,tr,item);
			});
		};
	},
	
	'bindOtherResult' : function(data)
	{
		
	},

	'getLoadPrompt' : function()
	{
		return "Loading "+this.getUrl();
	},
	
	'getErrorPrompt' : function()
	{
		return "加载数据失败";
	},
	
	'loadResult' : function(no,cb) {
		var me = this;
		var view = $(this.el);
		var pager = view.find(me.getPagerName());
		var table = view.find(me.getTableName());
		if( !pager || !table ) return;
		var size = pager.pagination("getItemsOnPage");
		var c = table.data("condition");
		var param = $.extend({'no':no,'size':size},c||{});
		me.prompt.call(me,me.getLoadPrompt.call(me));
		$.zload.get({
			'url' : me.getUrl.call(me),
			'data' : param
		}).done(function(resp) {
			if (resp.code == 0) {
				me.prompt.call(me, "完成");
				me.bindResult.call(me, resp.data);
				me.bindPager.call(me, resp.data);
				me.bindOtherResult.call(me,resp.data);
				if( cb ) cb();
			} else {
				me.prompt.call(me, me.getErrorPrompt.call(me)+ "，提示：" + resp.message+"。");
			};
		}).fail(function(jqXHR) {
			me.prompt.call(me, me.getErrorPrompt.call(me)+ "，提示：通讯故障。");
		});
	},

	'query' : function(cb) {
		var view = $(this.el);
		var me = this;
		view.find(me.getTableName()).data("condition", me.getQueryCondition.call(me));
		me.loadResult(1,cb);
	},

	'bindEvent' : function() {
		var view = $(this.el);
		var me = this;
		var pageSize = me.options && me.options["pageSize"];
		if( !pageSize ) pageSize = 20;
		var pager = view.find(me.getPagerName());
		pager.pagination({
			'itemsOnPage' : pageSize,
			'onPageClick' : function(pageNum){
				if( pageNum )
				{
					me.loadResult.call(me,pageNum);
				};
			},
		});
	},
	
	'load' : function()
	{
		this.query();
	},
});



app.views.FormView = app.views.BaseView.extend({
	// private variable	
	'getObjectTarget' : function()
	{
		return "保存数据";
	},
	
	// Overwrite
	'bindExtra' : function()
	{
		
	},

	'isRequireLoading' : function(id)
	{
		if( id ) 
		{
			return true;
		}
		else
		{
			return false;
		};
	},
	
	'getUrl' : function(id)
	{
		return "";
	},
	
	'loadExtraData' : function()
	{
		
	},

	// Overwrite
	'bindExtraObject' : function(object)
	{
		
	},
	
	// Overwrite
	'extractExtraObject' : function(object)
	{
		
	},
	
	'bindObject' :function(object)
	{
		if( object )
		{
			var view = $(this.el);
			$.each(view.find(".fedit"),function(){
				var name = $(this).attr("name");
				if( object[name] )
				{
					$(this).val(object[name]);
				};
			});
		};
		this.bindExtraObject(object);
	},
	
	'extractObject' : function()
	{
		var view = $(this.el);
		var object = {};
		$.each(view.find(".fedit"),function(){
			var name = $(this).attr("name");
			object[name] = $(this).val();
		});
		this.extractExtraObject(object);
		return object;
	},
	
	// done
	'done' : function()
	{
		var me = this;
		var done = me.options["done"];
		if( done !== undefined && done != null)
		{
			done();
		}
	},
	
	'trimObject' : function(src)
	{
		var type = $.type(src);
		var dst = undefined;
		var me = this;
		if( type == 'array' )
		{
			var dst = [];
			$.each(src,function(idx,item){
				dst.push(me.trimObject.call(me,item));
			});
		}
		else if( type == 'object' )
		{
			dst = {};
			$.each(src,function(key,value){
				if( value !== undefined && value != null && value != '' )
				{
					dst[key] = value;
				};
			});
		}
		else
		{
			dst = src;
		};
		return dst;
	},
	
	// check whether the object is ok
	//   if the checkObject return false, the save operation will quit
	//   otherwise continue 
	'checkObject' : function(object)
	{
		return true;
	},
	
	'saveObject' : function()
	{
		var me = this;
		var identifier = me.options["identifier"];
		var url = me.getUrl.call(me,identifier);
		var object = me.trimObject(me.extractObject());
		if( !me.checkObject(object) )
		{
			return;
		};
		
		var data = JSON.stringify(object);
		var target = me.getObjectTarget.call(me);
		me.prompt.call(me,"正在保存"+target+"......");
		
		$.zload.post({
			'url' : url,
			'contentType' : "application/json",
			'data' : data
		}).done(function(resp){
			if( resp.code == '0' )
			{
				me.prompt.call(me,target+"成功!");
				me.done.call(me);
			}
			else
			{
				me.prompt.call(me,"错误："+target+"失败，"+resp.message);
			};
		}).fail(function(jqXHR){
			me.prompt.call(me,"错误："+target+"失败，通讯故障。");
			console.log(JSON.stringify(jqXHR));
		});
	},
	
	'loadObject' : function()
	{
		var me = this;
		var identifier = me.options["identifier"];
		// if try to create a object, no data loading 
		if( !me.isRequireLoading(identifier) )
		{
			me.bindObject.call(me,null);
			return;
		};
		
		var url = me.getUrl(identifier);
		me.prompt.call(me,"正在加载"+me.getObjectTarget.call(me)+"...");
		$.zload.get({
			'url' : url
		}).done(function(resp){
			if( resp.code == '0' )
			{
				me.prompt.call(me,"");
				me.bindObject.call(me,resp.data);
			}
			else
			{
				me.prompt.call(me,"错误：加载"+me.getObjectTarget.call(me)+"失败，"+resp.message);
			};
		}).fail(function(jqXHR){
			me.prompt.call(me,"错误：加载"+me.getObjectTarget.call(me)+"失败，通信故障。");
		});
	},
	
	'bindEvent' : function()
	{
		var me = this;
		var view = $(this.el);
		view.find("button[name='save']").click(function(){
			me.saveObject.call(me);
		});			
	},
	
	'buildTableData': function(list)
	{
		var data = [];
		if( list === undefined || list == null )
		{
			return data;
		};
		$.each(list,function(idx,name){
			data.push([name]);
		});
		return data;
	},
	
	'buildSelectList' : function(opt)
	{
		if( opt.list === undefined || opt.list == null )
		{
			return [];
		};
	
		var list = opt.list;
		var key = opt.key;
		var value = opt.value;
		var filter = undefined;
		if( opt.filter !== undefined )
		{
			filter = new RegExp(opt.filter);
		};
		var data = [];
		$.each(list,function(idx,item){
			if( typeof item == 'string' )
			{
				if( filter === undefined || !filter.test(item))
				{
					data.push({'name':item,'value':item});
				};
			}
			else if( (typeof item) == 'object' && key !== undefined && value !== undefined )
			{
				if( filter === undefined || !filter.test(item[key]) )
				{
					data.push({'name':item[key],"value":item[value]});
				};
			}
		});
		return data;
	},
	
	'extractTableDataAsList' : function(tbl)
	{
		var value = [];
		tbl.editTable("exports",function(list){
			$.each(list,function(idx,fs){
				if( fs[0] != '' )
				{
					value.push(fs[0]);
				};
			});
		});		
		return value;
	},
	
	'display' : function()
	{
		var view = $(this.el);
		var hideTitle = this.options["hideTitle"];
		if( hideTitle !== undefined && hideTitle)
		{
			view.find(".title").hide();
		};
	},
	
	'load' : function()
	{
		var me = this;
		// load extra data, such as data from select ...
		me.loadExtraData();
		
		var object = me.options["object"];
		if( object )
		{
			me.bindObject(object);
		}
		else
		{
			this.loadObject();
		};
	},
	

	'bind' : function()
	{
		this.bindEvent();
		this.bindExtra();
	},
	
	'createGroupTemplate' : function(loading){
		var me = this;
		var view = $(this.el);
		var groupTemplate = {
			'type' : "groupSelect",
			'setValue' : function(column,value) {
				var div = $("<div/>",{"class":"edit_item"})
					.append($("<input type='hidden' name='key'>").val(value.id))
					.append($("<input type='text' name='value' disabled='disable'>").css({"width":"130px"}).val(value.name))
					.append($("<button/>",{"html":"...","href":"#"}).css({"width":"40px",height:"28px"}));
				var i_key = div.find("input[name='key']");
				var i_value = div.find("input[name='value']");
				div.find("button").click(function(e){
					me.stopBubble(e);
					view.css("position","relative");
					var left = i_value.position().left; 
					var top = i_value.position().top+i_value.outerHeight()+3;
					loading.done(function(resp){
						if( resp.code == 0 )
						{
							me.createTreeWindow({
								'parent' : view,
								'data' : resp.data,
								'css' : {"top":top,"left":left},
								'selected' : function(node) {
									i_key.val(node.id);
									i_value.val(node.name);
								}
							});
						};
					});
				});
				if( column["width"] !== undefined )
				{
					div.css({"width":column["width"]+"px"});
				};
				return div;
			},
			'getValue' : function(el) {
				return el.find("input[name='key']").val();
			}
		};
		return groupTemplate;
	}
});


app.views.ClusterView = app.views.BaseView.extend({
	'getClusterUrl' : function() {
		return app.contextPath + "/wp/cluster/items";
	},
	'getClusterTreeView' : function() {
		return ".cluster_tree";
	},
	'isFullClusterTree' : function() {
		return false;
	},
	
	'clusterSelected' : function(treeId,treeNode){
		console.log("node was selected.");
	},
	
	'displayClusterTree' : function()
	{
		var me = this;
		var view = $(this.el);
		var url = this.getClusterUrl();
		var setting = {
			'view' : {
				'showLine' : true,
				'showIcon' : true
			},
			'async': {
				'enable' : true,
				'url' : url,
				'type' : "get",
				'dataType' : "json",
				'cache' : false,
				'autoParam' : ["path"]
			},
			'data': {
				'keep' : {
					'parent' : true
				}
			},
			'callback' : {
				'beforeExpand' : function(treeId,treeNode) {
					
				},
				'onClick' : function(event,treeId,treeNode,flag){
					me.clusterSelected.call(me,treeId,treeNode);
				}
			}
		};
		var nodes = [{
			'name' : "Cluster(s)",
			'path' : "/",
			'title' : "",
			'isParent' : true,
			'open' : true
		}];

		var $container = view.find(this.getClusterTreeView());
		$.fn.zTree.init($container, setting, nodes);
	},
	
	'displayContent' : function() {
		
	},
	
	'display' : function()
	{
		this.displayClusterTree();
		this.displayContent();
	}
});
