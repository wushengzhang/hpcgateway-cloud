package hpcgateway.wp.net.ssh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import hpcgateway.wp.net.ssh.jsch.ChannelExec;
import hpcgateway.wp.net.ssh.jsch.ChannelSftp;
import hpcgateway.wp.net.ssh.jsch.ChannelShell;
import hpcgateway.wp.net.ssh.jsch.JSch;
import hpcgateway.wp.net.ssh.jsch.JSchException;
import hpcgateway.wp.net.ssh.jsch.Session;
import hpcgateway.wp.net.ssh.jsch.UserInfo;

public class SshClient implements UserInfo
{
	private JSch jsch;
	private String hostname;
	private int port;
	private String username;
	private String password;
	
	public SshClient()
	{
		this(null,0,null,null);
	}
	
	public SshClient(String hostname)
	{
		this(hostname,0,null,null);
	}
	
	public SshClient(String hostname,String username)
	{
		this(hostname,0,username,null);
	}
	
	public SshClient(String hostname,Integer port,String username,String password)
	{
		this.jsch = new JSch();
		this.hostname = hostname;
		this.port = port==null||port<=0 ? 22 : port;
		this.username = username;
		this.password = password;
	}
	
	public void addIdentity(String key)
	{
		if( key != null && !key.isEmpty() )
		{
			try 
			{
				jsch.addIdentity(key);
			}
			catch (JSchException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public ChannelShell shell(String type,Integer cols,Integer rows) throws Exception
	{
		Session session = jsch.getSession(username, hostname, port);
		if( password != null )
		{
			session.setPassword(password);
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");

		
		session.setConfig(config);
		session.setUserInfo(this);
		session.connect();

		ChannelShell channel = (ChannelShell)session.openChannel("shell");
		if( type == null ) type = "xterm";
		if( cols <= 0 ) cols = 80;
		if( rows <= 0 ) rows = 24;
		channel.setPty(true);
		channel.setPtyType(type,cols,rows,cols*12,rows*12);
		channel.connect(1000);
		return channel;
	}
	
	public String execute(String cmdline) throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream err = new ByteArrayOutputStream();
		try
		{
			int code = execute(cmdline,null,out,err);
			if( code != 0 )
			{
				throw new Exception(err.toString("UTF-8"));
			}
			return out.toString("UTF-8");
		}
		finally
		{
			try { out.close(); } catch(Exception ex) { }
			try { err.close(); } catch(Exception ex) { }
		}
	}
	
	public int execute(String cmdline,InputStream in,OutputStream out,OutputStream err) throws Exception
	{
		Session session = null;
		ChannelExec channel = null;
		
		try 
		{
			session = jsch.getSession(username, hostname, port);
			if( password != null )
			{
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setUserInfo(this);
			session.connect();
			
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(cmdline);
			if( in != null )
			{
				channel.setInputStream(in);
			}
			if( out != null )
			{
				channel.setOutputStream(out);
			}
			if( err != null )
			{
				channel.setErrStream(err);
			}
			channel.connect(15000);
			while( !channel.isClosed() )
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception ex)
				{
				}
			}
			int exitStatus = channel.getExitStatus();
			return exitStatus;
		}
		finally
		{
			if( channel != null )
			{
				channel.disconnect();
			}
			if( session != null ) 
			{
				session.disconnect();
			}
		}
	}
	

	public void save(String filename, InputStream in) throws Exception 
	{
		Session session = null;
		ChannelSftp channel = null;

		try
		{
			session = jsch.getSession(username, hostname, port);
			if( password != null )
			{
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setUserInfo(this);
			session.connect();
			channel = (ChannelSftp)session.openChannel("sftp");
			channel.connect();
			channel.put(in, filename);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if( channel != null )
			{
				channel.disconnect();
			}
			if( session != null )
			{
				session.disconnect();
			}
		}
	}

	public void save(String filename, byte[] buffer) throws Exception
	{
		InputStream in = new ByteArrayInputStream(buffer);
		try
		{
			save(filename,in);
		}
		finally
		{
			in.close();
		}
	}
	
	public void save(String filename, String source) throws Exception
	{
		InputStream in = new FileInputStream(source);
		try
		{
			save(filename,in);
		}
		finally
		{
			in.close();
		}
	}

	public byte[] load(String filename) throws Exception 
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		load(filename,out);
		return out.toByteArray();
	}

	public void load(String filename, OutputStream out) throws Exception 
	{
		Session session = null;
		ChannelSftp channel = null;

		try
		{
			session = jsch.getSession(username, hostname, port);
			if( password != null )
			{
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.setUserInfo(this);
			session.connect();
			channel = (ChannelSftp)session.openChannel("sftp");
			channel.connect();
			channel.get(filename, out);
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if( channel != null )
			{
				channel.disconnect();
			}
			if( session != null )
			{
				session.disconnect();			
			}
		}

	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPassphrase() 
	{
		return "";
	}

	public String getPassword() 
	{
		return password;
	}

	public boolean promptPassword(String message) 
	{
		//System.out.println(message);
		return false;
	}

	public boolean promptPassphrase(String message) 
	{
		//System.out.println(message);
		return false;
	}

	public boolean promptYesNo(String message) 
	{
		//System.out.println(message);
		return false;
	}

	public void showMessage(String message) 
	{
		//System.out.println(message);
	}
}
