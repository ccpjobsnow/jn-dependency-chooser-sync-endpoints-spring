package com.ccp.jn.web.spring.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.fields.validations.annotations.ValidationRules;
import com.ccp.jn.sync.service.JnSyncLoginService;
import com.ccp.jn.web.spring.validations.login.JnFieldValidationPassword;
import com.ccp.jn.web.spring.validations.login.JnFieldValidationPasswordAndToken;
import com.ccp.jn.web.spring.validations.login.JnFieldValidationPreRegistration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/login/{email}")
@Tag(name = "Login", description = "Controles de login para cadastro de token, senha, senha fraca, pre registro, alem de controles de bloqueios diversos tais como: token, senha, senha de desbloqueio de token")
public class JnLoginController {

	private final JnSyncLoginService loginService = new JnSyncLoginService();

	@GetMapping
	public String teste(@PathVariable("email") String email) {
		return "oiu";
	}

	@Operation(summary = "Executar Login", description = "Quando ocorre? Logo após o usuário digitar sua senha. Para que serve? Serve para o usuário executar login no sistema, gerando um token que será a prova "
			+ " (nas próximas requisições) que o requisitante (frontend), merece ter leitura ou escrita de certos recursos deste bando de dados. "
			+ "O parametro words hash é informado pelo front end (ou nao) por query parameter, se acaso ele for informado e estiver igual ao que o "
			+ "back end tem, o wordsHash não será devolvido na response desse método. Caso este parâmetro não for informado, ou se não for o mesmo que está no back end, então a lista do wordsHash é retornada juntamente com o novo wordsHash e o front deverá salvar no application storage (memória de longa duração do navegador)"
			+ "<br/><br/>Passo anterior: 'Verificação de e-mail'")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "{\r\n"
					+ "    \"sessionToken\": \"{valorDoToken}\",\r\n"
					+ "    \"wordsHash\": \"{wordsHash}\",\r\n"
					+ "    \"words\": [\r\n"
					+ "      {\r\n"
					+ "        \"word\": \"java\",\r\n"
					+ "        \"type\": \"IT\"\r\n"
					+ "      }\r\n"
					+ "    ]\r\n"
					+ "  }")) }, responseCode = "200", description = "Status: 'Usuário logado com sucesso' <br/><br/> Quando ocorre? Quando o usuário digita senha e e-mail corretos e não está com senha ou token bloqueado ou pendência de desbloqueio de token<br/><br/>Qual comportamento esperado do front end? Que ele remova o modal de login e guarde o 'sessionToken' contido no json retornado por este endpoint"),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "{\r\n"
							+ "    \"sessionToken\": \"{valorDoToken}\",\r\n"
							+ "    \"wordsHash\": \"{wordsHash}\",\r\n"
							+ "    \"words\": [\r\n"
							+ "      {\r\n"
							+ "        \"word\": \"java\",\r\n"
							+ "        \"type\": \"IT\"\r\n"
							+ "      }\r\n"
							+ "    ]\r\n"
							+ "  }")) }, responseCode = "201", description = "Status: 'O cadastro de Pre registro está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou de cadastrar dados do pré registro<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro do pré registro."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "{\r\n"
							+ "    \"sessionToken\": \"{valorDoToken}\",\r\n"
							+ "    \"wordsHash\": \"{wordsHash}\",\r\n"
							+ "    \"words\": [\r\n"
							+ "      {\r\n"
							+ "        \"word\": \"java\",\r\n"
							+ "        \"type\": \"IT\"\r\n"
							+ "      }\r\n"
							+ "    ]\r\n"
							+ "  }")) }, responseCode = "202", description = "Status: 'O cadastro de  senha está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou cadastrar senha<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro da senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "409", description = "Status: 'Usuário já logado' <br/><br/> Quando ocorre? Quando já está registrada uma sessão corrente para o usuário que está tentando fazer login neste sistema. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "422", description = "Status: 'Senha digitada incorretamente' <br/><br/> Quando ocorre? Quando o usuário, digitou incorretamente a senha, mas ainda não excedeu o máximo de tentativas de senhas incorretas. <br/><br/>Qual comportamento esperado do front end? Exibir mensagem de erro informando o número de tentativas incorretas de digitação de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "429", description = "Status: 'Senha recém bloqueada <br/><br/> Quando ocorre? No exato momento em que o usuário digitou incorretamente a senha, e acaba exceder o máximo de tentativas de senhas incorretas. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário à tela de recadastro de senha."), })
	@ValidationRules(rulesClass = JnFieldValidationPassword.class)
	@PostMapping
	public Map<String, Object> executeLogin(HttpServletRequest request, @PathVariable("email") String email,
			@Schema(example = "{\r\n"
					+ "    \"password\": \"Jobsnow1!\"\r\n"
					+ "  }") @RequestBody Map<String, Object> body, @RequestParam(value = "wordsHash", required =  false) String wordsHash) {
		String remoteAddr = request.getRemoteAddr();
		Map<String, Object> values = new CcpJsonRepresentation(body).put("ip", remoteAddr).put("email", email).content;
		CcpJsonRepresentation execute = this.loginService.executeLogin(values);
		return execute.content;
	}

	@Operation(summary = "Criar token para gerenciamento de senha", description = "Quando ocorre? Logo após ser constatado que é primeiro acesso deste usuário e ele confirmar o e-mail. Para que serve? Serve para o usuário requisitar envio de token para o seu e-mail e ele poder usar esse token para cadastrar senha. "
			+ " (nas próximas requisições) que o requisitante (frontend), merece ter leitura ou escrita de certos recursos deste bando de dados. Passo anterior: 'Verificação de e-mail'.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "")) }, responseCode = "200", description = "Status: 'Senha já cadastrada, usuário sem pendências"
					+ " de cadastro' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos "
					+ "(token, senha e pré registro)<br/><br/>"
					+ "Qual comportamento esperado do front end? Redirecionamento para a tela que pede senha para executar login."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "201", description = "Status: 'O cadastro de Pre registro está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou de cadastrar dados do pré registro<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro do pré registro."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = 
							" {\"asyncTaskId\": \"-484333a30ec794b6c5490290cfda0486e7c31c89\"}"
							)) }, responseCode = "202", description = "Status: 'Token para cadastro de senha enviado ao e-mail do usuário' <br/><br/> Quando ocorre? Quando o usuário acaba de requisitar com sucesso, o cadastro de senha<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro da senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "409", description = "Status: 'Usuário já logado' <br/><br/> Quando ocorre? Quando já está registrada uma sessão corrente para o usuário que está tentando fazer login neste sistema. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })
	@PostMapping("/token/language/{language}")
	public Map<String, Object> createLoginToken(@PathVariable("email") String email,
			@PathVariable("language") String language) {
		
		CcpJsonRepresentation createLoginToken = this.loginService.createLoginToken(email, language);
		return createLoginToken.content;
	}

	@Operation(summary = "Verificação de existência deste usuário", description = "Quando ocorre? Logo após o usuário se interessar em ter acesso a informações deste sistema que ele só pode ter se estiver devidamente identificado (logado) nele. Para que serve? Serve para verificar se o usuário existe no sistema, caso ele existir, verificar se há pendências cadastrais (senha, pré registro) para ele resolver e se não existir, fazê-lo preencher todos os dados que o sistema precisa.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Status: 'Senha já cadastrada, usuário sem pendências de cadastro' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos (token, senha e pré registro)<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela que pede senha para executar login."),
			@ApiResponse(responseCode = "201", description = "Status: 'O cadastro de Pre registro está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou de cadastrar dados do pré registro<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro do pré registro."),
			@ApiResponse(responseCode = "202", description = "Status: 'O cadastro de  senha está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou cadastrar senha<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro da senha."),
			@ApiResponse(responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(responseCode = "409", description = "Status: 'Usuário já logado' <br/><br/> Quando ocorre? Quando já está registrada uma sessão corrente para o usuário que está tentando fazer login neste sistema. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })
	@RequestMapping(value = "/token", method = RequestMethod.HEAD)
	public void existsLoginToken(@PathVariable("email") String email) {
		this.loginService.existsLoginToken(email);
	}

	@Operation(summary = "Executar logout no sistema", description = "Quando ocorre? Quando por qualquer razão, o usuário quis não mais ter acesso a informações onde ele precisava estar devidamente identificado (logado) neste sistema. Para que serve? Serve para o usuário previamente se desassociar das próximas ações que serão feitas por este front end.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "")) }, responseCode = "200", description = "Status: 'Usuário executou logout com sucesso' <br/><br/> Quando ocorre? Quando o usuário de fato estavacom sessão ativa (logado) neste sistema<br/><br/>Qual comportamento esperado do front end? Encerramento do modal de login."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário não logado no sistema' <br/><br/> Quando ocorre? Quando o o usuário não está com sessão ativa neste sistema. <br/><br/>Qual comportamento esperado do front end? Encerramento do modal de login."), })
	@DeleteMapping
	public void executeLogout(@PathVariable("email") String email) {
		this.loginService.executeLogout(email);
	}

	@Operation(summary = "Ressolicitação de envio de token", description = "Quando ocorre? Quando o usuário, alegando não ter recebido o token de gerenciamento de senha, pede reenvio, por e-mail deste token. Para que serve? Serve para acionar o suporte do sistema para que manualmente entre em contato com o usuário, na intenção de lhe enviar corretamente o token.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "{\r\n"
					+ "    \"asyncTaskId\": \"790bd2e7fb0e02164944adffb60f3f96c65d929e\",\r\n"
					+ "}\r\n"
					+ "")) }, responseCode = "200", description = "Status: 'Solicitação de reenvio de token executada com sucesso' <br/><br/> Quando ocorre? Quando o usuário consegue, com sucesso, solicitar o reenvio de token.<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela que de cadastro de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "409", description = "Status: 'Aguardando atendimento do suporte' <br/><br/> Quando ocorre? Quando o usuário acaba de solicitar com sucesso o seus desbloqueio de token, porém o suporte ainda não atendeu.<br/><br/>Qual comportamento esperado do front end? Apresentar mensagem de aviso ao usuário, sugerindo-o que aguarde resposta do suporte."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })

	@PostMapping("/token/language/{language}/request/again")
	public Map<String, Object> requestTokenAgain(@PathVariable("email") String email,
			@PathVariable("language") String language) {
		CcpJsonRepresentation execute = this.loginService.requestTokenAgain(email, language);
		return execute.content;
	}

	@Operation(summary = "Solicitação para desbloqueio de token", description = "Quando ocorre? Logo após o usuário se bloquear seu token e então solicitar o desbloqueio. Para que serve? Serve para o sistema notificar o suporte para avaliar a solicitação de desbloqueio de token e, se for o caso, passar ao usuário uma senha temporária para que este possa fazer o desbloqueio de seu token.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "{\r\n"
					+ "    \"asyncTaskId\": \"790bd2e7fb0e02164944adffb60f3f96c65d929e\",\r\n"
					+ "}\r\n"
					+ "")) }, responseCode = "200", description = "Status: 'Solicitação de desbloqueio de token executada com sucesso' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos (token, senha e pré registro)<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela que pede senha para executar login."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "409", description = "Status: 'Aguardando atendimento do suporte' <br/><br/> Quando ocorre? Quando o usuário acaba de solicitar com sucesso o seus desbloqueio de token, porém o suporte ainda não atendeu.<br/><br/>Qual comportamento esperado do front end? Apresentar mensagem de aviso ao usuário, sugerindo-o que aguarde resposta do suporte."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })

	@PostMapping("/token/language/{language}/unlocking")
	public Map<String, Object> requestUnlockToken(@PathVariable("email") String email,
			@PathVariable("language") String language) {
		CcpJsonRepresentation requestUnlockToken = this.loginService.requestUnlockToken(email, language);
		return requestUnlockToken.content;
	}

	@Operation(summary = "Salvar pré registro", description = "Quando ocorre? Logo após o usuário tentar executar login e o sistema constatar ausência de dados de pré registro. Para que serve? Serve para o usuário cadadtrar dados de pré registro.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "")) }, responseCode = "200", description = "Status: 'Usuário sem pendências de cadastro' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos (token, senha e pré registro)<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela que pede senha para executar login."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "{'sessionToken': 'tokengeradoaleatoriamente'}")) }, responseCode = "202", description = "Status: 'O cadastro de  senha está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou cadastrar senha<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro da senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "409", description = "Status: 'Usuário já logado' <br/><br/> Quando ocorre? Quando já está registrada uma sessão corrente para o usuário que está tentando fazer login neste sistema. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })
	@ValidationRules(rulesClass = JnFieldValidationPreRegistration.class)
	@PostMapping("/pre-registration")
	public void savePreRegistration(@PathVariable("email") String email, 
			@Schema(example = "{\r\n"
					+ "    \"goal\": \"jobs\",\r\n"
					+ "    \"channel\": \"linkedin\"\r\n"
					+ "  }") @RequestBody Map<String, Object> body) {
		CcpJsonRepresentation cmd = new CcpJsonRepresentation(body);
		CcpJsonRepresentation put = cmd.put("email", email);
		this.loginService.savePreRegistration(put);
	}

	@Operation(summary = "Confirmação de salvamento de senha fraca", description = "Quando ocorre? Logo após o usuário tentar cadastrar uma senha e ela ser constatada como fraca (de simples inferência). Para que serve? Serve para o usuário confirmar o cadastro e assumir o risco em cadastrar de uma senha fraca.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "")) }, responseCode = "200", description = "Status: 'Usuário sem pendências de cadastro' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos (token, senha e pré registro)<br/><br/>Qual comportamento esperado do front end? Que o modal de login desapareça, retornando o token da sessão."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "201", description = "Status: 'O cadastro de Pre registro está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou de cadastrar dados do pré registro<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro do pré registro."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."), })
	@PostMapping("/password/weak")
	public Map<String, Object> saveWeakPassword(@PathVariable("email") String email) {
		CcpJsonRepresentation execute = this.loginService
				.saveWeakPassword(CcpConstants.EMPTY_JSON.put("email", email));
		return execute.content;
	}

	@Operation(summary = "Desbloqueio de token", description = "Quando ocorre? Logo após o usuário se bloquear seu token, então solicitar o desbloqueio, receber do susporte do sistema a senha para desbloqueio de token e então, desbloquear o token. Para que serve? Desbloquear token de gerenciamento de senha.")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "")) }, responseCode = "200", description = "Status: 'Desbloqueio executado com sucesso' <br/><br/> Quando ocorre? Quando há o desbloqueio do token de gerenciamento de senhas<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro de senhas."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "202", description = "Status: 'Aguardando atendimento do suporte' <br/><br/> Quando ocorre? Quando o usuário acaba de solicitar com sucesso o seus desbloqueio de token, porém o suporte ainda não atendeu.<br/><br/>Qual comportamento esperado do front end? Apresentar mensagem de aviso ao usuário, sugerindo-o que aguarde resposta do suporte."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Senha pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou logar várias vezes com a mesma senha incorreta.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de alteração de senha."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "422", description = "Status: 'Token não bloqueado' <br/><br/> Quando ocorre? Quando o usuário tenta o desbloqueio de um token que não está bloqueado. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário por meio de mensagem que ele está tentando desbloquear um token que não está bloqueado."), })
	@ValidationRules(rulesClass = JnFieldValidationPassword.class)
	@PatchMapping("/token/lock")
	public Map<String, Object> unlockToken(@PathVariable("email") String email, 
			@Schema(example = "{\r\n"
					+ "    \"password\": \"6S1EZ7OA\"\r\n"
					+ "  }") @RequestBody Map<String, Object> requestBody) {
		CcpJsonRepresentation put = new CcpJsonRepresentation(requestBody).put("email", email);
		CcpJsonRepresentation execute = this.loginService.unlockToken(put);
		return execute.content;
	}

	@Operation(summary = "Salvamento de senha", description = "Quando ocorre? Logo após o sistema constatar que o usuário está com senha bloqueada ou faltando, login já em uso ou se o usuário quer alterar senha. Para que serve? Serve para o usuário cadastrar senha de acesso no sistema. O parametro words hash é informado pelo front end (ou nao) por query parameter, se acaso ele for informado e estiver igual ao que o back end tem, o wordsHash não será devolvido na response desse método. Caso este parâmetro não for informado, ou se não for o mesmo que está no back end, então a lista do wordsHash é retornada juntamente com o novo wordsHash e o front deverá salvar no application storage (memória de longa duração do navegador)")
	@ApiResponses(value = { @ApiResponse(content = {
			@Content(schema = @Schema(example = "{\r\n"
					+ "    \"sessionToken\": \"{valorDoToken}\",\r\n"
					+ "    \"wordsHash\": \"{wordsHash}\",\r\n"
					+ "    \"words\": [\r\n"
					+ "      {\r\n"
					+ "        \"word\": \"java\",\r\n"
					+ "        \"type\": \"IT\"\r\n"
					+ "      }\r\n"
					+ "    ]\r\n"
					+ "  }")) }, responseCode = "200", description = "Status: 'Usuário sem pendências de cadastro' <br/><br/> Quando ocorre? Quando o usuário previamente cadastrou todos os dados de pre requisitos (token, senha e pré registro)<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela que pede senha para executar login."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "{\r\n"
							+ "    \"sessionToken\": \"{valorDoToken}\",\r\n"
							+ "    \"wordsHash\": \"{wordsHash}\",\r\n"
							+ "    \"words\": [\r\n"
							+ "      {\r\n"
							+ "        \"word\": \"java\",\r\n"
							+ "        \"type\": \"IT\"\r\n"
							+ "      }\r\n"
							+ "    ]\r\n"
							+ "  }")) }, responseCode = "201", description = "Status: 'O cadastro de Pre registro está pendente' <br/><br/> Quando ocorre? Quando o usuário deixou de cadastrar dados do pré registro<br/><br/>Qual comportamento esperado do front end? Redirecionamento para a tela de cadastro do pré registro."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "400", description = "Status: 'Email inválido' <br/><br/> Quando ocorre? Quando a url path recebe um conjunto de caracteres que não representa um e-mail válido.<br/><br/>Qual comportamento esperado do front end? Apresentar erro genérico de sistema para o usuário."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "401", description = "Status: 'Token digitado incorretamente' <br/><br/> Quando ocorre? Quando o usuário, digitou incorretamente o token, mas ainda não excedeu o máximo de tentativas de senhas incorretas. <br/><br/>Qual comportamento esperado do front end? Exibir mensagem de erro informando o número de tentativas incorretas de digitação de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "403", description = "Status: 'Token bloqueado' <br/><br/> Quando ocorre? Quando o usuário, anteriormente tentou várias vezes alterar sua senha fazendo uso de token incorreto.<br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de requisição de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "404", description = "Status: 'Usuário novo no sistema' <br/><br/> Quando ocorre? Quando o e-mail do usuário é desconhecido por este banco de dados. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para a tela de confirmação de e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "420", description = "Status: 'Token pendente de desbloqueio' <br/><br/> Quando ocorre? Quando o usuário bloqueou o token (digitando-o incorretamente por várias vezes na tela de alteração de senha) e então requisita desbloqueio de token, porém o suporte ainda não o atendeu. <br/><br/>Qual comportamento esperado do front end? Exibir uma mensagem de que em breve o suporte do JobsNow entrará em contato com ele por meio dos contatos informados e redirecioná-lo para a tela de desbloqueio de token."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "421", description = "Status: 'Senha de desbloqueio de token está bloqueada' <br/><br/> Quando ocorre? Quando o usuário, na tela de desbloqueio de token, por diversas vezes errou a digitação da senha de desbloqueio de token. <br/><br/>Qual comportamento esperado do front end? Informar ao usuário que ele está temporariamente bloqueado no acesso ao sistema e redirecioná-lo para a primeira tela do fluxo de login, para o caso de ele querer tentar com outro e-mail."),
			@ApiResponse(content = {
					@Content(schema = @Schema(example = "")) }, responseCode = "422", description = "Status: 'A senha não cumpre requisitos para ser uma senha forte' <br/><br/> Quando ocorre? Quando a combinação de caracteres digitadas pelo usuário, não cumpre os requisitos para ser considerada uma senha forte. <br/><br/>Qual comportamento esperado do front end? Redirecionar o usuário para tela de confirmação de senha fraca."), })
	
	@ValidationRules(rulesClass = JnFieldValidationPasswordAndToken.class)
	@PostMapping("/password")
	public Map<String, Object> updatePassword(@PathVariable("email") String email,
			@Schema(example = " {\r\n"
					+ "    \"password\": \"Jobsnow1!\",\r\n"
					+ "    \"token\": \"RA48JRFM\"\r\n"
					+ "  }") @RequestBody Map<String, Object> requestBody, @RequestParam(value = "wordsHash", required =  false)String wordsHash) {
		CcpJsonRepresentation put = new CcpJsonRepresentation(requestBody).put("email", email);
		CcpJsonRepresentation execute = this.loginService.updatePassword(put);
		return execute.content;
	}

}
