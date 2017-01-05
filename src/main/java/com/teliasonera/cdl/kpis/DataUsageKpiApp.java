package com.teliasonera.cdl.kpis;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import com.teliasonera.cdl.kpis.service.DataUsageKPIService;
import com.teliasonera.cdl.kpis.utils.DateUtils;

@SpringBootApplication
public class DataUsageKpiApp implements CommandLineRunner {

	
	@Autowired
	private DataUsageKPIService dataUsageKPIService;
	
	public void run(String... args) throws Exception {
		long startTime = 0L;
		long endTime = 0L;
		if(args.length < 2) {
			startTime = DateUtils.START_DATE.getDate();
			endTime = DateUtils.END_DATE.getDate();
		} else {
			try {
				startTime = Long.parseLong(args[0]);
				endTime = Long.parseLong(args[1]);
			} catch (NumberFormatException e) {
				
			}
		}
		dataUsageKPIService.extractAndStoreDataUsageEvents(startTime,endTime);	
		dataUsageKPIService.extractVolumeSnapShot();
	}
	
	@Bean
    public RestTemplate myRestTemplate(@Value("${navigator.username}") String username,
    		@Value("${navigator.password}") String password) {
		final RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(getMessageConverters());
	    restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
	    return restTemplate;
    }
	
	@Bean
	public JdbcTemplate jdbcTemplate(@Value("${hive.url}") String url) {
		return new JdbcTemplate(dataSource(url));
	}
	
	
	public DataSource dataSource(String url) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		//dataSource.setUsername(username);
		//dataSource.setPassword(password);
		return dataSource;
	}

	
	private List<HttpMessageConverter<?>> getMessageConverters() {
	    List<HttpMessageConverter<?>> converters = 
	      new ArrayList<HttpMessageConverter<?>>();
	   // converters.add(new MappingJackson2HttpMessageConverter());
	    converters.add(new FormHttpMessageConverter());
	    converters.add(new StringHttpMessageConverter());
	    return converters;
	}
	
	public static void main(String[] args) {
		/*System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "60000");*/
		SpringApplication.run(DataUsageKpiApp.class, args).close();
	}

}
