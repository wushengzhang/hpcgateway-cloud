/* @version 2.1 fixedMenu
 * @author Lucas Forchino
 * @webSite: http://www.jqueryload.com
 * jquery top fixed menu
 */
(function($){
    $.fn.fixedMenu=function(){
        return this.each(function(){
			var linkClicked= false;
            var menu= $(this);
			$('body').bind('click',function(){
			
					if(menu.find('.fixedmenu_active').size()>0 && !linkClicked)
					{
						menu.find('.fixedmenu_active').removeClass('fixedmenu_active');
					}
					else
					{
						linkClicked = false; 
					}
			});
			
            menu.find('ul li > a').bind('click',function(){
				linkClicked = true;
				if ($(this).parent().hasClass('fixedmenu_active')){
					$(this).parent().removeClass('fixedmenu_active');
				}
				else{
					$(this).parent().parent().find('.fixedmenu_active').removeClass('fixedmenu_active');
					$(this).parent().addClass('fixedmenu_active');
				}
				var href = $(this).attr("href");
				if( href != "#" )
				{
					menu.find('.fixedmenu_active').removeClass('fixedmenu_active');
				};
            })
        });
    }
})(jQuery);