package hpcgateway.wp.core.page;


public class Result 
{

	private String code;
	private String message;
	private Object data;

	public Result()
	{
		this("0","OK");
	}

	public Result(String code,String message)
	{
		this.code = code;
		this.message = message;
		this.data = null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
