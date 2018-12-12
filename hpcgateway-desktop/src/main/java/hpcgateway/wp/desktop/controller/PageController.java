package hpcgateway.wp.desktop.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import hpcgateway.wp.acl.Authentication;
import hpcgateway.wp.desktop.service.LoginService;
import hpcgateway.wp.metaconfig.service.MetadataService;
import hpcgateway.wp.orm.beans.CloudGroup;
import hpcgateway.wp.orm.beans.CloudUser;


@Controller
@Scope("request")
@RequestMapping(value = "/page")
public class PageController
{
	final public static String JSP_MAIN = "main";
	
	final public static String TYPE_TEMPLATE = "template";
	final public static String TYPE_ICON = "icon";
	final public static String TYPE_PROGRAM = "program";
	final public static String TYPE_CSS = "css";
	final public static String TYPE_LANGUAGE = "language";
	
	final public static String FILE_TEMPLATE = "template.html";
	final public static String FILE_ICON = "icon.png";
	final public static String FILE_PROGRAM = "module.js";
	final public static String FILE_CSS = "module.css";

	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static Map<String,String> fileMap;
	
	@Resource
	private SessionController sessionController;
	@Resource
	private MetadataService metadataService;
	@Resource
	protected ThreadPoolTaskExecutor threadpool;
	@Resource
	private LoginService loginService;
	
	static
	{
		fileMap = new HashMap<String,String>();
		fileMap.put(TYPE_TEMPLATE, FILE_TEMPLATE);
		fileMap.put(TYPE_ICON, FILE_ICON);
		fileMap.put(TYPE_PROGRAM, FILE_PROGRAM);
		fileMap.put(TYPE_CSS, FILE_CSS);
	}
	
	private boolean copyProfile(File src,HttpServletResponse response) throws IOException
	{
		if( !src.exists() || !src.isFile() )
		{
			return false;
		}

		OutputStream out = null;
		FileInputStream in = null;

		try
		{
			out = response.getOutputStream();
			in = new FileInputStream(src);
			
			byte[] buffer = new byte[1024];
			int length = in.read(buffer);
			while( length >= 0 )
			{
				if( length > 0 )
				{
					out.write(buffer,0,length);
				}
				length = in.read(buffer);
			}
			in.close();
			out.close();
			return true;
		}
		finally
		{
			if( in != null )
			{
				try { in.close(); } catch(IOException e) {}				
			}
			if( out != null )
			{
				try { out.close(); } catch(IOException e) {}
			}
		}
	}
	
	@RequestMapping(value="/module",method=RequestMethod.GET,produces="text/html")
	public void downloadModuleContent(
			@RequestParam(value="t",required=true) String type,
			@RequestParam(value="jsp",required=true) String jsp,
			@RequestParam(value="p",required=true) String prefix,
			@RequestParam(value="m",required=true) String module,
			@RequestParam(value="f",required=false) String file,
			HttpServletRequest request,
			HttpServletResponse response
	)
	throws Exception
	{
		String filename = null;
		if( type.compareTo(TYPE_TEMPLATE) == 0 )
		{
			response.addHeader("Content-Type", "text/html;charset=UTF-8");
			filename = fileMap.get(type);
		}
		else if( type.compareTo(TYPE_ICON) == 0 )
		{
			response.addHeader("Content-Type", "image/png");
			filename = fileMap.get(type);
		}
		else if( type.compareTo(TYPE_PROGRAM) == 0 )
		{
			response.addHeader("Content-Type", "application/x-javascript;charset=UTF-8");
			filename = fileMap.get(type);
		}
		else if( type.compareTo(TYPE_CSS) == 0 )
		{
			response.addHeader("Content-Type", "text/css;charset=UTF-8");
			filename = fileMap.get(type);
		}
		else if( fileMap.containsKey(type) )
		{
			filename = fileMap.get(type);
		}
		else if( type.compareTo(TYPE_LANGUAGE) == 0 )
		{
			response.addHeader("Content-Type", "application/x-javascript;charset=UTF-8");
			filename = file;
		}
		else if( file != null )
		{
			filename = String.format("%s.%s", file,type);
		}
		
//		boolean success = false;
		
		if( filename != null && !filename.isEmpty() )
		{
			String relativePath = String.format("/WEB-INF/profile/%s/%s/%s",jsp,prefix,module.replace('.', '/'));
			String path = request.getServletContext().getRealPath(relativePath);			
			copyProfile(new File(path,filename),response);
		}
	
//		if( !success )
//		{
//			response.sendError(404, "Required resouce does not exist.");
//		}
	}
		
	@RequestMapping(value="/login")
	public ModelAndView goLogin(
			@CookieValue(value="user",required=false) String username
	){
		String message = sessionController.getMessage();
		ModelAndView modelAndView = new ModelAndView("login");
		if( username == null ) username = "";
		modelAndView.addObject("username",username);
		modelAndView.addObject("message",message);
		return modelAndView;
	}
	
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(
			@RequestParam(value="username",required=true) final String username,
			@RequestParam(value="password",required=true) final String password,
			HttpServletRequest request
	){
		if( username == null || username.isEmpty() )
		{
			sessionController.setMessage("username required");
			return "redirect:login";
		}

		// do logining
		try
		{
			CloudUser user = loginService.checkUser(username, password);
			List<CloudGroup> groups = metadataService.queryGroups(user.getId());
			Authentication authorization = new Authentication();
			authorization.put(Authentication.USER, user);
			authorization.put(Authentication.GROUPS, groups);
			
			sessionController.setAuthorization(authorization);
			
			// put http session used by the Interceptor
			request.getSession().setAttribute(SessionController.SESSION_KEY,sessionController);
			
			// redirect to JSP_MAIN
			return "redirect:"+JSP_MAIN;
		}
		catch(Exception ex)
		{
			logger.error(String.format("Logined failed, user: %s msg: %s",username,ex.getMessage()));
			sessionController.setMessage("Logined failed, "+ex.getMessage());
			return "redirect:login";
		}
				
	
	}
	
	
	static public class ModuleObject
	{
		private String jsp;
		private String prefix;
		private String module;
		public ModuleObject()
		{
			this("","","");
		}
		public ModuleObject(String jsp,String prefix,String module)
		{
			this.jsp = jsp;
			this.prefix = prefix;
			this.module = module;
		}
		public String getJsp()
		{
			return jsp;
		}
		public void setJsp(String jsp)
		{
			this.jsp = jsp;
		}
		public String getPrefix()
		{
			return prefix;
		}
		public void setPrefix(String prefix)
		{
			this.prefix = prefix;
		}
		public String getModule()
		{
			return module;
		}
		public void setModule(String module)
		{
			this.module = module;
		}		
	}
	
	private void scan(Map<String,ModuleObject> modules,int baseUrlLength,String jsp,String prefix,File dir)
	{
		List<File> dirs = new ArrayList<File>();
		for( File file : dir.listFiles() )
		{
			if( file.isDirectory() )
			{
				dirs.add(file);
			}			
			else if( file.isFile() && file.getName().compareTo("module.js") == 0 )
			{
				String module = dir.getAbsolutePath().substring(baseUrlLength).replace(File.separatorChar, '.');
				if( !modules.containsKey(module) )
				{
					ModuleObject mo = new ModuleObject();
					mo.setJsp(jsp);
					mo.setModule(module);
					mo.setPrefix(prefix);
					modules.put(module,mo);
				}
			}
		}
		
		for(File file : dirs)
		{
			scan(modules,baseUrlLength,jsp,prefix,file);
		}
	}
	
	private void scan(Map<String,ModuleObject> modules,String basePath,String jsp,String prefix)
	{
		String path = String.format("%s/%s/%s", basePath,jsp,prefix);
		File dir = new File(path);
		if( dir.exists() && dir.isDirectory() )
		{
			scan(modules,path.length()+1,jsp,prefix,dir);
		}
	}

	@RequestMapping(value="/main")
	public ModelAndView goMain(HttpServletRequest request,HttpServletResponse response)
	{
		// Current User & Group Information
		Authentication authorization = sessionController.getAuthentication();
		CloudUser user = (CloudUser)authorization.get(Authentication.USER);
		@SuppressWarnings("unchecked")
		List<CloudGroup> groups = (List<CloudGroup>)authorization.get(Authentication.GROUPS);
		//
		// Scan /WEB-INF/profile/main/{default|users|groups}
		//
		Map<String,ModuleObject> modules = new TreeMap<String,ModuleObject>();
		String jsp = JSP_MAIN;
		String basePath = request.getServletContext().getRealPath("/WEB-INF/profile");
		
		// users
		String prefix = String.format("users/%s", user.getName());
		scan(modules,basePath,jsp,prefix);
		
		// groups
		if( groups!=null && groups.size()>0 )
		{
			for(CloudGroup group : groups)
			{
				prefix = String.format("groups/%s", group.getName());
				scan(modules,basePath,jsp,prefix);
			}
		}
		
		// default
		prefix = "default";
		scan(modules,basePath,jsp,prefix);

		ModelAndView modelAndView = new ModelAndView(jsp);
		modelAndView.addObject("modules", modules);

		Cookie cookie = new Cookie("user",user.getName());  
		cookie.setMaxAge(60*60*24*7);//保留7天  
		response.addCookie(cookie);

		return modelAndView;
	}

	@RequestMapping(value="/dologout")
	public String doLogout(HttpServletRequest request)
	{
		request.getSession().removeAttribute(SessionController.SESSION_KEY);
		sessionController.setAuthorization(null);
		sessionController.setMessage("");
		return "redirect:login";
	}
}
