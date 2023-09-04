package com.ccp.jn.web.spring.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.jn.sync.service.JnSupportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping(value = "/support/{chatId}")
@Api(value = "API que disponibiliza serviços para o time de suporte do JobsNow")
public class JnSupportController {

	
	@ApiOperation(value = "Nome deste passo: 'Criar senha temporária para usuário desbloquear token'"
			+ "... Quando ocorre? Quando usuário bloqueia o token e solicita desbloqueio deste token, o sistema envia por mensagem instantânea esta solicitação"
			+ " que nomomento em que vai atender a esta solicitação, evoca este endpoint. "
			+ "Para que serve? Para enviar por e-mail ao usuário, a senha temporária que se corretamente digitada, lhe desbloqueará o token."
			)
	 @ApiResponses(value = {
			 	@ApiResponse(code = 200, message = "Status: 'Senha temporária enviada com sucesso' <br/><br/> Quando ocorre? Quando o operador tem permissão para atender a solicitação, quando existe uma solicitação de desbloqueio de token, quando o token está de fato bloqueado e quando esta solicitação ainda não foi respondida."),
		        @ApiResponse(code = 400, message = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
		        @ApiResponse(code = 401, message = "Status: 'Operador sem permissão para atender esta solicitação' <br/><br/> Quando ocorre? Quando o operador não está cadastrado na tabela correta para atender este tipo de solicitação."),
		        @ApiResponse(code = 404, message = "Status: 'Token não encontrado' <br/><br/> Quando ocorre? Quando o token do usuário não é encontrado."),
		        @ApiResponse(code = 409, message = "Status: 'Solicitação já respondida' <br/><br/> Quando ocorre? Quando a solicitação consta na tabela de solicitações (deste tipo) respondidas."),
		        @ApiResponse(code = 422, message = "Status: 'Solicitação inexistente' <br/><br/> Quando ocorre? Quando a solicitação não existe."),
	 })	
	@PostMapping("/token/{email}/unlock")
	public Map<String, Object> answerUnlockTokenRequest(@PathVariable("chatId") String chatId, @PathVariable("email") String email) {
		CcpMapDecorator result = JnSupportService.unlockToken.execute(Long.valueOf(chatId), email);
		return result.content;
	}

	@ApiOperation(value = "Nome deste passo: 'Reenvio de token ao usuário'"
			+ "... Quando ocorre? Quando o usuário solicita reenvio de token"
			+ "Para que serve? Para que o suporte JobsNow possa enviar por e-mail ao usuário, o token caso ele não tenha recebido, geralmente pelo fornecedor de API de e-mail do JobsNow ser bloqueado pelo provedor de quem está recebendo e-mail."
			)
	 @ApiResponses(value = {
			 	@ApiResponse(code = 200, message = "Status: 'Senha temporária enviada com sucesso' <br/><br/> Quando ocorre? Quando o operador tem permissão para atender a solicitação, quando existe uma solicitação de desbloqueio de token, quando o token está de fato bloqueado e quando esta solicitação ainda não foi respondida."),
		        @ApiResponse(code = 400, message = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
		        @ApiResponse(code = 401, message = "Status: 'Operador sem permissão para atender esta solicitação' <br/><br/> Quando ocorre? Quando o operador não está cadastrado na tabela correta para atender este tipo de solicitação."),
		        @ApiResponse(code = 404, message = "Status: 'Token não encontrado' <br/><br/> Quando ocorre? Quando o token do usuário não é encontrado."),
		        @ApiResponse(code = 409, message = "Status: 'Solicitação já respondida' <br/><br/> Quando ocorre? Quando a solicitação consta na tabela de solicitações (deste tipo) respondidas."),
		        @ApiResponse(code = 422, message = "Status: 'Solicitação inexistente' <br/><br/> Quando ocorre? Quando a solicitação não existe."),
	 })	
	@PostMapping("/token/{email}/resending")
	public Map<String, Object> resentTokenToTheUser(@PathVariable("chatId") String chatId, @PathVariable("email") String email) {
		CcpMapDecorator result = JnSupportService.resendToken.execute(Long.valueOf(chatId), email);
		return result.content;
	}
	
}
