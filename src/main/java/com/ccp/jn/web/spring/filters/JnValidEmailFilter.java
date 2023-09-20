package com.ccp.jn.web.spring.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.implementations.cache.gcp.memcache.CcpGcpMemCache;
import com.ccp.implementations.db.dao.elasticsearch.CcpElasticSearchDao;
import com.ccp.implementations.db.utils.elasticsearch.CcpElasticSearchDbRequest;
import com.ccp.implementations.file.bucket.gcp.CcpGcpFileBucket;
import com.ccp.implementations.http.apache.mime.CcpApacheMimeHttp;
import com.ccp.implementations.json.gson.CcpGsonJsonHandler;
import com.ccp.implementations.main.authentication.gcp.oauth.CcpGcpMainAuthentication;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.CcpGcpPubSubMensageriaSender;
import com.ccp.implementations.mensageria.sender.gcp.pubsub.local.CcpLocalEndpointMensageriaSender;
import com.ccp.implementations.password.mindrot.CcpMindrotPasswordHandler;

public class JnValidEmailFilter implements Filter{

	static {
		boolean localEnviroment = new CcpStringDecorator("c:\\rh").file().exists();
		CcpDependencyInjection.loadAllDependencies
		(
				localEnviroment ? new CcpLocalEndpointMensageriaSender() : new CcpGcpPubSubMensageriaSender()
				,new CcpGcpMainAuthentication()
				,new CcpGsonJsonHandler()
				,new CcpGcpFileBucket()
				,new CcpMindrotPasswordHandler()
				,new CcpElasticSearchDbRequest()
				,new CcpGcpMemCache()
				,new CcpApacheMimeHttp() 
				,new CcpElasticSearchDao()
		);

	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain){

		HttpServletRequest request = (HttpServletRequest) req;

		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD, PATCH");
		response.setHeader("Access-Control-Max-Age", "3600");

		response.setHeader("Access-Control-Allow-Headers",
				"Access-Control-Allow-Headers, X-Requested-With, authorization, token, email, Content-Type, Authorization, Access-Control-Request-Methods, Access-Control-Request-Headers");

		String method = request.getMethod();

		boolean optionsMethod = "OPTIONS".equalsIgnoreCase(method);

		if (optionsMethod) {
			return;
		}

		StringBuffer requestURL = request.getRequestURL();
		String url = new CcpStringDecorator(requestURL.toString()).url().asDecoded();
		String filtered = "login/";
		int indexOf = url.indexOf(filtered) + filtered.length();
		String urlSecondPiece = url.substring(indexOf);
		String[] split = urlSecondPiece.split("/");
		String email = split[0];
		boolean invalidEmail = new CcpStringDecorator(email).email().isValid() == false;
		if(invalidEmail) {
			response.setStatus(400);
			return;
		}
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}

}
