package com.ccp.jn.web.spring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ccp.jn.web.spring.controller.JnLoginController;
import com.ccp.jn.web.spring.controller.JnSupportController;
import com.ccp.jn.web.spring.controller.async.tasks.GetAsyncTaskByIdController;
import com.ccp.jn.web.spring.controller.contactus.SaveContactUsController;
import com.ccp.jn.web.spring.controller.resumes.crud.DownloadResumeToHisOwnerController;
import com.ccp.jn.web.spring.controller.resumes.search.DownloadResumeToRecruiterController;
import com.ccp.jn.web.spring.exceptions.handler.JnSyncExceptionHandler;
import com.ccp.jn.web.spring.filters.JnValidEmailFilter;

@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		JnLoginController.class, 
		JnSupportController.class,
		JnSyncExceptionHandler.class,
		SaveContactUsController.class
		,DownloadResumeToHisOwnerController.class
		,GetAsyncTaskByIdController.class
		,DownloadResumeToRecruiterController.class
})
@SpringBootApplication
public class JnSyncSpringApplicationStarter {
	
	public static void main(String[] args) {
		System.setProperty("spring.main.allow-bean-definition-overriding","true");

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
