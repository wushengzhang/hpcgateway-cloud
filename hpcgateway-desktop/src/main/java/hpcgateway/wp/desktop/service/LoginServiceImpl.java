package hpcgateway.wp.desktop.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import hpcgateway.wp.metaconfig.dao.hibernate.HpcgatewayDao;
import hpcgateway.wp.orm.beans.CloudUser;
import hpcgateway.wp.orm.beans.PasswordHelper;

@Service("loginService")
public class LoginServiceImpl implements LoginService
{
	@Resource
	private HpcgatewayDao hpcgatewayDao;

	
	@Override
	public CloudUser checkUser(String username, String password) throws Exception 
	{
		String c = PasswordHelper.encryptedPassword(password);
		System.out.println("crypted password = ["+c+"]");
		CloudUser user = hpcgatewayDao.fetchUser(username);
		if( user == null || user.getPassword() == null || !PasswordHelper.validPassword(password,user.getPassword()) )
		{
			throw new Exception("invalid username or password.");
		}
		user.setPassword(password);
		return user;
	}
}
