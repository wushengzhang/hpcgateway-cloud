(function($){
	$.zload = {};
	
	$.zload.post = function(options) {
		var default_options = {'cache':false,'type':'POST','dataType':'json','processData':true,'data':{}};
		var local_options = $.extend({},default_options,options);
	  	return $.ajax(local_options);		
	};

	$.zload.put = function(options){
		var default_options = {'cache':false,'type':'PUT','dataType':'json','processData':true,'data':{}};
		var local_options = $.extend({},default_options,options);
		return $.ajax(local_options);
	};
	
	$.zload.get = function(options) {
		var default_options = {'cache':false,'type':'GET','dataType':'json','processData':true,'data':{}};
		var local_options = $.extend({},default_options,options);
	  	return $.ajax(local_options);		
	};
	
	$.zload.del = function(options){
		var default_options = {'cache':false,'type':'DELETE','dataType':'json','processData':true};
		var local_options = $.extend({},default_options,options);
	  	return $.ajax(local_options);		
	};
	
	$.zload.upload = function(options) {
		var default_options = {'cache':false,'type':'POST','dataType':'json','processData':false,'contentType':false,'mimeType':'multipart/form-data'};
		var local_options = $.extend({},default_options,options);
	  	return $.ajax(local_options);				
	};
	
	$.zload.upload_put = function(options) {
		var default_options = {'cache':false,'type':'PUT','dataType':'json','processData':false,'contentType':false,'mimeType':'multipart/form-data'};
		var local_options = $.extend({},default_options,options);
	  	return $.ajax(local_options);				
	};
	
})(jQuery);
