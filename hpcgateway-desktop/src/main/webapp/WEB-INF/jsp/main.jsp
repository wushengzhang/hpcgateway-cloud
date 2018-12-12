<%@page language="java" pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  

<!DOCTYPE html> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>HPCGATEWAY</title>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/elfinder.full.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/theme.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/simplePagination.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/hpcgateway.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/reset.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/fixedmenu.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/zTreeStyle.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/editable.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/default.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/demo.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/desktop.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/xterm.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/xterm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/addons/attach/attach.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/addons/fit/fit.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/underscore.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/Chart.bundle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.simplePagination.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.i18n.properties.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/backbone.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.desktop.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.fixedMenu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.editTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.monitor-cluster.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/jquery.monitor-host.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/elfinder.full.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/i18n/elfinder.zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/lib/tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/wp/jquery.ztmpl.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wp/jquery.zload.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wp/init.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wp/view.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wp/app.js"></script>
</head>
<body>

<div class="abs" id="wrapper">
  <div class="abs" id="desktop"></div>
  <div class="abs" id="bar_top">
    <span class="float_right" id="clock"></span>
    <ul>
      <li>
        <a class="menu_trigger" id="menu_system" href="#">System</a>
        <ul class="menu">
          <li>
            <a href="#profile" class="menu_item"><img width="16" height="16" style="margin-left:-3px;margin-right:3px;" src="" alt="X"><span id="menu_profile">Profile</span></a>
          </li>
          <li>
            <a href="<%=request.getContextPath()%>/wp/page/dologout" target="_self" class="menu_item"><span id="menu_logout" style="margin-left:16px;">Logout</span> <label id="userID"></label></a>
          </li>
        </ul>
      </li>
      <li>
        <a class="menu_trigger" id="menu_help" href="#help">Help</a>
        <ul class="menu">
          <li>
            <a href="#about" class="menu_item"><img width="16" height="16" style="margin-left:-3px;margin-right:3px;" src="" alt="X"><span id="menu_about">About CoS Desktop</span></a>
          </li>
        </ul>
      </li>
    </ul>
  </div>
 
  <div class="abs" id="bar_bottom">
    <a class="float_left" href="#" id="show_desktop" title="Show Desktop">
      <img src="<%=request.getContextPath()%>/images/icons/icon_22_desktop.png" />
    </a>
    <ul id="dock">

    </ul>
  </div>
</div>

<!-- finally, try to initialize the JSP page-->
<script type="text/javascript">

$(document).ready(function() {
	// store the context path
	app.contextPath = "<%=request.getContextPath()%>";

	// load modules
	<c:forEach items="${modules}" var="m">
		app.funcs.loadModule("${m.value.jsp}","${m.value.prefix}","${m.value.module}");
	</c:forEach>
});

</script>
<!-- the end of initialization -->

</body>
</html>
