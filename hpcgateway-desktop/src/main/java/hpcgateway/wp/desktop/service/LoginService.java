package hpcgateway.wp.desktop.service;

import hpcgateway.wp.orm.beans.CloudUser;

public interface LoginService
{
	public CloudUser checkUser(String username, String password) throws Exception;
}
