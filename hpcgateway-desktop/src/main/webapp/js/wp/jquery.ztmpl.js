(function($){
	var _promises = {};
	var tplurl = "/app/template?m=";
	
	var loadTemplateAsync = function(tplId){
		var promise = _promises[tplId] || $.get(tplId);
	    _promises[tplId] = promise;
	    return promise;
	};
	
	var loadTemplateAsync2 = function(name,url){
		var promise = _promises[name] || $.get(url);
	    _promises[name] = promise;
	    return promise;
	};
	
	$.ztmpl = {};
	$.ztmpl.setUrl = function(url) {
		tplurl = url;
	};
	$.ztmpl.load = function(name,url) {
	    var promise = loadTemplateAsync2(name,url);
	    return promise;
	};
})(jQuery);
