
(function($){

	// define modulename
	var module_name = "materials";
	// define view
	var module = app.views.BaseView.extend({
		'getModuleName' : function(){
			return module_name;
		},
		
		'setupLanguage' : function(map)
		{
		},
		
		'bind': function()
		{
			var me = this;
			var view = $(this.el);
			view.find("div.fixedmenu").fixedMenu();
			view.find("a#id_cos_file_exit").click(function(e){
				e.preventDefault();
				me.parent.find("a.window_close").trigger("click");
			});	
			
			view.find("div.basin").empty();
			me.openView.call(me,view,"cos.web")
		},
	});
	
	app.funcs.registerModule(module_name,module,true,"materials","mat-gen");
	
})(jQuery);
