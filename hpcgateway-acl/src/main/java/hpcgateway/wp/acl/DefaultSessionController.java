package hpcgateway.wp.acl;

public class DefaultSessionController
{
	/*
	 * SESSION key
	 */
	final public static String SESSION_KEY = "session_controller";

	/*
	 * 
	 */
	protected Authentication authentication;

	public Authentication getAuthentication()
	{
		return authentication;
	}

	public void setAuthorization(Authentication authentication)
	{
		this.authentication = authentication;
	}

}
