package com.ccp.jn.web.spring.controller.contactus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.Cache;
import com.ccp.implementations.db.crud.elasticsearch.Crud;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.file.bucket.FileBucket;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.MensageriaSender;
import com.ccp.implementations.password.mindrot.Password;

@SpringBootApplication
public class SpringApplicationStarter {

	public static void main(String[] args) {

		CcpDependencyInjection.loadAllImplementationsProviders
		(
				new MensageriaSender(),
				new FileBucket(),
				new Password(),
				new DbUtils(),
				new Cache(),
				new Crud()
		);
		
		SpringApplication.run(SpringApplicationStarter.class, args);
	}

	
}
