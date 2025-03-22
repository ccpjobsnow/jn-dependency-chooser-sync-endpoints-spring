package com.ccp.jn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.implementations.cache.gcp.memcache.CcpGcpMemCache;
import com.ccp.implementations.db.crud.elasticsearch.CcpElasticSearchCrud;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;
import com.ccp.implementations.main.authentication.gcp.oauth.CcpGcpMainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.CcpGcpPubSubMensageriaSender;
import com.ccp.implementations.password.mindrot.CcpMindrotPasswordHandler;
import com.ccp.jn.async.business.factory.CcpJnAsyncBusinessFactory;
import com.ccp.jn.controller.ControllerJnLogin;
import com.ccp.jn.sync.service.JnValidateSession;
import com.ccp.local.testings.implementations.CcpLocalInstances;
import com.ccp.local.testings.implementations.cache.CcpLocalCacheInstances;
import com.ccp.web.servlet.filters.CcpPutSessionValuesAndExecuteTaskFilter;
import com.ccp.web.servlet.filters.CcpValidEmailFilter;
import com.ccp.web.spring.exceptions.handler.CcpSyncExceptionHandler;
import com.jn.commons.utils.JnAsyncBusiness;
import com.jn.sync.mensageria.JnSyncMensageriaSender;

@EnableWebMvc
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan(basePackageClasses = {
		ControllerJnLogin.class, 
		CcpSyncExceptionHandler.class,
})
@SpringBootApplication
public class ApplicationStarterJnSyncSpring {
	
	public static void main(String[] args) {
		
		boolean localEnviroment = new CcpStringDecorator("c:\\rh").file().exists();
		CcpInstanceProvider<?> businessInstanceProvider = new CcpJnAsyncBusinessFactory();
		CcpDependencyInjection.loadAllDependencies
		(
				localEnviroment ? CcpLocalInstances.mensageriaSender.getLocalImplementation(businessInstanceProvider) : new CcpGcpPubSubMensageriaSender(),
				localEnviroment ? CcpLocalInstances.bucket.getLocalImplementation(businessInstanceProvider) : new CcpGcpFileBucket(),
				localEnviroment ? CcpLocalCacheInstances.map.getLocalImplementation(businessInstanceProvider) : new CcpGcpMemCache()
				,new CcpMindrotPasswordHandler()
				,new CcpElasticSearchDbRequest()
				,new CcpGcpMainAuthentication()
				,new CcpElasticSearchCrud()
				,new CcpGsonJsonHandler()
				,new CcpApacheMimeHttp() 
		);

		CcpSyncExceptionHandler.genericExceptionHandler = new JnSyncMensageriaSender(JnAsyncBusiness.notifyError);

		SpringApplication.run(ApplicationStarterJnSyncSpring.class, args);
	}

	@Bean
	public FilterRegistrationBean<CcpValidEmailFilter> emailFilter() {
		FilterRegistrationBean<CcpValidEmailFilter> filtro = new FilterRegistrationBean<>();
		CcpValidEmailFilter emailSyntaxFilter = CcpValidEmailFilter.getEmailSyntaxFilter("login/");
		filtro.setFilter(emailSyntaxFilter);
		filtro.addUrlPatterns("/login/*");
		return filtro;
	}

	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> putSessionValuesFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filtro = new FilterRegistrationBean<>();
		filtro.setFilter(CcpPutSessionValuesAndExecuteTaskFilter.TASKLESS);
		filtro.addUrlPatterns("/contact-us/*", "/login/*");
		return filtro;
	}

	@Bean
	public FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> validateSessionFilter() {
		FilterRegistrationBean<CcpPutSessionValuesAndExecuteTaskFilter> filtro = new FilterRegistrationBean<>();
		CcpPutSessionValuesAndExecuteTaskFilter filter = new CcpPutSessionValuesAndExecuteTaskFilter(JnValidateSession.INSTANCE);
		filtro.setFilter(filter);
		filtro.addUrlPatterns("/contact-us/*");
		return filtro;
	}
}
