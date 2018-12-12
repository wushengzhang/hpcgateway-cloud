(function($){
	
	"use strict";
	
	   // MonitorCluster
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
		this.items = {};
		
		var hosts = this.options["hosts"];
		if( hosts && hosts.length > 0 )
		{
			this.addHosts(hosts);
		};
	};
	
	MonitorCluster.prototype.countHosts = function(cb)
	{
		var size = this.options["rows"] * this.options["cols"];
		cb(size);
	};
	
	MonitorCluster.prototype.addHost = function(hostname)
	{
		var me = this;
		var idx = this.hosts.length;
		this.hosts.push(hostname);
		
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
		//this.items[hostname] = div;
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
				if( me.options["select"] ) me.options.select(hostname);
			});
		this.items[hostname] = $table;
		

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
	
	MonitorCluster.prototype.addHosts = function(hostnames)
	{
		var me = this;
		$.each(hostnames,function(idx,hostname){
			me.addHost.call(me,hostname);
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
		var div = this.items[d.hostname];
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