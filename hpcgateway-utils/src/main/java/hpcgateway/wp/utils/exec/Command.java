package hpcgateway.wp.utils.exec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Command
{
	private String cmdline;
	private InputStream stdin;
	private ByteArrayOutputStream stdout;
	private ByteArrayOutputStream stderr;
	private int exitStatus;
	
	public Command(String cmdline)
	{
		this(cmdline,(InputStream)null);
	}

	public Command(String cmdline,String stdin)
	{
		this(cmdline,stdin==null?null:new ByteArrayInputStream(stdin.getBytes()));
	}
	
	public Command(String cmdline,InputStream stdin)
	{
		this.cmdline = cmdline;
		this.stdin = stdin;
		this.stdout = new ByteArrayOutputStream();
		this.stderr = new ByteArrayOutputStream();
	}
	
	private void copy(InputStream in,OutputStream out) throws Exception
	{
		try
		{
			byte[] buffer = new byte[1024];
			int length = in.read(buffer);
			while(length>=0)
			{
				if( out!=null && length>0 )
				{
					out.write(buffer,0,length);
				}
				length = in.read(buffer);
			}
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			try 
			{ 
				in.close(); 
			} 
			catch(Exception ex) 
			{ 
				ex.printStackTrace(); 
			}
			if( out != null )
			{
				try 
				{ 
					out.close(); 
				} 
				catch(Exception ex) 
				{ 
					ex.printStackTrace(); 
				}
			}
		}
	}
		
	public void execute() throws Exception
	{
		try
		{
			Process process = Runtime.getRuntime().exec(cmdline);
			if( stdin != null )
			{
				copy(stdin,process.getOutputStream());
				stdin = null;
			}
			
			copy(process.getInputStream(),stdout);
			copy(process.getErrorStream(),stderr);
	    	exitStatus = process.waitFor();
	    	if( exitStatus != 0 )
	    	{
	    		throw new Exception(this.getStderrString("UTF-8"));
	    	}
	    	
		}
		catch(Exception ex)
		{
			throw ex;
		}
		finally
		{
			if( stdin != null )
			{ 
				try{ stdin.close(); } catch(Exception ex) { ex.printStackTrace(); }
			}
			if( stdout != null ) 
			{ 
				try { stdout.close(); } catch(Exception ex) { ex.printStackTrace(); }
			}
		}
	}

	public String getCmdline() {
		return cmdline;
	}


	public int getExitStatus() {
		return exitStatus;
	}

	public InputStream getStdoutInputStream()
	{
		return new ByteArrayInputStream(stdout.toByteArray());
	}
	
	public InputStream getStderrInputStream()
	{
		return new ByteArrayInputStream(stderr.toByteArray());
	}

	public String getStdoutString(String encoding) throws UnsupportedEncodingException 
	{
		return stdout.toString(encoding);
	}
	
	public String getStderrString(String encoding) throws UnsupportedEncodingException
	{
		return stderr.toString(encoding);
	}
}
