package br.com.microservices.intimacaojudicial;

import br.com.microservices.intimacaojudicial.dto.ProcessoDTO;
import br.com.microservices.intimacaojudicial.dto.ReuDTO;
import br.com.microservices.intimacaojudicial.entity.Processo;
import br.com.microservices.intimacaojudicial.entity.Reu;
import br.com.microservices.intimacaojudicial.service.IntimacaoJudicialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class IntimacaoJudicialApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private IntimacaoJudicialService processoService;

	private Processo processo;

	@BeforeEach
	public void setup() {
		// Inicializa um mock de processo para ser utilizado nos testes
		processo = new Processo();
		processo.setId(1L);
		processo.setNroProcesso("123456");
		processo.setReus(Collections.emptyList());
	}

	@Test
	public void testSalvarProcesso() throws Exception {
		// Simula o comportamento do service
		Mockito.doNothing().when(processoService).criarProcesso(Mockito.any(Processo.class));

		// Cria o DTO do processo que será enviado no corpo da requisição
		ReuDTO reu = new ReuDTO("Nome do Réu");
		var processoDTO = new ProcessoDTO("123456", reu);

		mockMvc.perform(post("/intimacao-judicial/processo/api/v1/salvar")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(processoDTO)))
				.andExpect(status().isOk());
	}

	@Test
	public void testBuscarProcesso() throws Exception {
		// Simula o comportamento do service ao buscar o processo pelo número
		Mockito.when(processoService.buscarProcesso("123456")).thenReturn(processo);

		mockMvc.perform(get("/intimacao-judicial/processo/api/v1/buscar/123456"))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) jsonPath("$.nroProcesso").value("123456"))
				.andExpect((ResultMatcher) jsonPath("$.reus").isEmpty());
	}

	@Test
	public void testAdicionarReuProcesso() throws Exception {
		// Simula o comportamento do service ao adicionar um réu
		Reu reu = new Reu("João", processo);
		Mockito.when(processoService.incluirReu(eq("123456"), anyString())).thenReturn(reu);

		// Cria o DTO do réu que será enviado no corpo da requisição
		var reuDTO = new ReuDTO("João");

		mockMvc.perform(post("/intimacao-judicial/processo/api/v1/incluirReu/123456")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reuDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nomeReu").value("João"))
				.andExpect(jsonPath("$.nroProcesso").value("123456"))
				.andExpect(jsonPath("$.mensagem").value("Réu adicionado com sucesso."));
	}

	@Test
	public void testDeletarProcesso() throws Exception {
		// Simula o comportamento do service ao deletar o processo
		Mockito.doNothing().when(processoService).excluirProcesso("123456");

		mockMvc.perform(delete("/intimacao-judicial/processo/api/v1/excluir/123456"))
				.andExpect(status().isOk())
				.andExpect(content().string("Excluido com sucesso"));
	}

	@Test
	public void testProcessoNaoEncontrado() throws Exception {
		// Simula um erro de processo não encontrado
		Mockito.when(processoService.buscarProcesso("999999")).thenThrow(new IllegalArgumentException("Número do processo não encontrado."));

		mockMvc.perform(get("/intimacao-judicial/processo/api/v1/buscar/999999"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Número do processo não encontrado."));
	}

}
