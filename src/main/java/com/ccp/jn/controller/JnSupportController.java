package com.ccp.jn.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.jn.sync.service.JnSyncSupportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping(value = "/support/{chatId}")
@Tag(name = "Support", description = "API que disponibiliza serviços para o time de suporte do JobsNow")
public class JnSupportController {

	@Operation(summary = "Criar senha temporária para usuário desbloquear token", description = "Quando ocorre? Quando usuário bloqueia o token e solicita desbloqueio deste token, o sistema envia por mensagem instantânea esta solicitação"
			+ " que nomomento em que vai atender a esta solicitação, evoca este endpoint. "
			+ "<br/><br/>Para que serve? Para enviar por e-mail ao usuário, a senha temporária que se corretamente digitada, lhe desbloqueará o token."
			)
	 @ApiResponses(value = {
			 	@ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{'asyncTaskId':'ticketDaFilaSha1EmBase64'}"))}, responseCode = "200", description = "Status: 'Senha temporária enviada com sucesso' <br/><br/> Quando ocorre? Quando o operador tem permissão para atender a solicitação, quando existe uma solicitação de desbloqueio de token, quando o token está de fato bloqueado e quando esta solicitação ainda não foi respondida."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "401", description = "Status: 'Operador sem permissão para atender esta solicitação' <br/><br/> Quando ocorre? Quando o operador não está cadastrado na tabela correta para atender este tipo de solicitação."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "404", description = "Status: 'Token não encontrado' <br/><br/> Quando ocorre? Quando o token do usuário não é encontrado."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "409", description = "Status: 'Solicitação já respondida' <br/><br/> Quando ocorre? Quando a solicitação consta na tabela de solicitações (deste tipo) respondidas."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "422", description = "Status: 'Solicitação inexistente' <br/><br/> Quando ocorre? Quando a solicitação não existe."),
	 })	
	@PostMapping("/token/{email}/unlock")
	public Map<String, Object> answerUnlockTokenRequest(@PathVariable("chatId") String chatId, @PathVariable("email") String email) {
		Long valueOf = Long.valueOf(chatId);
		CcpJsonRepresentation result = JnSyncSupportService.unlockToken.execute(valueOf, email);
		return result.content;
	}

	@Operation(summary = "Reenvio de token ao usuário",  description = "Quando ocorre? Quando o usuário solicita reenvio de token"
			+ "<br/><br/>Para que serve? Para que o suporte JobsNow possa enviar por e-mail ao usuário, o token caso ele não tenha recebido, geralmente pelo fornecedor de API de e-mail do JobsNow ser bloqueado pelo provedor de quem está recebendo e-mail."
			)
	 @ApiResponses(value = {
			 	@ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{'asyncTaskId':'ticketDaFilaSha1EmBase64'}"))}, responseCode = "200", description = "Status: 'Senha temporária enviada com sucesso' <br/><br/> Quando ocorre? Quando o operador tem permissão para atender a solicitação, quando existe uma solicitação de desbloqueio de token, quando o token está de fato bloqueado e quando esta solicitação ainda não foi respondida."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "401", description = "Status: 'Operador sem permissão para atender esta solicitação' <br/><br/> Quando ocorre? Quando o operador não está cadastrado na tabela correta para atender este tipo de solicitação."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "404", description = "Status: 'Token não encontrado' <br/><br/> Quando ocorre? Quando o token do usuário não é encontrado."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "409", description = "Status: 'Solicitação já respondida' <br/><br/> Quando ocorre? Quando a solicitação consta na tabela de solicitações (deste tipo) respondidas."),
		        @ApiResponse(content = {@Content(mediaType = "application/json",schema = @Schema(example = "{}"))}, responseCode = "422", description = "Status: 'Solicitação inexistente' <br/><br/> Quando ocorre? Quando a solicitação não existe."),
	 })	
	@PostMapping("/token/{email}/resending")
	public Map<String, Object> resentTokenToTheUser(@PathVariable("chatId") String chatId, @PathVariable("email") String email) {
		CcpJsonRepresentation result = JnSyncSupportService.resendToken.execute(Long.valueOf(chatId), email);
		return result.content;
	}
	
}
