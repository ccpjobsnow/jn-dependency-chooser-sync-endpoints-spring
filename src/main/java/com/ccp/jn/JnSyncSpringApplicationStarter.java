package com.ccp.jn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ccp.jn.controller.JnLoginController;
import com.ccp.jn.filters.JnValidEmailFilter;
import com.ccp.jn.sync.business.JnSyncBusinessNotifyError;
import com.ccp.web.spring.exceptions.handler.CcpSyncExceptionHandler;

@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		JnLoginController.class, 
		CcpSyncExceptionHandler.class,
})
@SpringBootApplication
public class JnSyncSpringApplicationStarter {
	
	public static void main(String[] args) {
		
		CcpSyncExceptionHandler.genericExceptionHandler = new JnSyncBusinessNotifyError();

		SpringApplication.run(JnSyncSpringApplicationStarter.class, args);
	}

	@Bean
	public FilterRegistrationBean<JnValidEmailFilter> filtroJwt() {
		FilterRegistrationBean<JnValidEmailFilter> filtro = new FilterRegistrationBean<>();
		filtro.setFilter(new JnValidEmailFilter());
		filtro.addUrlPatterns("/login/*");

		return filtro;
	}

}
