package com.ccp.jn.web.spring.controller.login;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.jn.sync.login.controller.CreateLoginToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@Api(value = "Operações a serem executadas em vendas")
@CrossOrigin
@RestController
@RequestMapping("/login/{email}/token/language/{language}")
public class CreateLoginTokenController {
	
	private final CreateLoginToken injected = new CreateLoginToken();

	@ApiOperation(value = "Executa ajuste de estoque. Json de")
	 @ApiResponses(value = {
		        @ApiResponse(code = 200, message = "Executou o ajuste do estoque com sucesso"),
		        @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
		        @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
		    })
	@PostMapping
	public Map<String, Object> execute(@PathVariable("email") String email, @PathVariable("language")String language) {
		return this.injected.execute(email, language).content;
	}

}
