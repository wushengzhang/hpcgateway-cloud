package hpcgateway.wp.config;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

@Configuration
@PropertySource("classpath:config/core.properties")
public class CoreConfig
{
	@Value("${core.thread.pool.corePoolSize}") Integer corePoolSize;
	@Value("${core.thread.pool.maxPoolSize}") Integer maxPoolSize;
	@Value("${core.thread.pool.queueCapacity}") Integer queueCapacity;
	@Value("${core.thread.pool.keepAliveSeconds}") Integer keepAliveSeconds;
	@Value("${core.kaptcha.border}") String kaptchaBorder;
	@Value("${core.kaptcha.border.color}") String  kaptchaBorderColor;
	@Value("${core.kaptcha.textproducer.font.color}") String kaptchaTextproducerFontColor;
	@Value("${core.kaptcha.textproducer.font.size}") String kaptchaTextproducerFontSize;
	@Value("${core.kaptcha.image.width}") String kaptchaImageWidth;
	@Value("${core.kaptcha.image.height}") String kaptchaImageHeight;
	@Value("${core.kaptcha.textproducer.char.length}") String kaptchaTextproducerCharLength;
	@Value("${core.kaptcha.textproducer.font.names}") String kaptchaTextproducerFontNames;

	@Bean(name="threadPool")
	public ThreadPoolTaskExecutor threadPool()
	{
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		if( corePoolSize != null )
		{
			threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
		}
		if( maxPoolSize != null )
		{
			threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
		}
		if( queueCapacity != null )
		{
			threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
		}
		if( keepAliveSeconds != null )
		{
			threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
		}
		ThreadPoolExecutor.CallerRunsPolicy handler = new ThreadPoolExecutor.CallerRunsPolicy();
		threadPoolTaskExecutor.setRejectedExecutionHandler(handler);
		
		return threadPoolTaskExecutor;
	}
	
	@Bean(name="captchaProducer")
	public DefaultKaptcha captchaProducer()
	{
		System.out.println("Create captchaProducer ...");
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		
		Properties properties = new Properties();
		if( kaptchaBorder!=null && !kaptchaBorder.isEmpty() )
		{
			properties.setProperty("kaptcha.border", kaptchaBorder);
		}
		if( kaptchaBorderColor!=null && !kaptchaBorderColor.isEmpty() )
		{
			properties.setProperty("kaptcha.border.color", kaptchaBorderColor);
		}
		if( kaptchaTextproducerFontColor!=null && !kaptchaTextproducerFontColor.isEmpty() )
		{
			properties.setProperty("kaptcha.textproducer.font.color", kaptchaTextproducerFontColor);
		}
		if( kaptchaTextproducerFontSize!=null && !kaptchaTextproducerFontSize.isEmpty() )
		{
			properties.setProperty("kaptcha.textproducer.font.size", kaptchaTextproducerFontSize);
		}
		if( kaptchaImageWidth!=null && !kaptchaImageWidth.isEmpty() )
		{
			properties.setProperty("kaptcha.image.width", kaptchaImageWidth);
		}
		if( kaptchaImageHeight!=null && !kaptchaImageHeight.isEmpty() )
		{
			properties.setProperty("kaptcha.image.height", kaptchaImageHeight);
		}
		if( kaptchaTextproducerCharLength!=null && !kaptchaTextproducerCharLength.isEmpty() )
		{
			properties.setProperty("kaptcha.textproducer.char.length", kaptchaTextproducerCharLength);
		}
		if( kaptchaTextproducerFontNames!=null && !kaptchaTextproducerFontNames.isEmpty() )
		{
			properties.setProperty("kaptcha.textproducer.font.names", kaptchaTextproducerFontNames);
		}
		
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}
}
