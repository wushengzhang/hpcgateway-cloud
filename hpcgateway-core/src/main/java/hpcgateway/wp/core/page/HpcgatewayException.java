package hpcgateway.wp.core.page;

public class HpcgatewayException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2255066744588229855L;
	private String code;
	
	public HpcgatewayException(String code,String message)
	{
		super(message);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
