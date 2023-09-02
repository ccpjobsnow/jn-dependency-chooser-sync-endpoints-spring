package com.ccp.jn.web.spring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memcache.Cache;
import com.ccp.implementations.db.dao.elasticsearch.Dao;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.file.bucket.gcp.FileBucket;
import com.ccp.implementations.http.apache.mime.Http;
import com.ccp.implementations.main.authentication.MainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.MensageriaSender;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.local.LocalMensageriaSender;
import com.ccp.implementations.password.mindrot.Password;
import com.ccp.implementations.text.extractor.apache.tika.JsonHandler;
import com.ccp.jn.web.spring.controller.LoginController;
import com.ccp.jn.web.spring.controller.async.tasks.GetAsyncTaskByIdController;
import com.ccp.jn.web.spring.controller.contactus.SaveContactUsController;
import com.ccp.jn.web.spring.controller.resumes.crud.DownloadResumeToHisOwnerController;
import com.ccp.jn.web.spring.controller.resumes.search.DownloadResumeToRecruiterController;
import com.ccp.jn.web.spring.exceptions.handler.JnSyncExceptionHandler;
import com.ccp.jn.web.spring.filters.ValidEmailFilter;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		SwaggerConfig.class,
		LoginController.class, 
		JnSyncExceptionHandler.class,
		SaveContactUsController.class
		,DownloadResumeToHisOwnerController.class
		,GetAsyncTaskByIdController.class
		,DownloadResumeToRecruiterController.class
})
@SpringBootApplication
public class JnSyncSpringApplicationStarter {

	
	public static void main(String[] args) {
		boolean localEnviroment = new CcpStringDecorator("c:\\rh").file().exists();
		CcpDependencyInjection.loadAllDependencies
		(
				localEnviroment ? new LocalMensageriaSender() : new MensageriaSender()
				,new MainAuthentication()
				,new JsonHandler()
				,new FileBucket()
				,new Password()
				,new DbUtils()
				,new Cache()
				,new Http() 
				,new Dao()
		);

		SpringApplication.run(JnSyncSpringApplicationStarter.class, args);
	}

	@Bean
	public FilterRegistrationBean<ValidEmailFilter> filtroJwt() {
		FilterRegistrationBean<ValidEmailFilter> filtro = new FilterRegistrationBean<>();
		filtro.setFilter(new ValidEmailFilter());
		filtro.addUrlPatterns("/login/*");

		return filtro;
	}

}
