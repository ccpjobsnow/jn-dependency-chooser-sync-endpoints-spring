package com.ccp.jn.web.spring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memcache.Cache;
import com.ccp.implementations.db.crud.elasticsearch.Crud;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.file.bucket.gcp.FileBucket;
import com.ccp.implementations.http.apache.mime.Http;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.MensageriaSender;
import com.ccp.implementations.password.mindrot.Password;
import com.ccp.jn.web.spring.controller.contactus.SaveContactUsController;
import com.ccp.jn.web.spring.controller.login.ExistsLoginTokenController;
import com.ccp.jn.web.spring.controller.login.resumes.crud.DownloadResumeToHisOwnerController;
import com.ccp.jn.web.spring.controller.login.resumes.search.DownloadResumeToRecruiterController;
import com.ccp.jn.web.spring.exceptions.handler.JnSiteExceptionHandler;
import com.jn.commons.JnEntity;

@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {ExistsLoginTokenController.class, 
		JnSiteExceptionHandler.class,
		SaveContactUsController.class
		,DownloadResumeToHisOwnerController.class
		,DownloadResumeToRecruiterController.class
})
@SpringBootApplication
public class SiteSpringApplicationStarter {

	
	public static void main(String[] args) {
		CcpDependencyInjection.loadAllImplementationsProviders
		(
				new MensageriaSender()
				,new FileBucket()
				,new Password()
				,new DbUtils()
				,new Cache()
				,new Http()
				,new Crud()
		);
		JnEntity.loadEntitiesMetadata();

		SpringApplication.run(SiteSpringApplicationStarter.class, args);
		System.out.println(CcpDependencyInjection.classes.size());
	}

	
}
