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

public class ValidEmailFilter implements Filter{

	static {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
