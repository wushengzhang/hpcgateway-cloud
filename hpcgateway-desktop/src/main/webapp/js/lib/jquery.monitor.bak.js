(function($){
	
	"use strict";
	
	Chart.defaults.global.tooltips.enabled = false;
	Chart.defaults.global.title.display = false;
	Chart.defaults.global.legend.display = false;
	
	var defMAX = 50;
	
	// item - CPU
	var MonitorCpu = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : defMAX,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};

	MonitorCpu.prototype.addGeneral = function(general) {
		if( !this.options.charts ) this.options.charts = {};
		var chart = this.options.charts[general]; 
		if(  chart )
		{
			return chart;
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table).attr("name",general);
		$("<td/>").append(general).css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"140px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "sy",
					"fill" : true,
					"backgroundColor": "rgba(255,50,50,0.4)",
					"borderColor": "rgba(255,50,50,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "hi",
					"fill" : true,
					"backgroundColor": "rgba(255,100,100,0.4)",
					"borderColor": "rgba(255,100,100,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "si",
					"fill" : true,
					"backgroundColor": "rgba(255,150,150,0.4)",
					"borderColor": "rgba(255,150,150,1)",
					"pointRadius" : 0,
					"data" : [],
				}, {
					"label" : "st",
					"fill" : true,
					"backgroundColor": "rgba(255,200,200,0.4)",
					"borderColor": "rgba(255,200,200,1)",
					"pointRadius" : 0,
					"data" : [],
				}, {
					"label" : "wa",
					"fill" : true,
					"backgroundColor": "rgba(100,255,100,0.4)",
					"borderColor": "rgba(100,255,100,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "ni",
					"fill" : true,
					"backgroundColor": "rgba(155,255,155,0.4)",
					"borderColor": "rgba(155,255,155,1)",
					"pointRadius" : 0,
					"data" : []
				},{
					"label" : "us",
					"fill" : true,
					"backgroundColor": "rgba(100,100,255,0.4)",
					"borderColor": "rgba(100,100,255,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "id",
					"fill" : true,
					"backgroundColor": "rgba(10,10,10,0.4)",
					"borderColor": "rgba(255,255,255,1)",
					"pointRadius" : 0,
					"data" : []
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
		       'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'ticks' : {
			  	            'min' : 0,
			  	            'max' : 100
			  	              },
			  	          'stacked' : true,
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.charts[general] = chart;
		return chart;
	};
	
	MonitorCpu.prototype.initialize = function() {
		var me = this;
		var $table = $("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};

	MonitorCpu.prototype.update = function(method,data,cb) {
		var me = this;
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for( var ts in data )
		{
			var methods = data[ts];
			var timestamp = new Date();
			timestamp.setTime(ts);
			if( cb ) cb(ts);
			var cpus = methods[method];
			if( !cpus || cpus.length == 0 )
			{
				continue;
			}
			this.addGeneral("total");
			for( var name in cpus )
			{
				var cpu = cpus[name];
				var c = this.addGeneral(name);
				c.data.labels.push(timestamp.format("hh:mm:ss"));
				c.data.datasets[0].data.push(cpu.sy);
				c.data.datasets[1].data.push(cpu.hi);
				c.data.datasets[2].data.push(cpu.si);
				c.data.datasets[3].data.push(cpu.st);
				c.data.datasets[4].data.push(cpu.wa);
				c.data.datasets[5].data.push(cpu.ni);
				c.data.datasets[6].data.push(cpu.us);
				c.data.datasets[7].data.push(cpu.id);		
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
	
	// CPU - extensive
	var oldCpu = $.fn.monitorCpu;
	$.fn.monitorCpu = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorCpu = $this.data("js.monitorCpu");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorCpu",(monitorCpu=new MonitorCpu(this,options)));				
			}
			else if( t == "string" && monitorCpu && monitorCpu[method] )
			{
				monitorCpu[method].apply(monitorCpu,args);
			};
		});
		return this;
	};
	$.fn.monitorCpu.Constructor = MonitorCpu;
   $.fn.monitorCpu.noConflict = function () {
        $.fn.monitorCpu = oldCpu;
        return this
    };
  
   // item - Memory
	var MonitorMemory = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'max' : defMAX,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorMemory.prototype.addSwp = function()
	{
		if( this.options.swp ) {
			return this.options.swp;
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table).attr("name","swap");
		$("<td/>").append("Swap").css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"100px"});
		var chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "used",
					"fill" : true,
					"backgroundColor": "rgba(255,50,50,0.4)",
					"borderColor": "rgba(255,50,50,1)",
					"pointRadius" : 0,
					"data" : [],
				}, {
					"label" : "free",
					"fill" : true,
					"backgroundColor": "rgba(255,200,200,0.4)",
					"borderColor": "rgba(255,200,200,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,

				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'ticks' : {
			  	            'min' : 0,
			  	            'max' : 100
			  	              },
			  	          'stacked' : true,
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.swp = chart;
		return chart;		
	};
	
	MonitorMemory.prototype.addMem = function()
	{
		if( this.options.mem ) {
			return this.options.mem;
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table).attr("name","mem");
		$("<td/>").append("Memory").css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"100px"});
		var chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "used",
					"fill" : true,
					"backgroundColor": "rgba(255,50,50,0.4)",
					"borderColor": "rgba(255,50,50,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "cache",
					"fill" : true,
					"backgroundColor": "rgba(255,100,100,0.4)",
					"borderColor": "rgba(255,100,100,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "buffer",
					"fill" : true,
					"backgroundColor": "rgba(255,150,150,0.4)",
					"borderColor": "rgba(255,150,150,1)",
					"pointRadius" : 0,
					"data" : [],
				}, {
					"label" : "free",
					"fill" : true,
					"backgroundColor": "rgba(255,200,200,0.4)",
					"borderColor": "rgba(255,200,200,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false, 
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'ticks' : {
			  	            'min' : 0,
			  	            'max' : 100
			  	              },
			  	          'stacked' : true,
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.mem = chart;
		return chart;
	};	
	
	MonitorMemory.prototype.initialize = function()
	{
		var $table = $("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
		this.addMem();
		this.addSwp();
	};
	
	MonitorMemory.prototype.update = function(data,cb)
	{
		var me = this;
		var max = this.options["max"];
		if( !max ) max = defMAX;

		for( var timestamp in data )
		{
			if( cb ) cb(timestamp);
			var m = data[timestamp];
			console.log(timestamp+" => "+JSON.stringify(m));
			var ts = new Date();
			ts.setTime(timestamp);

			var mem = this.addMem();
			mem.data.labels.push(ts.format("hh:mm:ss"));
			mem.data.datasets[0].data.push(m.mem);
			mem.data.datasets[1].data.push(m.cache);
			mem.data.datasets[2].data.push(m.buffer);
			mem.data.datasets[3].data.push(m.memFree);
			if( mem.data.labels.length > max )
			{
				mem.data.labels.shift();
				$.each(mem.data.datasets,function(idx,ds){
					ds.data.shift();
				});					
			};
			
			var swp = this.addSwp();
			swp.data.labels.push(ts.format("hh:mm:ss"));
			swp.data.datasets[0].data.push(m.swp);
			swp.data.datasert[1].data.push(m.swpFree);
			if( swp.data.labels.length > max )
			{
				swp.data.labels.shift();
				$.each(swp.data.datasets,function(idx,ds){
					ds.data.shift();
				});
			};
			c.update();		
		};		
	};
	
	var oldMem = $.fn.monitorMemory;
	$.fn.monitorMemory = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorMemory = $this.data("js.monitorMemory");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorMemory",(monitorMemory=new MonitorMemory(this,options)));				
			}
			else if( t == "string" && monitorMemory && monitorMemory[method] )
			{
				monitorMemory[method].apply(monitorMemory,args);
			};
		});
		return this;
	};
	$.fn.monitorMemory.Constructor = MonitorMemory;
   $.fn.monitorMemory.noConflict = function () {
        $.fn.monitorMemory = oldMem;
        return this
    };
    
	// item - Ethernet
	var MonitorEthernet = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorEthernet.prototype.initialize = function() {
		var $table = $("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorEthernet.prototype.addGeneral = function(name)
	{
		var me = this;
		if( !me.options.charts )  me.options.charts = {};
		if( this.options.charts[name] ) {
			return this.options.charts[name];
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(name).css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"160px"});
		var chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "kin",
					"fill" : false,
					"borderColor": "rgba(255,0,0,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "kout",
					"fill" : false,
					"borderColor": "rgba(0,0,255,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'ticks' : {
			  	            'min' : 0,
			  	              },
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorEthernet.prototype.update = function(method,data,cb)
	{
		var me = this;
		var max = this.options["max"];
		if( !max ) max = defMAX;
		this.addGeneral("total");
		for( var ts in data )
		{
			if( cb ) cb(ts);
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");

			var methods = data[ts];
			var nets = methods[method];
			if( !nets )
			{
				continue;
			};
			
			for( var name in nets )
			{
				var c = this.addGeneral(name);
				var net = nets[name];
				c.data.labels.push(label);
				c.data.datasets[0].data.push(net.kin);
				c.data.datasets[1].data.push(net.kout);
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

	var oldEthernet = $.fn.monitorEthernet;
	$.fn.monitorEthernet = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorEthernet = $this.data("js.monitorEthernet");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorEthernet",(monitorEthernet=new MonitorEthernet(this,options)));				
			}
			else if( t == "string" && monitorEthernet && monitorEthernet[method] )
			{
				monitorEthernet[method].apply(monitorEthernet,args);
			};
		});
		return this;
	};
	$.fn.monitorEthernet.Constructor = MonitorEthernet;
   $.fn.monitorEthernet.noConflict = function () {
        $.fn.monitorEthernet = oldEthernet;
        return this
    };
    
	// item - Ib
	var MonitorIb = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
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
						    "rgba(0,0,0,1)",
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
	
	MonitorIb.prototype.initialize = function() {
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorIb.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		var colors = this.options["colors"];
		var datasets = [];
		$.each(fields,function(idx,field){
			datasets.push({
				"label" : field,
				"fill" : false,
				"borderColor" : colors[idx],
				"pointRadius" : 0,
				"data" : []
			});
		});
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").appendTo($tr).append(name);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"320px"});
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
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	         'display' : true,
		  	            }],		            	
		       	 },
			},
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorIb.prototype.update = function(data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for(var ts in data)
		{
			if( cb ) cb(ts);
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			
			var methods = data[ts];
			var items = methods[method];
			if( !items )
			{
				continue;
			}
			for( var name in items )
			{
				var item = items[name];
				var fields = item["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(item[field]);
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


	var oldIb = $.fn.monitorIb;
	$.fn.monitorIb = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorIb = $this.data("js.monitorIb");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorIb",(monitorIb=new MonitorIb(this,options)));				
			}
			else if( t == "string" && monitorIb && monitorIb[method] )
			{
				monitorIb[method].apply(monitorIb,args);
			};
		});
		return this;
	};
	$.fn.monitorIb.Constructor = MonitorIb;
   $.fn.monitorIb.noConflict = function () {
        $.fn.monitorIb = oldIb;
        return this
    };
    
	// item - Disk
	var MonitorDisk = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : defMAX,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorDisk.prototype.initialize = function() 
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorDisk.prototype.addIops = function(disk)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[disk];
		if( chart )
		{
			return chart;
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(disk).css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"160px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "rdiops",
					"fill" : true,
					"backgroundColor": "rgba(255,50,50,0.4)",
					"borderColor": "rgba(100,100,100,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "wriops",
					"fill" : true,
					"backgroundColor": "rgba(50,50,255,0.4)",
					"borderColor": "rgba(100,100,100,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		
		this.options.charts[disk] = chart; 
		return chart;
	};
	
	MonitorDisk.prototype.addKbps = function(disk)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[disk];
		if( chart )
		{
			return chart;
		};
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(disk).css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"100px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "rdiops",
					"fill" : false,
					"borderColor": "rgba(255,0,0,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "wriops",
					"fill" : false,
					"borderColor": "rgba(0,0,255,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});		
		this.options.charts[disk] = chart;
		return chart;
	};
	
	MonitorDisk.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for( var ts in data )
		{
			if( cb ) cb(ts);
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			
			var methods = data[ts];
			var disks = methods[method];
			if( !disks )
			{
				continue;
			};
			
			if( method == "KBPS" )
			{
				for( var name in disks )
				{
					var c = this.addKbps(name);
					var disk = disks[name];
					c.data.labels.push(label);
					c.data.datasets[0].data.push(disk.rdkbs);
					c.data.datasets[1].data.push(disk.wrkbs);
					if( c.data.labels.length > max )
					{
						c.data.labels.shift();
						$.each(c.data.datasets,function(idx,ds){
							ds.data.shift();
						});					
					};
					c.update();		
				};
			}
			else if( method == "IOPS" )
			{
				for( var name in disks )
				{
					var c = this.addIops(name);
					var disk = disks[name];
					c.data.labels.push(label);
					c.data.datasets[0].data.push(disk.rdios);
					c.data.datasets[1].data.push(disk.wrios);
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
	};

	var oldDisk = $.fn.monitorDisk;
	$.fn.monitorDisk = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorDisk = $this.data("js.monitorDisk");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorDisk",(monitorDisk=new MonitorDisk(this,options)));				
			}
			else if( t == "string" && monitorDisk && monitorDisk[method] )
			{
				monitorDisk[method].apply(monitorDisk,args);
			};
		});
		return this;
	};
	$.fn.monitorDisk.Constructor = MonitorDisk;
   $.fn.monitorDisk.noConflict = function () {
        $.fn.monitorDisk = oldDisk;
        return this
    };
    
	// item - Process
	var MonitorProcess = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorProcess.prototype.update = function()
	{
		
	};

	var oldProcess = $.fn.monitorProcess;
	$.fn.monitorProcess = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorProcess = $this.data("js.monitorProcess");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorProcess",(monitorProcess=new MonitorProcess(this,options)));				
			}
			else if( t == "string" && monitorProcess && monitorProcess[method] )
			{
				monitorProcess[method].apply(monitorProcess,args);
			};
		});
		return this;
	};
	$.fn.monitorProcess.Constructor = MonitorProcess;
   $.fn.monitorProcess.noConflict = function () {
        $.fn.monitorProcess = oldProcess;
        return this
    };
    
	// item - Interrupt
	var MonitorInterrupt = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorInterrupt.prototype.update = function()
	{
		
	};

	var oldInterrupt = $.fn.monitorInterrupt;
	$.fn.monitorInterrupt = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorInterrupt = $this.data("js.monitorInterrupt");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorInterrupt",(monitorInterrupt=new MonitorInterrupt(this,options)));				
			}
			else if( t == "string" && monitorInterrupt && monitorInterrupt[method] )
			{
				monitorInterrupt[method].apply(monitorInterrupt,args);
			};
		});
		return this;
	};
	$.fn.monitorInterrupt.Constructor = MonitorInterrupt;
   $.fn.monitorInterrupt.noConflict = function () {
        $.fn.monitorInterrupt = oldInterrupt;
        return this
    };
    
    
	// item - Buddy
	var MonitorBuddy = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorBuddy.prototype.initialize = function()
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorBuddy.prototype.addNode = function(node)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[node];
		if( chart )
		{
			return chart;
		};
		var $table = this.$element.find("table");
		
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(node).css({"width":"80px"}).appendTo($tr);
		var $div = $("<div/>");		
		$("<td/>").appendTo($tr).append($div);
				
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"200px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : "p1",
					"fill" : false,
					"borderColor": "rgba(255,0,0,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p2",
					"fill" : false,
					"borderColor": "rgba(255,255,0,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p4",
					"fill" : false,
					"borderColor": "rgba(255,0,255,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p8",
					"fill" : false,
					"borderColor": "rgba(255,64,128,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p16",
					"fill" : false,
					"borderColor": "rgba(255,128,64,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p32",
					"fill" : false,
					"borderColor": "rgba(0,0,255,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p64",
					"fill" : false,
					"borderColor": "rgba(255,128,64,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p128",
					"fill" : false,
					"borderColor": "rgba(128,64,255,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p256",
					"fill" : false,
					"borderColor": "rgba(64,128,255,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p512",
					"fill" : false,
					"borderColor": "rgba(0,255,0,1)",
					"pointRadius" : 0,
					"data" : [],
				},{
					"label" : "p1024",
					"fill" : false,
					"borderColor": "rgba(100,255,255,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});

		this.options.charts[node] = chart;
		return chart;
	};
	
	MonitorBuddy.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for( var ts in data )
		{
			if( cb ) cb(ts);
			var methods = data[ts];
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			var buddies = methods[method];		
			if( !buddies )
			{
				continue;
			};
			for( var node in buddies )
			{
				var chart = this.addNode(node);
				var buddy = buddies[node];
				if( !buddy ) {
					continue;
				};
				chart.data.labels.push(label);
				chart.data.datasets[0].data.push(buddy.p1);
				chart.data.datasets[1].data.push(buddy.p2);
				chart.data.datasets[2].data.push(buddy.p4);
				chart.data.datasets[3].data.push(buddy.p8);	
				chart.data.datasets[4].data.push(buddy.p16);	
				chart.data.datasets[5].data.push(buddy.p32);	
				chart.data.datasets[6].data.push(buddy.p64);	
				chart.data.datasets[7].data.push(buddy.p128);	
				chart.data.datasets[8].data.push(buddy.p256);	
				chart.data.datasets[9].data.push(buddy.p512);	
				chart.data.datasets[10].data.push(buddy.p1024);	
				// move
				if( chart.data.labels.length > max )
				{
					chart.data.labels.shift();
					$.each(chart.data.datasets,function(idx,ds){
						ds.data.shift();
					});					
				};
				chart.update();	
			};
		};
	};

	var oldBuddy = $.fn.monitorBuddy;
	$.fn.monitorBuddy = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorBuddy = $this.data("js.monitorBuddy");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorBuddy",(monitorBuddy=new MonitorBuddy(this,options)));				
			}
			else if( t == "string" && monitorBuddy && monitorBuddy[method] )
			{
				monitorBuddy[method].apply(monitorBuddy,args);
			};
		});
		return this;
	};
	$.fn.monitorBuddy.Constructor = MonitorBuddy;
   $.fn.monitorBuddy.noConflict = function () {
        $.fn.monitorBuddy = oldBuddy;
        return this
    };
    
	// item - Inode
	var MonitorInode = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorInode.prototype.initialize = function()
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%"});
	};
	
	MonitorInode.prototype.addInode = function(name)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").appendTo($tr).append(name).css({"width":"80px"});
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		$div.css({"width":"100%","height":"160px"});
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"100px"});
		chart = new Chart($canvas,{
			"type" : "line",
			"labels" : [],
			"data" : {
				"datasets" : [{
					"label" : name,
					"fill" : false,
					"borderColor": "rgba(100,100,255,1)",
					"pointRadius" : 0,
					"data" : [],
				}],
			},
			"options" : {
				'responsive': true,
				'maintainAspectRatio': false,
				'legend' : {
					'display' : true,
					'position' : "top",
				},
				'scales' : {
	            'xAxes' : [{
	               'display' : true,
	            	}],
	            'yAxes' : [{
	            	'ticks' : {
	            		'min' : 0
	            		},
		  	         'display' : true,
	  	            }],		            	
	       	 	},
			},
		});
		this.options.charts[name] = chart;
		return chart;
	};
	

	MonitorInode.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for(var ts in data)
		{
			if( cb ) cb(ts);
			
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			var methods = data[ts];
			var inodes = methods[method];
			if( !inodes )
			{
				continue;
			};
			for( var name in inodes )
			{
				var inode = inodes[name];
				var value = inode[name];
				var c = this.addInode(name);
				c.data.labels.push(label);
				c.data.datasets[0].data.push(value);
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

	var oldInode = $.fn.monitorInode;
	$.fn.monitorInode = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorInode = $this.data("js.monitorInode");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorInode",(monitorInode=new MonitorInode(this,options)));				
			}
			else if( t == "string" && monitorInode && monitorInode[method] )
			{
				monitorInode[method].apply(monitorInode,args);
			};
		});
		return this;
	};
	$.fn.monitorInode.Constructor = MonitorInode;
   $.fn.monitorInode.noConflict = function () {
        $.fn.monitorInode = oldInode;
        return this
    };
    
    
	// item - Slab
	var MonitorSlab = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorSlab.prototype.update = function()
	{
		
	};

	var oldSlab = $.fn.monitorSlab;
	$.fn.monitorSlab = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorSlab = $this.data("js.monitorSlab");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorSlab",(monitorSlab=new MonitorSlab(this,options)));				
			}
			else if( t == "string" && monitorSlab && monitorSlab[method] )
			{
				monitorSlab[method].apply(monitorSlab,args);
			};
		});
		return this;
	};
	$.fn.monitorSlab.Constructor = MonitorSlab;
   $.fn.monitorSlab.noConflict = function () {
        $.fn.monitorSlab = oldSlab;
        return this
    };
    
    
	// item - Slub
	var MonitorSlub = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorSlub.prototype.update = function()
	{
		
	};

	var oldSlub = $.fn.monitorSlub;
	$.fn.monitorSlub = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorSlub = $this.data("js.monitorSlub");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorSlub",(monitorSlub=new MonitorSlub(this,options)));				
			}
			else if( t == "string" && monitorSlub && monitorSlub[method] )
			{
				monitorSlub[method].apply(monitorSlub,args);
			};
		});
		return this;
	};
	$.fn.monitorSlub.Constructor = MonitorSlub;
   $.fn.monitorSlub.noConflict = function () {
        $.fn.monitorSlub = oldSlub;
        return this
    };
    
    
	// item - Sock
	var MonitorSock = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
				'colors' : [
						    "rgba(255,0,0,1)",
						    "rgba(255,128,128,1)",
						    "rgba(0,255,0,1)",
						    "rgba(128,255,128,1)",
						    "rgba(0,0,255,1)",
						    "rgba(128,128,255,1)",
						    "rgba(255,255,0,1)",
						    "rgba(255,255,128,1)",
						    "rgba(255,0,255,1)",
						    "rgba(255,128,255,1)",
						    "rgba(0,255,255,1)",
						    "rgba(128,255,255,1)",
						    "rgba(0,0,0,1)",
						    "rgba(128,128,128,1)"
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
	
	MonitorSock.prototype.initialize = function()
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorSock.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		var colors = this.options["colors"];
		var datasets = [];
		$.each(fields,function(idx,field){
			datasets.push({
				"label" : field,
				"fill" : false,
				"borderColor" : colors[idx],
				"pointRadius" : 0,
				"data" : []
			});
		});
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").appendTo($tr).append(name).css({"width":"80px"});
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"160px"});
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
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	         'display' : true,
		  	            'ticks' : {
			  	            'min' : 0,
			  	              },
		  	            }],		            	
		       	 },
		 },
		});
		this.options.charts[name] = chart;
		return chart;
	},
	
	MonitorSock.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;

		for(var ts in data)
		{
			if( cb ) cb(ts);
			
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			
			var methods = data[ts];
			if( !methods )
			{
				continue;
			};
			var socks = methods[method];
			for(var name in socks )
			{
				var v = socks[name];
				var fields = v["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(v[field]);
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

	var oldSock = $.fn.monitorSock;
	$.fn.monitorSock = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorSock = $this.data("js.monitorSock");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorSock",(monitorSock=new MonitorSock(this,options)));				
			}
			else if( t == "string" && monitorSock && monitorSock[method] )
			{
				monitorSock[method].apply(monitorSock,args);
			};
		});
		return this;
	};
	$.fn.monitorSock.Constructor = MonitorSock;
   $.fn.monitorSock.noConflict = function () {
        $.fn.monitorSock = oldSock;
        return this
    };
    
    
	// item - Tcpip
	var MonitorTcpip = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'max' : 50,
				'charts' : {},
				'colors' : [
				    "rgba(255,0,0,1)",
				    "rgba(255,128,128,1)",
				    "rgba(0,255,0,1)",
				    "rgba(128,255,128,1)",
				    "rgba(0,0,255,1)",
				    "rgba(128,128,255,1)",
				    "rgba(255,255,0,1)",
				    "rgba(255,255,128,1)",
				    "rgba(255,0,255,1)",
				    "rgba(255,128,255,1)",
				    "rgba(0,255,255,1)",
				    "rgba(128,255,255,1)",
				    "rgba(0,0,0,1)",
				    "rgba(128,128,128,1)"
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
	

	MonitorTcpip.prototype.initialize = function() 
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorTcpip.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		var colors = this.options["colors"];
		var datasets = [];
		$.each(fields,function(idx,field){
			datasets.push({
				"label" : field,
				"fill" : false,
				"borderColor" : colors[idx],
				"pointRadius" : 0,
				"data" : []
			});
		});
		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").appendTo($tr).append(name);
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"320px"});
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
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	         'display' : true,
		  	            }],		            	
		       	 },
			},
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorTcpip.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;
		for(var ts in data)
		{
			if( cb ) cb(ts);
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			
			var methods = data[ts];
			var items = methods[method];
			if( !items )
			{
				continue;
			}
			for( var name in items )
			{
				var item = items[name];
				var fields = item["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(item[field]);
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

	var oldTcpip = $.fn.monitorTcpip;
	$.fn.monitorTcpip = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorTcpip = $this.data("js.monitorTcpip");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorTcpip",(monitorTcpip=new MonitorTcpip(this,options)));				
			}
			else if( t == "string" && monitorTcpip && monitorTcpip[method] )
			{
				monitorTcpip[method].apply(monitorTcpip,args);
			};
		});
		return this;
	};
	$.fn.monitorTcpip.Constructor = MonitorTcpip;
   $.fn.monitorTcpip.noConflict = function () {
        $.fn.monitorTcpip = oldTcpip;
        return this
    };
    
    
	// item - NfsClient
	var MonitorNfsClient = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
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
						    "rgba(0,0,0,1)",
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
	
	MonitorNfsClient.prototype.initialize = function()
	{
		$("<table/>").appendTo(this.$element).css({"width":"100%","height":"100%"});
	};
	
	MonitorNfsClient.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		var colors = this.options["colors"];
		var datasets = [];
		$.each(fields,function(idx,field){
			datasets.push({
				"label" : field,
				"fill" : false,
				"borderColor" : colors[idx],
				"pointRadius" : 0,
				"data" : []
			});
		});

		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(name).appendTo($tr).css({"width":"80px"});
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"320px"});
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
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorNfsClient.prototype.update = function(method,data,cb)
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;

		for(var ts in data)
		{
			// callback
			if( cb ) cb(ts);
			// create label
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			// 
			var methods = data[ts];
			var client = methods[method];
			if( !client )
			{
				continue;
			};
			for( var name in client )
			{
				var v = client[name];
				var fields = v["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(v[field]);
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

	var oldNfsClient = $.fn.monitorNfsClient;
	$.fn.monitorNfsClient = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorNfsClient = $this.data("js.monitorNfsClient");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorNfsClient",(monitorNfsClient=new MonitorNfsClient(this,options)));				
			}
			else if( t == "string" && monitorNfsClient && monitorNfsClient[method] )
			{
				monitorNfsClient[method].apply(monitorNfsClient,args);
			};
		});
		return this;
	};
	$.fn.monitorNfsClient.Constructor = MonitorNfsClient;
   $.fn.monitorNfsClient.noConflict = function () {
        $.fn.monitorNfsClient = oldNfsClient;
        return this
    };
    
    
	// item - NfsServer
	var MonitorNfsServer = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'charts' : {},
				'max' : 50,
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
						    "rgba(0,0,0,1)",
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
	
	MonitorNfsServer.prototype.add = function(name,fields)
	{
		if( !this.options["charts"] ) this.options["charts"] = {};
		var chart = this.options.charts[name];
		if( chart )
		{
			return chart;
		};
		var colors = this.options["colors"];
		var datasets = [];
		$.each(fields,function(idx,field){
			datasets.push({
				"label" : field,
				"fill" : false,
				"borderColor" : colors[idx],
				"pointRadius" : 0,
				"data" : []
			});
		});

		var $table = this.$element.find("table");
		var $tr = $("<tr/>").appendTo($table);
		$("<td/>").append(name).appendTo($tr).css({"width":"80px"});
		var $div = $("<div/>");
		$("<td/>").appendTo($tr).append($div);
		
		var $canvas = $("<canvas/>").appendTo($div).css({"width":"100%","height":"320px"});
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
					'display' : true,
					'position' : "top",
				},
				'scales' : {
		            'xAxes' : [{
		               'display' : true,
		            	}],
		            'yAxes' : [{
			  	          'display' : true,
		  	            }],		            	
		       	 },
		 },
		});
		this.options.charts[name] = chart;
		return chart;
	};
	
	MonitorNfsServer.prototype.update = function()
	{
		var max = this.options["max"];
		if( !max ) max = defMAX;

		for(var ts in data)
		{
			// callback
			if( cb ) cb(ts);
			// create label
			var timestamp = new Date();
			timestamp.setTime(ts);
			var label = timestamp.format("hh:mm:ss");
			// 
			var methods = data[ts];
			var client = methods[method];
			if( !client )
			{
				continue;
			};
			for( var name in client )
			{
				var v = client[name];
				var fields = v["fields"];
				var c = this.add(name,fields);
				c.data.labels.push(label);
				$.each(fields,function(idx,field){
					c.data.datasets[idx].data.push(v[field]);
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

	var oldNfsServer = $.fn.monitorNfsServer;
	$.fn.monitorNfsServer = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorNfsServer = $this.data("js.monitorNfsServer");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorNfsServer",(monitorNfsServer=new MonitorNfsServer(this,options)));				
			}
			else if( t == "string" && monitorNfsServer && monitorNfsServer[method] )
			{
				monitorNfsServer[method].apply(monitorNfsServer,args);
			};
		});
		return this;
	};
	$.fn.monitorNfsServer.Constructor = MonitorNfsServer;
   $.fn.monitorNfsServer.noConflict = function () {
        $.fn.monitorNfsServer = oldNfsServer;
        return this
    };
    
	// item - LustreClient
	var MonitorLustreClient = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorLustreClient.prototype.update = function()
	{
		
	};

	var oldLustreClient = $.fn.monitorLustreClient;
	$.fn.monitorLustreClient = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorLustreClient = $this.data("js.monitorLustreClient");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorLustreClient",(monitorLustreClient=new MonitorLustreClient(this,options)));				
			}
			else if( t == "string" && monitorLustreClient && monitorLustreClient[method] )
			{
				monitorLustreClient[method].apply(monitorLustreClient,args);
			};
		});
		return this;
	};
	$.fn.monitorLustreClient.Constructor = MonitorLustreClient;
   $.fn.monitorLustreClient.noConflict = function () {
        $.fn.monitorLustreClient = oldLustreClient;
        return this
    };
    
	// item - LustreMds
	var MonitorLustreMds = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorLustreMds.prototype.update = function()
	{
		
	};

	var oldLustreMds = $.fn.monitorLustreMds;
	$.fn.monitorLustreMds = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorLustreMds = $this.data("js.monitorLustreMds");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorLustreMds",(monitorLustreMds=new MonitorLustreMds(this,options)));				
			}
			else if( t == "string" && monitorLustreMds && monitorLustreMds[method] )
			{
				monitorLustreMds[method].apply(monitorLustreMds,args);
			};
		});
		return this;
	};
	$.fn.monitorLustreMds.Constructor = MonitorLustreMds;
   $.fn.monitorLustreMds.noConflict = function () {
        $.fn.monitorLustreMds = oldLustreMds;
        return this
    };
    
	// item - LustreOss
	var MonitorLustreOss = function(element,options) {
		this.$element = $(element);
		this.defaultOptions = {
				'cpus' : {},
				'max' : 50,
		};
		this.options = $.extend({},this.defaultOptions,options);
		var innerWidth = this.$element.innerWidth();
		if( innerWidth < this.options["max"] )
		{
			this.$element.innerWidth(this.options["max"]);
		};		
		this.initialize();
	};
	
	MonitorLustreOss.prototype.update = function()
	{
		
	};

	var oldLustreOss = $.fn.monitorLustreOss;
	$.fn.monitorLustreOss = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorLustreOss = $this.data("js.monitorLustreOss");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorLustreOss",(monitorLustreOss=new MonitorLustreOss(this,options)));				
			}
			else if( t == "string" && monitorLustreOss && monitorLustreOss[method] )
			{
				monitorLustreOss[method].apply(monitorLustreOss,args);
			};
		});
		return this;
	};
	$.fn.monitorLustreOss.Constructor = MonitorLustreOss;
   $.fn.monitorLustreOss.noConflict = function () {
        $.fn.monitorLustreOss = oldLustreOss;
        return this
    };
	var MonitorCluster = function(element,options) {
			this.$element = $(element);
			this.defaultOptions = {
					'gap' : 4,
					'side' : 220,
					'start' : 10,
					'rows' : 1,
					'cols' : 3,
					'autoRange' : true,
					'low' : 'rgba(0,255,0,0.5)',
					'high' : 'rgba(255,0,0,0.5)',
					'middle' : 'rgba(255,255,65,0.5)',
					'hosts' : [],
					'items' : {}
			};
			this.options = $.extend({},this.defaultOptions,options);
			this.initialize();
	};
	

	
	MonitorCluster.prototype.initialize = function()
	{
		this.$element.empty();
		if( this.options["autoRange"] )
		{
			var w = this.$element.innerWidth();
			var h = this.$element.innerHeight();
			var cols = Math.floor(w/this.options["side"]);
			if( cols < 2 ) { cols = 2 };
			this.options["cols"] = cols;
			var rows = Math.floor(h/this.options["side"]);
			if( rows < 1 ) { rows = 1; };
			this.options["rows"] = rows;
		};
		this.hosts = [];
		this.items = [];
	};
	
	MonitorCluster.prototype.countHosts = function(cb)
	{
		var size = this.options["rows"] * this.options["cols"];
		cb(size);
	};
	
	MonitorCluster.prototype.addHost = function(hostname,cb)
	{
		var idx = this.options["hosts"].length;
		this.options["hosts"].push(hostname);
		
		var row = Math.floor(idx/this.options["cols"]);
		var col = Math.floor(idx % this.options["cols"]);
		var x = this.options["start"] + col*this.options["side"] + col*this.options["gap"];
		var y = this.options["start"] + row*this.options["side"] + row*this.options["gap"];
		var side = this.options["side"]-2;
		var padding = Math.floor(side*0.05);
		var doublePadding = 2*padding;
		var hostnameHeight = 20;
		var height = Math.floor((side-hostnameHeight)/2) - padding;
		var netWidth = Math.floor(side*0.5) - doublePadding;
		var ibWidth = Math.floor(side*0.25) - doublePadding;
		var memWidth = Math.floor(side*0.25);
//		var netWidth = Math.floor(side*0.3);
//		var ibWidth = Math.floor(side*0.2);
//		var gapWidth = Math.floor(side*0.1);
//		var memWidth = side - netWidth - ibWidth - 2*gapWidth;
		//var ibx = netWidth + gapWidth;
		
		//var div = $("<div/>").appendTo(this.$element).css({"position":"absolute","top":y+"px","left":x+"px","width":side+"px","height":side+"px","border":"1px solid white"});
		//this.options["items"][hostname] = div;
		var table = "<table style='border-collapse:collapse;border:1px solid white;'>" +
				      "  <tr style='height:<%=height%>px;vertical-align:bottom;'>" +
				      "    <td style='width:<%=netWidth%>px;border:1px solid white;padding:<%=padding%>px <%=padding%>px 0px <%=padding%>px;'><div class='net' style='width:<%=netWidth%>px;height:<%=height%>px;'></div></td>" +
				      "    <td style='width:<%=ibWidth%>px;border:1px solid white;padding:<%=padding%>px <%=padding%>px 0px <%=padding%>px;'><div class='ib' style='width:<%=ibWidth%>px;height:<%=height%>px;'></div></td>" +
				      "    <td style='width:<%=memWidth%>px;border:1px solid white;padding:<%=padding%>px 0px 0px 0px;'><div class='mem' style='width:<%=memWidth%>px;height:<%=height%>px;'></div></td>" +
				      "  </tr>" +
				      "  <tr style='height:<%=height%>px;'>" +
				      "    <td colspan='3' style='border:1px solid white;padding:<%=padding%>px <%=padding%>px 0px <%=padding%>px;'><div class='cpu' style='width:100%;height:<%=height%>px;'></div></td>" +
				      "  </tr>" +
				      "  <tr style='height:<%=hostnameHeight%>px;'>" +
				      "    <td colspan='3' style='border:1px solid white;text-align:center;color:white;'><%=hostname%></td>" +
				      "  </tr>" +
				      "</table>";
		var $table = $(_.template(table,{'hostname':hostname,'height':height,'hostnameHeight':hostnameHeight,"netWidth":netWidth,"ibWidth":ibWidth,"memWidth":memWidth,'padding':padding,'doublePadding':doublePadding}))
			.appendTo(this.$element)
			.css({"position":"absolute","top":y+"px","left":x+"px","width":side+"px","height":side+"px","border":"1px solid white"})
			.click(function(e){
				if( cb ) cb(hostname);
			});
		this.options["items"][hostname] = $table;
		

		var cpuDiv = $table.find(".cpu");
		var memDiv = $table.find(".mem");
		var netDiv = $table.find(".net");
		var ibDiv = $table.find(".ib");
		
//		var nameDiv = $("<div>").append(hostname).addClass("name").appendTo(div).css({"position":"absolute","left":"0px","bottom":"0px","right":"0px","height":"20px;","color":"white","text-align":"center"});
//		var cpuDiv = $("<div/>").addClass("cpu").appendTo(div).css({"position":"absolute","top":h+"px","left":"0px","right":"0px","bottom":"20px"});
//		var memDiv = $("<div/>").addClass("mem").appendTo(div).css({"position":"absolute","top":"0px","right":"0px","width":memWidth+"px","height":h+"px"});
//		var netDiv = $("<div/>").addClass("net").appendTo(div).css({"position":"absolute","left":"0px","top":"0px","width":netWidth+"px","height":h+"px"});
//		var ibDiv = $("<div/>").addClass("ib").appendTo(div).css({"position":"absolute","left":ibx+"px","top":"0px","width":ibWidth+"px","height":h+"px"});
		
		var cpu = $("<canvas/>").appendTo(cpuDiv).css({"width":"100%","height":"100%"});
		var mem = $("<canvas/>").appendTo(memDiv).css({"width":"100%","height":"100%"});
		var net = $("<canvas/>").appendTo(netDiv).css({"width":"100%","height":"100%"});
		var ib = $("<canvas/>").appendTo(ibDiv).css({"width":"100%","height":"100%"});
		var barOptions = {
			'responsive': true,
			'maintainAspectRatio': false,

			'scales' : {
	    	  'xAxes' : [{
	          'display' : false
	           }],
	        'yAxes' : [{
	          'display' : false,
	          'ticks' : {
	            'min' : 0,
	            'max' : 100
	              }
	           }]
	        },					    	
		};
		var cpuConf = {
				'type' : "bar",
				'options' : barOptions,
				'data' : {
					'labels' : [],
					'datasets' : [
					 {
					  'data' : [],
					  'backgroundColor' : []
					 }
					]
				},
		};
		var cpuChart = new Chart(cpu,cpuConf);
		cpuDiv.data("js.cpu",cpuChart);
		var memBarWidth = Math.floor(memWidth/2);
		var memOptions = $.extend({},barOptions,{'barThickness':memBarWidth});
		var memConf = {
				'type' : "bar",
				'options' : memOptions,
				'data' : {
					'labels' : ["memory","swap"],
					'datasets' : [
					 {
					  'data' : [],
					  'backgroundColor' : []
					 }
					]
				},
		};
		var memChart = new Chart(mem,memConf);
		memDiv.data("js.mem",memChart);
		var netBarOptions = {
				'responsive': true,
				'maintainAspectRatio': false,
			      'scales' : {
			    	  'xAxes' : [{
			          'display' : false
			           }],
			        'yAxes' : [{
			          'display' : true,
			          'ticks' : {
				            'min' : 0,
				            'max' : 100,
				              }
			           }]
			        },					    	
				};
		var netConf = {
				'type' : "bar",
				'options' : netBarOptions,
				'data' : {
					'labels' : [],
					'datasets' : [
					 {
						 // send
					  'data' : [],
					  'backgroundColor' : []
					 },
					 {
						 // receive
					  'data' : [],
					  'backgroundColor' : []
					 }
					]
				},
		};
		var netChart = new Chart(net,netConf);
		netDiv.data("js.net",netChart);
		
		var ibConf = {
				'type' : "bar",
				'options' : barOptions,
				'data' : {
					'labels' : [],
					'datasets' : [
					 {
						// send
					  'data' : [],
					  'backgroundColor' : []
					 },
					 {
					   // receive
					  'data' : [],
					  'backgroundColor' : []
					 }
					]
				},
		};
		var ibChart = new Chart(ib,ibConf);
		ibDiv.data("js.ib",ibChart);
	};
	
	MonitorCluster.prototype.addHosts = function(hostnames,cb)
	{
		var me = this;
		$.each(hostnames,function(idx,hostname){
			me.addHost.call(me,hostname,cb);
		});
	};
	
	MonitorCluster.prototype.getBackgroundColor = function(value)
	{
		if( value <= 50.00 )
		{
			return this.options["low"];
		}
		else if( value > 50.00 && value <= 90.00 )
		{
			return this.options["middle"];
		}
		else
		{
			return this.options["high"];
		};		
	};
	
	MonitorCluster.prototype.updateHost = function(d)
	{
		if( !d ) return;
		var me = this;
		var hostname = d["hostname"];
		var div = me.options["items"][d.hostname];
		if( !div )
		{
			return;
		};
		var cpuChart = div.find(".cpu").data("js.cpu");
		var memChart = div.find(".mem").data("js.mem");
		var netChart = div.find(".net").data("js.net");
		var ibChart = div.find(".ib").data("js.ib");
		
		if( d.cpuNames )
		{
			cpuChart.data.labels = d.cpuNames;
		};
		if( d.cpuUtilizationRatios )
		{
			cpuChart.data.datasets[0].data = d.cpuUtilizationRatios;
			$.each(d.cpuUtilizationRatios,function(idx,value){
				cpuChart.data.datasets[0].backgroundColor[idx] = me.getBackgroundColor.call(me,value);
			});
		};
		cpuChart.update();

		if( d.memUtilizationRatio )
		{
			memChart.data.datasets[0].data[0] = d.memUtilizationRatio;
			memChart.data.datasets[0].backgroundColor[0] = me.getBackgroundColor.call(me,d.memUtilizationRatio);
		};
		if( d.swpUtilizationRatio )
		{
			memChart.data.datasets[0].data[1] = d.swpUtilizationRatio;
			memChart.data.datasets[0].backgroundColor[1] = me.getBackgroundColor.call(me,d.swpUtilizationRatio);
		};
		memChart.update();
		
		if( d.netNames )
		{
			netChart.data.labels = d.netNames;
		};
		if( d.netWriteSpeeds )
		{
			netChart.data.datasets[0].data = d.netWriteSpeeds;
			$.each(d.netWriteSpeeds,function(idx,value){
				netChart.data.datasets[0].backgroundColor[idx] = me.getBackgroundColor.call(me,value);
			});
		};
		if( d.netReadSpeeds )
		{
			netChart.data.datasets[1].data = d.netReadSpeeds;
			$.each(d.netReadSpeeds,function(idx,value){
				netChart.data.datasets[1].backgroundColor[idx] = me.getBackgroundColor.call(me,value);
			});
		};
		netChart.update();
		
		if( d.ibNames )
		{
			ibChart.data.labels = d.ibNames;
		};
		if( d.ibWriteSpeeds )
		{
			ibChart.data.datasets[0].data = d.ibWriteSpeeds;
			$.each(d.ibWriteSpeeds,function(idx,value){
				ibChart.data.datasets[0].backgroundColor[idx] = me.getBackgroundColor.call(me,value);
			});
		};
		if( d.ibReadSpeeds )
		{
			ibChart.data.datasets[1].data = d.ibReadSpeeds;
			$.each(d.ibReadSpeeds,function(idx,value){
				ibChart.data.datasets[1].backgroundColor[idx] = me.getBackgroundColor.call(me,value);
			});
		};
		ibChart.update();
	};
	

	
	// CLuster
	var oldCluster = $.fn.monitorCluster;
	$.fn.monitorCluster = function(method) {
		var args = Array.prototype.slice.call(arguments,1);
		this.each(function(){
			var $this = $(this);
			var monitorCluster = $this.data("js.monitorCluster");
			var t = typeof method;
			if( t == "object" )
			{
				var options = method;
				$this.data("js.monitorCluster",(monitorCluster=new MonitorCluster(this,options)));
			}
			else if( t == "string" && monitorCluster && monitorCluster[method] )
			{
				monitorCluster[method].apply(monitorCluster,args);
			};
		});
		return this;
	};
	$.fn.monitorCluster.Constructor = MonitorCluster;
   $.fn.monitorCluster.noConflict = function () {
        $.fn.monitorCluster = oldCluster;
        return this
    };

})(jQuery);