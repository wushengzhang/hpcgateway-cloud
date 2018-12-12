package hpcgateway.wp.acl;

import java.util.Map;

public interface Script 
{
	public String getName();
	public void setName(String name);
	public ScriptType scriptType();
	public Map<String,Object> execute(Authentication authorization,RequestInfo request) throws Exception;
}
