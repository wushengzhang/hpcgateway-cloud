package hpcgateway.wp.desktop.controller;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import hpcgateway.wp.acl.DefaultSessionController;


@Controller("sessionController")
@Scope("session")
public class SessionController extends DefaultSessionController implements java.io.Serializable
{
	final public static String SESSION_KEY = "session_controller";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public SessionController()
	{
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}





}
