package hpcgateway.wp.orm.h2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.h2.tools.Server;

public class H2dbListener implements ServletContextListener
{
	private Server server;
	
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		String tcpPort = context.getInitParameter("h2.tcpPort");
		if( tcpPort == null )
		{
			tcpPort = "8043";
		}
		
		System.out.println(String.format("# Startup H2 database server on %s ....",tcpPort));
		
		try
		{
			server = Server.createTcpServer("-tcp","-tcpPort",tcpPort).start(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		if( server != null )
		{
			server.stop();
			server = null;
		}
	}



}
