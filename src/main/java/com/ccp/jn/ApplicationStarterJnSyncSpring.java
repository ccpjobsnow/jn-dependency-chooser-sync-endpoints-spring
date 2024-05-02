package com.ccp.jn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ccp.jn.controller.ControllerJnLogin;
import com.ccp.jn.filters.FilterJnValidEmail;
import com.ccp.jn.sync.business.utils.SyncBusinessJnNotifyError;
import com.ccp.web.spring.exceptions.handler.CcpSyncExceptionHandler;

@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		ControllerJnLogin.class, 
		CcpSyncExceptionHandler.class,
})
@SpringBootApplication
public class ApplicationStarterJnSyncSpring {
	
	public static void main(String[] args) {
		
		CcpSyncExceptionHandler.genericExceptionHandler = new SyncBusinessJnNotifyError();

		SpringApplication.run(ApplicationStarterJnSyncSpring.class, args);
	}

	@Bean
	public FilterRegistrationBean<FilterJnValidEmail> filtroJwt() {
		FilterRegistrationBean<FilterJnValidEmail> filtro = new FilterRegistrationBean<>();
		filtro.setFilter(new FilterJnValidEmail());
		filtro.addUrlPatterns("/login/*");

		return filtro;
	}

}
