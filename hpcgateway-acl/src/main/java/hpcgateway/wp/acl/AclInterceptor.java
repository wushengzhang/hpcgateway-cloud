package hpcgateway.wp.acl;

import java.util.List;


public interface AclInterceptor
{
	public void registerExemptUrl(String url);
	public void registerExemptUrls(List<String> url);
	public void registerExemptUrls(String[] urls);
	
	public void registerExemptPrefix(String prefix);
	public void registerExemptPrefixes(List<String> prefixes);
	public void registerExemptPrefixes(String[] prefixes);

	public void registerScript(Script script) throws Exception;
	public void scanAcl(Class<?> clazz) throws Exception;
	public void scanAcl(Class<?> clazz,List<ScriptMapping> scriptMappings) throws Exception;
	public void configAcl(Class<?> clazz,AclConfiguration ac) throws Exception;
}
