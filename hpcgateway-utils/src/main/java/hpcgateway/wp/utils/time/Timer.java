package hpcgateway.wp.utils.time;

public class Timer
{
	final public static void sleep(int seconds) 
	{
		long expire = System.currentTimeMillis() + (seconds*1000);
		long current = System.currentTimeMillis();
		while( current < expire )
		{
			try
			{
				Thread.sleep(200);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			current = System.currentTimeMillis();
		}
	}
}
