//
// Namespace - Module Pattern.
//
var JQD = (function($, window, document, undefined) {
  // Expose innards of JQD.
  return {
    go: function() {
      for (var i in JQD.init) {
        JQD.init[i]();
      }
    },
    init: {
      frame_breaker: function() {
        if (window.location !== window.top.location) {
          window.top.location = window.location;
        }
      },
      //
      // Initialize the clock.
      //
      clock: function() {
        var clock = $('#clock');

        if (!clock.length) {
          return;
        }

        // Date variables.
        var date_obj = new Date();
        var hour = date_obj.getHours();
        var minute = date_obj.getMinutes();
        var day = date_obj.getDate();
        var year = date_obj.getFullYear();
        var suffix = 'AM';

        // Array for weekday.
        var weekday = [
          'Sunday',
          'Monday',
          'Tuesday',
          'Wednesday',
          'Thursday',
          'Friday',
          'Saturday'
        ];

        // Array for month.
        var month = [
          'January',
          'February',
          'March',
          'April',
          'May',
          'June',
          'July',
          'August',
          'September',
          'October',
          'November',
          'December'
        ];

        // Assign weekday, month, date, year.
        weekday = weekday[date_obj.getDay()];
        month = month[date_obj.getMonth()];

        // AM or PM?
        if (hour >= 12) {
          suffix = 'PM';
        }

        // Convert to 12-hour.
        if (hour > 12) {
          hour = hour - 12;
        }
        else if (hour === 0) {
          // Display 12:XX instead of 0:XX.
          hour = 12;
        }

        // Leading zero, if needed.
        if (minute < 10) {
          minute = '0' + minute;
        }

        // Build two HTML strings.
        var clock_time = weekday + ' ' + hour + ':' + minute + ' ' + suffix;
        var clock_date = month + ' ' + day + ', ' + year;

        // Shove in the HTML.
        clock.html(clock_time).attr('title', clock_date);

        // Update every 60 seconds.
        setTimeout(JQD.init.clock, 60000);
      },
      //
      // Initialize the desktop.
      //
      desktop: function() {
        // Alias to document.
        var d = $(document);
        
        /*d.on('mouseover',function(e){
           var box=$("div.window");
           var oLeft=box.offset().left;
           var oTop=box.offset().top;
           var w=box.width();
           var h=box.height();
           var x=e.pageX;
           var y=e.pageY;
           var changeX=function(e){console.log("changeX")
                    var dX=parseInt(w)-e.pageX;
                    var changeSize=function(e){
                        var cw=dX+e.pageX;
                        box.css("width",cw);
                    };
                    d.on('mousemove',changeSize); 
                    d.on('mouseup',function(e){
                        d.unbind('mousemove',changeSize);
                        d.unbind('mousedown',changeX);
                        d.unbind('mousedown',changeY);
                        box.css("cursor","default");
                    }); 
                };
           var changeY=function(e){
                    var dY=parseInt(h)-e.pageY;
                    var changeSize=function(e){
                        var ch=dY+e.pageY;        
                        box.css("height",ch);
                    };
                    d.on('mousemove',changeSize); 
                    d.on('mouseup',function(e){
                        d.unbind('mousemove',changeSize);
                        d.unbind('mousedown',changeY);
                        d.unbind('mousedown',changeX);
                        box.css("cursor","default");
                    }); 
                };
           if(x==oLeft){
               box.css("cursor","w-resize");
               d.on('mousedown',changeX);
           }else if(y==oTop){
               box.css("cursor","n-resize");
               d.on('mousedown',changeY);
           }else if(x==oLeft+w){
               box.css("cursor","e-resize");
               d.on('mousedown',changeX);
           }else if(y==oTop+h){
               box.css("cursor","s-resize");
               d.on('mousedown',changeY); 
           }
        });*/
        // Cancel mousedown.
        d.mousedown(function(ev) {
          var tags = [
            'a',
            'button',
            'input',
            'select',
            'textarea',
            'tr'
          ].join(',');

          if (!$(ev.target).closest(tags).length) {
            JQD.util.clear_active();
            ev.preventDefault();
            ev.stopPropagation();
          }
        });

        // Cancel right-click.
        d.on('contextmenu', function() {
          return false;
        });

        // Relative or remote links?
        d.on('click', 'a', function(ev) {
          var url = $(this).attr('href');
          this.blur();

          if (url.match(/^#/)) {
            ev.preventDefault();
            ev.stopPropagation();
          }
          else {
            $(this).attr('target', '_blank');
          }
        });

        // Make top menus active.
        d.on('mousedown', 'a.menu_trigger', function() {
          if ($(this).next('ul.menu').is(':hidden')) {
            JQD.util.clear_active();
            $(this).addClass('active').next('ul.menu').show();
          }
          else {
            JQD.util.clear_active();
          }
        });
        // Transfer focus, if already open.
        d.on('mouseenter', 'a.menu_trigger', function() {
          if ($('ul.menu').is(':visible')) {
            JQD.util.clear_active();
            $(this).addClass('active').next('ul.menu').show();
          }
        });

        // Cancel single-click.
        d.on('mousedown', 'a.icon', function() {
          // Highlight the icon.
          JQD.util.clear_active();
          $(this).addClass('active');
        });

        // Respond to double-click.
        d.on('dblclick', 'a.icon', function() {
          // Get the link's target.
          var x = $(this).attr('href');
          var y = $(x).find('a').attr('href');
          // Show the taskbar button.
          if ($(x).is(':hidden')) {
            $(x).remove().appendTo('#dock');
            $(x).show('fast');
          }
          // Bring window to front.
          JQD.util.window_flat();
          $(y).addClass('window_stack').show();
          //drag
          JQD.drag(y);
        });

        // Make icons draggable.
        d.on('mouseenter', 'a.icon', function() {
          $(this).off('mouseenter').draggable({
            revert: true,
            containment: 'parent'
          });
        });

        // Taskbar buttons.
        d.on('click', '#dock a', function() {
          // Get the link's target.
          var x = $($(this).attr('href'));
          // Hide, if visible.
          if (x.is(':visible')) {
            x.hide();
          }
          else {
            // Bring window to front.
            JQD.util.window_flat();
            x.show().addClass('window_stack');
          }
        });

        // Focus active window.
        d.on('mousedown', 'div.window', function() {
          // Bring window to front.
          JQD.util.window_flat();
          $(this).addClass('window_stack');
        });

        // Make windows draggable.
        d.on('mouseenter', 'div.window', function() {
          $(this).off('mouseenter').draggable({
            // Confine to desktop.
            // Movable via top bar only.
            cancel: 'a',
            containment: 'parent',
            handle: 'div.window_top'
          }).resizable({
            containment: 'parent',
            minWidth: 400,
            minHeight: 200
          });
        });

        // Double-click top bar to resize, ala Windows OS.
        d.on('dblclick', 'div.window_top', function() {
          JQD.util.window_resize(this);
        });

        // Double click top bar icon to close, ala Windows OS.
        d.on('dblclick', 'div.window_top img', function() {
          // Traverse to the close button, and hide its taskbar button.
          $($(this).closest('div.window_top').find('a.window_close').attr('href')).hide('fast');

          // Close the window itself.
          $(this).closest('div.window').hide();

          // Stop propagation to window's top bar.
          return false;
        });

        // Minimize the window.
        d.on('click', 'a.window_min', function() {
          $(this).closest('div.window').hide();
        });

        // Maximize or restore the window.
        d.on('click', 'a.window_resize', function() {
          JQD.util.window_resize(this);
        });

        // Close the window.
        //d.on('click', 'a.window_close', function() {
        //  $(this).closest('div.window').hide();
        //  $($(this).attr('href')).hide('fast');
        //});

        // Show desktop button, ala Windows OS.
        d.on('mousedown', '#show_desktop', function() {
          // If any windows are visible, hide all.
          if ($('div.window:visible').length) {
            $('div.window').hide();
          }
          else {
            // Otherwise, reveal hidden windows that are open.
            $('#dock li:visible a').each(function() {
              $($(this).attr('href')).show();
            });
          }
        });

        $('table.data').each(function() {
          // Add zebra striping, ala Mac OS X.
          $(this).find('tbody tr:odd').addClass('zebra');
        });

        d.on('mousedown', 'table.data tr', function() {
          // Clear active state.
          JQD.util.clear_active();

          // Highlight row, ala Mac OS X.
          $(this).closest('tr').addClass('active');
        });
        
//        d.on('resize', 'div.window#window_manager', function(){
//        	console.log(this);
//        });	
      },
      wallpaper: function() {
        // Add wallpaper last, to prevent blocking.
        if ($('#desktop').length) {
          var paths = window.location.pathname.split("/");
          var img = '<img id="wallpaper" class="abs" src="/'+paths[1]+'/images/misc/wallpaper.jpg" />';
          $('body').prepend(img);
        }
      }
    },
    util: {
      //
      // Clear active states, hide menus.
      //
      clear_active: function() {
        $('a.active, tr.active').removeClass('active');
        $('ul.menu').hide();
      },
      //
      // Zero out window z-index.
      //
      window_flat: function() {
        $('div.window').removeClass('window_stack');
      },
      //
      // Resize modal window.
      //
      window_resize: function(el) {
        // Nearest parent window.
        var win = $(el).closest('div.window');

        // Is it maximized already?
        if (win.hasClass('window_full')) {
          // Restore window position.
          win.removeClass('window_full').css({
            'top': win.attr('data-t'),
            'left': win.attr('data-l'),
            'right': win.attr('data-r'),
            'bottom': win.attr('data-b'),
            'width': win.attr('data-w'),
            'height': win.attr('data-h')
          	});
          $("a.window_resize").removeClass("re");
          }
        else 
          {
          win.attr({
            // Save window position.
            'data-t': win.css('top'),
            'data-l': win.css('left'),
            'data-r': win.css('right'),
            'data-b': win.css('bottom'),
            'data-w': win.css('width'),
            'data-h': win.css('height')
          }).addClass('window_full').css({
            // Maximize dimensions.
            'top': '0',
            'left': '0',
            'right': '0',
            'bottom': '0',
            'width': '100%',
            'height': '100%'
          });
          $("a.window_resize").addClass("re");
        }

        // Bring window to front.
        JQD.util.window_flat();
        win.addClass('window_stack');
        win.trigger("resize");
      }
    },
    
	templates : {
		
		icon :   "<a id=\"desktop_icon_<%=id%>\" class=\"abs icon\" style=\"left:120px;top:20px;\" href=\"#icon_dock_<%=id%>\">"+
	             "  <img src=\"<%=icon%>\" width=\"32\" height=\"32\"/>"+
	             "  <label><%=name%></label>"+
                 "</a>",
        
        bar :    "<li id=\"icon_dock_<%=id%>\">" +
                 "  <a href=\"#window_<%=id%>\">" + 
                 "    <img src=\"<%=icon%>\" width=\"22\" height=\"22\"/>"+
                 "    <%=name%>"+
                 "  </a>"+
                 "</li>",
               
        window : "<div id=\"window_<%=id%>\" class=\"abs window\">" +
                 "  <div class=\"abs window_inner\">" +
                 "    <div class=\"window_top\">" +
                 "      <span class=\"float_left\">" +
                 "        <img src=\"<%=icon%>\" width=\"16\" height=\"16\"/>" +
                 "        <span class='<%=id%>'><%=name%></span>"+
                 "      </span>" +
                 "      <span class=\"float_right\">" +
                 "        <a href=\"#\" class=\"window_min\"></a>" +
                 "        <a href=\"#\" class=\"window_resize\"></a>" +
                 "        <a href=\"#icon_dock_<%=id%>\" class=\"window_close\"></a>" +
                 "      </span>" +
                 "    </div>" +
                 "    <div class=\"abs window_content basin\">" +
                 "    </div>"+
                 "    <div class=\"abs window_bottom\">" +
                 "    </div>" +
                 "  </div>" +
                 "  <span class=\"abs ui-resizable-handle ui-resizable-se\" style=\"cursor:se-resize\"></span>" +
                 "</div>"    
	},
	
	registerResizeListener:function(id,callback)
	{
		var d = $(document);
        d.on('resize', 'div.window#'+id, function(){
        	if(callback!=undefined){
        		callback($(this));
        	};
        });	
	},
	
    drag:function(box){
    	  var win = $(box).closest('div.window');
        var flag=false; 
        var dragFn=function(e){
                flag=true;
                l=e.pageX-$(box).offset().left;
                t=e.pageY-parseInt($(box).css("top"));
            };
            var d=$(document);
            d.on("mousedown",box+" div.window_top",dragFn);
            d.mousemove(function(e){
                if(flag){
                    X=e.pageX-l;
                    Y=e.pageY-t;
                    $(box).css("left",X);
                    $(box).css("top",Y);
                    $(box).css("cursor","move");
                }
            }).mouseup(function(){
                flag=false;
                $(box).css("cursor","default");
            });
            //drag change size
            d.on('mousedown',''+box+' span.ui-resizable-se',function(e){
                var dX=parseInt($(box).css("width"))-e.pageX,
                    dY=parseInt($(box).css("height"))-e.pageY;
                var changeSize=function(e){
                    var w=dX+e.pageX;
                    var h=dY+e.pageY;
                    $(box).css("width",w);
                    $(box).css("height",h);
                    win.trigger("resize");
                		};
                d.on('mousemove',changeSize); 
                d.on('mouseup',function(e){
                	d.unbind('mousemove',changeSize);
                	}); 
            	});
    },
    
   open : function(obj)
    {
		var dock = $("div#bar_bottom ul#dock");
		if( dock.find("#icon_dock_"+obj.id).length > 0 )
		{
			return;
		};
		var bar = $(_.template(JQD.templates.bar,obj)).appendTo(dock);
		var winbody = _.template(JQD.templates.window,obj);
		var win = $(winbody).appendTo(desktop);
		win.find("a.window_close").click(function(evt){
			if( obj.shouldClose === undefined || obj.shouldClose() )
			{
				bar.hide("fast").remove();
				win.hide().find("div.basin").empty();
				win.remove();
				if( obj.close !== undefined )
				{
					obj.close.call(obj);
				};
			};
		});
      
      // Show the taskbar button.
      if( bar.is(':hidden') ) 
        {
          bar.remove().appendTo('#dock');
          bar.show('fast');
        }
      
		// Bring window to front.
		JQD.util.window_flat();
		win.addClass('window_stack').show();
		//drag
		JQD.drag(win.attr("id"));
        
		if( obj.render )
		{
			obj.render.call(obj,win);
		};
    },
    
	register: function(obj)
	{
		var me = this;
		var desktop = $("div#desktop");
		var iconNum = $("a.icon").length;
		var TotalRows = 6;
		var cols = Math.floor(iconNum / TotalRows);
		var rows = iconNum % TotalRows;
		var left = cols*100+20;
		var top = rows*80+20;
		var icon = $(_.template(JQD.templates.icon,obj)).css({"left":left+"px","top":top+"px"}).appendTo(desktop);
		icon.dblclick(function(evt){
			evt.preventDefault();
			me.open.call(me,obj);
		});
	},
	
	setIconNames: function(names)
	{
		var desktop = $("div#desktop");
		$.each(names,function(idx,obj){
			desktop.find("#desktop_icon_"+obj.id+" > label").empty().append(obj.name);
		});
	},

  };
// Pass in jQuery.
})(jQuery, this, this.document);

