(function($){
	
	"use strict";
	
	Chart.defaults.global.tooltips.enabled = false;
	Chart.defaults.global.title.display = false;
	Chart.defaults.global.legend.display = false;
	
	var defMAX = 50;
	
	// MonitorHost
	var MonitorHost = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'max' : 50,
				'height' : 160,
				'fill' : false,
				'showLegend' : true,
				'pointRadius' : 0,
				'stacked' : false,
				'charts' : {},
				//'backgroundColors' : [],
				'colors' : [
					    "rgba(255,0,0,1)",
					    "rgba(0,255,0,1)",
					    "rgba(0,0,255)",
					    "rgba(255,255,0,1)",
					    "rgba(255,0,255,1)",
					    "rgba(0,255,255,1)",
					    "rgba(192,192,192,1)",
					    "rgba(255,96,96,1)",
					    "rgba(96,255,96,1)",
					    "rgba(96,96,255,1)",
					    "rgba(255,255,96,1)",
					    "rgba(255,96,255,1)",
					    "rgba(96,255,255,1)",
					    "rgba(96,96,96,1)",
					    "rgba(255,192,192,1)",
					    "rgba(192,255,192,1)",
					    "rgba(192,192,255,1)",
					    "rgba(255,255,192,1)",
					    "rgba(255,192,255,1)",
					    "rgba(192,255,255,1)",
					    "rgba(0,0,0,1)"
				],
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	

	MonitorHost.prototype.initialize = function() 
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorHost.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		
		var colors = this.options["colors"];
		var backgroundColors = this.options["backgroundColors"];
		var height = this.options["height"];
		var fill = this.options["fill"];
		var pointRadius = this.options["pointRadius"];
		var showLegend = this.options["showLegend"];
		var stacked = this.options["stacked"];
		
		var datasets = [];
		$.each(fields,function(idx,field){
			var color = colors[idx];
			var backgroundColor = color;
			if( backgroundColors && backgroundColor[idx] )
			{
				backgroundColor = backgroundColors[idx];
			};
			datasets.push({
				"label" : field,
				"fill" : fill,
				"borderColor" : color,
				"backgroundColor" : backgroundColor,
				"pointRadius" : pointRadius,
				"data" : []
			});
		});
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").appendTo($tr).append(name);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":height+"px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : datasets,
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : showLegend,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	         'display' : true,
			  	         'stacked' : stacked,

		  	            }],		            	
		       	 },
			},
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorHost.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for(var ts in data)
		{
			// callback
			if( cb ) cb(ts);
			// process timestamp label
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			// methods
			var methods = data[ts];
			var objs = methods[method];
			if( !objs )
			{
				continue;
			}
			for( var name in objs )
			{
				var obj = objs[name];
				var fields = obj["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(obj[field]);
				});
				if( c.data.labels.length > max )
				{
					c.data.labels.shift();
					$.each(c.data.datasets,function(idx,ds){
						ds.data.shift();
					});					
				};
				c.update();
			};
		};
	};

	var oldMonitorHost = $.fn.monitorHost;
	$.fn.monitorHost = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorHost = $this.data("js.monitorHost");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorHost",(monitorHost=new MonitorHost(this,options)));				
			}
			else if( t == "string" && monitorHost && monitorHost[method] )
			{
				monitorHost[method].apply(monitorHost,args);
			};
		});
		return this;
	};
	$.fn.monitorHost.Constructor = MonitorHost;
   $.fn.monitorHost.noConflict = function () {
        $.fn.monitorHost = oldMonitorHost;
        return this
    };

})(jQuery);
