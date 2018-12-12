
var app = {
		language : "en_US",
		contextPath : "",
		urls : {
			'module' : "/wp/page/module",
		},
		views : {},
		funcs : {},
		modules : [],
};

app.funcs.getModuleUrl = function(type,name)
{
	var m = app.modules[name];
	return m && app.contextPath+app.urls.module+"?t="+type+"&jsp="+m.jsp+"&p="+m.prefix+"&m="+m.name;
};

app.funcs.getModuleLanguageUrl = function(name)
{
	var m = app.modules[name];
	return m && app.contextPath+app.urls.module+"?t=language&jsp="+m.jsp+"&p="+m.prefix+"&m="+m.name+"&f=";
};

app.funcs.openModule = function(parent,name,opt)
{
	// get module's reference
	if( !app.modules[name] )
	{
		console.log("Error: module was not defined for "+name);
		return;
	};
	//var module = app.modules[name];	
	if( !app.modules[name].loading )  
	{
		console.log("Error: module was not loading for "+name);
		return;
	};
	app.modules[name].loading.done(function(){
		var defaultOptions = {};
		var options = $.extend({},defaultOptions,opt);
		var subview = new app.modules[name].view();
		if( opt && opt.onCreate )
		{
			opt.onCreate(subview);
		};
		//subview.options = options;
	
		subview.render.call(subview,parent,options);
	}).fail(function(jqXHR){
		alert("模块【"+name+"】加载失败。");
		console.log(jqXHR);
	});
};


app.funcs.registerModule = function(name,module,desktop,id,display)
{
	if( !app.modules[name] )
	{
		console.log("Error: module was not defined for "+name);
		return;
	}
	// setup module's view
	app.modules[name].view = module;
	// initialize desktop application if the desktop is true
	if( desktop )
	{
		var obj = {
			'id' : id,
			'name' : display,
			'module' : name,
			'icon' : app.funcs.getModuleUrl("icon",name),
			'render' : function(win) {
				var view = new module();
				obj.onClose = function() {
					view.onClose && view.onClose.call(view);
				};
				view.render.call(view,win);
			}
		};
		JQD.register(obj);
	};
};

app.funcs.loadModule = function(jsp,prefix,module)
{
	if( !app.modules[module] )
	{
		app.modules[module] = {
		  'jsp' : jsp,
		  'prefix' : prefix,
		  'name' : module,
		  'loading' : null,
		  'view' : null
		};
		var url = app.funcs.getModuleUrl("program",module);
		app.modules[module].loading = $.ajax({
			'url': url,
			'dataType' : "script",
			'method' : "get",
			'async' : false
		}).done(function(html){
			//console.log("# loading module "+module+" successfully!");
		}).fail(function(jqXHR){
			console.log("Error: module was loaded failed from "+url);
		});
	};
};
