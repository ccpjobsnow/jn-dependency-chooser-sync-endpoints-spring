package com.ccp.jn.web.spring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memcache.Cache;
import com.ccp.implementations.db.crud.elasticsearch.Crud;
import com.ccp.implementations.db.utils.elasticsearch.DbUtils;
import com.ccp.implementations.file.bucket.gcp.FileBucket;
import com.ccp.implementations.http.apache.mime.Http;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.MensageriaSender;
import com.ccp.implementations.password.mindrot.Password;
import com.jn.commons.JnBusinessEntity;

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
				new Http(),
				new Crud()
		);
		JnBusinessEntity.loadEntitiesMetadata();
		
		SpringApplication.run(SpringApplicationStarter.class, args);
	}

	
}
