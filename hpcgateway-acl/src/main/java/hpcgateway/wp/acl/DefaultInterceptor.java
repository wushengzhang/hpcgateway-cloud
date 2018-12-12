package hpcgateway.wp.acl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class DefaultInterceptor implements HandlerInterceptor,InitializingBean,AclInterceptor
{
	//private Logger logger = Logger.getLogger(DefaultInterceptor.class);
	
	static private AntPathMatcher antPathMatcher;
	
	/*
	 * exempting urls and prefixes
	 */
	private Set<String> exemptURLs;
	private Set<String> exemptPrefixes;
	private ReentrantLock exemptLock;

	/*
	 * store the mapping from name to script 
	 */
	private Map<String,Script> scriptMap;
	private ReentrantLock scriptLock;
	
	/*
	 * store the mapping from url to script
	 */
	private Map<String,Script> fastMap;
	private Map<String,Script> urlMap;
	private ReentrantLock cacheLock;
	
	/*
	 * 
	 */
	private Script defaultScript;
                                     
	/*
	 * pagePrefix is used to check if the url comes from a page
	 */
	private String pagePrefix;
	
	/*
	 * 
	 */
	private String prefix;
	
	/*
	 * loginPageName
	 */
	private String loginPageName;
	
	/*
	 * loginUrl = pagePrefix + "/" + loginPageName
	 */
	private String loginUrl;
	
	static
	{
		antPathMatcher= new AntPathMatcher();
	}
	
	public DefaultInterceptor()
	{
		this.exemptURLs = new TreeSet<String>();
		this.exemptPrefixes = new TreeSet<String>();
		this.exemptLock = new ReentrantLock();

		this.scriptMap = new TreeMap<String,Script>();
		this.scriptLock = new ReentrantLock();

		this.fastMap = new TreeMap<String,Script>();
		this.urlMap = new TreeMap<String,Script>();
		this.cacheLock = new ReentrantLock();
	}
	

	
	private void _registerMappings(String[] prefixes,String[] mappings,Pattern pattern,Script script,boolean overwrite)
	{		
		/*
		 * ignore if no request mapping is available
		 */
		if( mappings == null || mappings.length == 0 || script == null)
		{
			return;
		}
		
		/*
		 * allocate a new TreeMap if the urlMap is null
		 */
		if( urlMap == null )
		{
			urlMap = new TreeMap<String,Script>();
		}

		/*
		 * cache every prefix/mapping 
		 */
		for(String mapping : mappings)
		{
			if( mapping == null || mapping.isEmpty() )
			{
				continue;
			}

			for(String p : prefixes)
			{
				String seperator = "";
				if( !mapping.startsWith("/") ) seperator = "/";
				String url = String.format("%s%s%s", p, seperator, mapping);
				boolean ok = true;
				if( pattern != null )
				{
					Matcher matcher = pattern.matcher(url);
					ok = matcher.find();
				}
				if( ok && (overwrite || !urlMap.containsKey(url)) )
				{
					urlMap.put(url, script);
				}
			}
		}
	}
	
	private void registerMappings(String[] prefixes,String[] mappings,Pattern pattern,Script script,boolean overwrite)
	{
		this.cacheLock.lock();
		try
		{
			_registerMappings(prefixes,mappings,pattern,script,overwrite);
		}
		finally
		{
			this.cacheLock.unlock();
		}
	}

	
	private void _cache(String url,Script script)
	{
		if( this.fastMap == null ) {
			this.fastMap = new TreeMap<String,Script>();
		}
		this.fastMap.put(url, script);
	}
	
	private void cache(String url,Script script)
	{
		this.cacheLock.lock();
		try
		{
			_cache(url,script);
		}
		finally
		{
			this.cacheLock.unlock();
		}
	}
	
	private Script findScript(String path)
	{
		this.cacheLock.lock();
		try
		{
			Script script = null;
			if( this.fastMap!=null && this.fastMap.containsKey(path) )
			{
				script = this.fastMap.get(path);
			}
			else if( urlMap != null && !urlMap.isEmpty() )
			{
				for(String url : urlMap.keySet() )
				{
					if( antPathMatcher.match(url, path) )
					{
						script = urlMap.get(url);
						_cache(path, script);
						break;
					}
				}
			}
			return script;
		}
		finally
		{
			this.cacheLock.unlock();
		}
	}
	
	private void _registerExemptUrl(String url)
	{
		if( exemptURLs == null )
		{
			exemptURLs = new TreeSet<String>();
		}
		exemptURLs.add(url);
	}
	
	@Override
	public void registerExemptUrl(String url)
	{
		this.exemptLock.lock();
		try
		{
			_registerExemptUrl(url);
		}
		finally
		{
			this.exemptLock.unlock();
		}
	}
	
	@Override
	public void registerExemptUrls(List<String> urls)
	{
		this.exemptLock.lock();
		try
		{
			if( urls != null && !urls.isEmpty() )
			{
				for(String url : urls)
				{
					_registerExemptUrl(url);
				}
			}
		}
		finally
		{
			this.exemptLock.unlock();
		}		
	}
	
	@Override
	public void registerExemptUrls(String[] urls)
	{
		this.exemptLock.lock();
		try
		{
			if( urls != null && urls.length > 0 )
			{
				for(String url : urls)
				{
					_registerExemptUrl(url);
				}
			}
		}
		finally
		{
			this.exemptLock.unlock();
		}	
	}
	
	private void _registerExemptPrefix(String prefix)
	{
		if( this.exemptPrefixes == null )
		{
			this.exemptPrefixes = new TreeSet<String>();
		}
		this.exemptPrefixes.add(prefix);
	}
	
	@Override
	public void registerExemptPrefix(String prefix)
	{
		this.exemptLock.lock();
		try
		{
			_registerExemptPrefix(prefix);
		}
		finally
		{
			this.exemptLock.unlock();
		}	
	}
	
	@Override
	public void registerExemptPrefixes(List<String> prefixes)
	{
		
		this.exemptLock.lock();
		try
		{
			if( prefixes != null && !prefixes.isEmpty() )
			{
				for(String prefix : prefixes)
				{
					_registerExemptPrefix(prefix);
				}
			}
		}
		finally
		{
			this.exemptLock.unlock();
		}	
	}
	
	@Override
	public void registerExemptPrefixes(String[] prefixes)
	{
		
		this.exemptLock.lock();
		try
		{
			if( prefixes != null && prefixes != null )
			{
				for(String prefix : prefixes)
				{
					_registerExemptPrefix(prefix);
				}
			}
		}
		finally
		{
			this.exemptLock.unlock();
		}	
	}
	
	private boolean checkExempt(String path)
	{
		this.exemptLock.lock();
		try
		{
			if( exemptURLs != null && exemptURLs.contains(path) )
			{
				return true;
			}
			if( exemptPrefixes != null && !exemptPrefixes.isEmpty() )
			{
				for(String p : exemptPrefixes)
				{
					if( path.startsWith(p) )
					{
						_registerExemptUrl(path);
						return true;
					}
				}
			}
			return false;
		}
		finally
		{
			this.exemptLock.unlock();
		}
	}
	
	@Override
	public void registerScript(Script script)
	{
		if(script==null || script.getName()==null || script.getName().isEmpty())
		{
			return;
		}
		
		this.scriptLock.lock();
		try
		{
			if( scriptMap == null ) 
			{
				scriptMap = new TreeMap<String,Script>();
			}
			if( !scriptMap.containsKey(script.getName()) )
			{
				scriptMap.put(script.getName(), script);
			}
		}
		finally
		{
			this.scriptLock.unlock();
		}
	}
	
	private Script fetchAclScript(String scriptName)
	{
		this.scriptLock.lock();
		try
		{
			Script script = null;
			if( scriptMap != null && scriptMap.containsKey(scriptName) )
			{
				script = scriptMap.get(scriptName);
			}
			return script;
		}
		finally
		{
			this.scriptLock.unlock();
		}
	}
	
	private Script fetchAclScript(Method method)
	{
		ACL aclAnnotation = method.getAnnotation(ACL.class);
		if( aclAnnotation == null )
		{
			return null;
		}
		String sn = aclAnnotation.value();		
		if( sn == null || sn.isEmpty() )
		{
			return null;
		}
		
		Script script = null;
		this.scriptLock.lock();
		try
		{
			if( scriptMap.containsKey(sn) ) 
			{
				script = scriptMap.get(sn);
			}
		}
		finally
		{
			this.scriptLock.unlock();
		}
		return script;
	}
	

	
	@Override
	public void scanAcl(Class<?> clazz) throws Exception
	{
		scanAcl(clazz,null);
	}
	

	
	/*
	 * first, scan the controller designated by clazz
	 * then, find all methods annotated by @RequestMapping
	 * finally, register the urls in @RequestMapping in DefaultInterceptor
	 * 
	 * 	
	 * @see AclInterceptor#scanAcl(java.lang.Class, java.util.List)
	 */
	@Override
	public void scanAcl(Class<?> clazz,List<ScriptMapping> scriptMappings) throws Exception
	{
		/*
		 * it will quit if no method is available
		 */
		Method[] methods = clazz.getMethods();
		if( methods == null || methods.length == 0 )
		{
			return;
		}
		
		/*
		 * compile the regular express(optional)
		 */
		List<TreeMap<String,Object>> list = null;
		if( scriptMappings != null && !scriptMappings.isEmpty() )
		{
			list = new ArrayList<TreeMap<String,Object>>();
			for(ScriptMapping scriptMapping : scriptMappings)
			{
				String scriptName = scriptMapping.getScriptName();
				Script script = this.fetchAclScript(scriptName);
				if( script == null )
				{
					continue;
				}
				String urlPattern = scriptMapping.getUrlPattern();
				Pattern pattern = Pattern.compile(urlPattern);
				TreeMap<String,Object> item = new TreeMap<String,Object>();
				item.put("pattern", pattern);
				item.put("script", script);
				list.add(item);
			}
		}
			
		
		/*
		 * initialize all prefixes
		 */
		String[] prefixes = new String[] { "" };
		RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
		if( requestMapping != null )
		{
			String[] ps = requestMapping.value();
			if( ps != null && ps.length > 0 ) {
				prefixes = ps;
			}
		}

		for(Method method :methods)
		{
			// fetch all mapping(s)
			RequestMapping r = method.getAnnotation(RequestMapping.class);
			String[] mappings = r==null ? null : r.value();
			if( mappings == null || mappings.length == 0 )
			{
				continue;
			}
			
			if( list == null || list.isEmpty() )
			{
				Script script = fetchAclScript(method);
				registerMappings(prefixes,mappings,null,script,false);
			}
			else
			{
				for(TreeMap<String,Object> map : list)
				{
					Pattern pattern = (Pattern)map.get("pattern");
					Script script = (Script)map.get("script");
					registerMappings(prefixes,mappings,pattern,script,true);
				}
			}
		}
	}
	


	@Override
	public void configAcl(Class<?> clazz, AclConfiguration aclConfiguration) throws Exception
	{
		Collection<Script> scripts = aclConfiguration==null ? null : aclConfiguration.getScriptList();
		if( scripts != null && !scripts.isEmpty() )
		{
			for( Script script : scripts )
			{
				this.registerScript(script);
			}
		}
		List<ScriptMapping> scriptMappings = aclConfiguration==null ? null : aclConfiguration.getScriptMappings();	
		scanAcl(clazz,scriptMappings);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		String seperator = "";
		if( !this.loginPageName.startsWith("/") ) {
			seperator = "/";
		}
		this.loginUrl = this.pagePrefix + seperator + this.loginPageName;
	}

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String contextPath = request.getContextPath();
		String path = request.getRequestURI();
		if( contextPath!=null && !contextPath.isEmpty() && path.startsWith(contextPath) ) {
			path = path.substring(contextPath.length());
		}
		if( prefix!=null && !prefix.isEmpty() && path.startsWith(prefix) )
		{
			path = path.substring(prefix.length());
		}
		
		/*
		 * check if the url is an exemption
		 */
		if( checkExempt(path) )
		{
			return true;
		}
		
		/*
		 * check if the user is logined
		 */
		DefaultSessionController sessionController = (DefaultSessionController)request.getSession().getAttribute(DefaultSessionController.SESSION_KEY);
		Authentication authorization = null;
		if( sessionController==null || (authorization=sessionController.getAuthentication())==null )
		{
			if( path.startsWith(pagePrefix) )
		    {
			   response.sendRedirect(contextPath+prefix+loginUrl);  
		    }
			else
		   	{
			   response.sendError(500, "no permit");
		   	}
			return false; 
		}
			
		/*
		 * find a script for the url
		 */
		Script script = findScript(path);
		if( script == null )
		{
			// use the default script if no script is available
			script = defaultScript;
			// if the script is still null, the url will be rejected 
			if( script == null ) 
			{
				response.sendError(500, "no permit");
				return false;
			}
		}
		
		
		/*
		 * 
		 */
		RequestInfo req = new RequestInfo();
		req.setUrl(path);
		req.setMethod(request.getMethod());
		req.setPort(String.valueOf(request.getServerPort()));
		req.setProtocol(request.getProtocol());
		req.setParameters(request.getParameterMap());
		Map<String,Object> status = script.execute(authorization, req);
		Boolean result = false;
		if( status != null && status.containsKey("result") ) 
		{
			result = (Boolean)status.get("result");
		}
		if( result == null ) 
		{
			result = Boolean.FALSE;
		}
		
		if( !result )
		{
			// FIXME:
			//  if you want to send more information to client, otherwise http code 500,
			//  try to redirect the request to a RESTFUL dealing with errors.
			//  the RESTFUL will send more information to client.
			response.sendError(500, "no permit");
		}
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		
	}

	public Script getDefaultScript()
	{
		return defaultScript;
	}

	public void setDefaultScript(Script defaultScript)
	{
		this.defaultScript = defaultScript;
	}
	
	public String getLoginUrl()
	{
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	public String getPagePrefix()
	{
		return pagePrefix;
	}

	public void setPagePrefix(String pagePrefix)
	{
		this.pagePrefix = pagePrefix;
	}

	public String getLoginPageName()
	{
		return loginPageName;
	}

	public void setLoginPageName(String loginPageName)
	{
		this.loginPageName = loginPageName;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

}
