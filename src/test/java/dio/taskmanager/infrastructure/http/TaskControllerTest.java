package dio.taskmanager.infrastructure.http;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class TaskControllerTest {
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void should_save_and_retrieve_task_by_id() throws Exception {
        Map<String, String> taskRequest = new HashMap<>();
        // faz um map chave e valor do que vai mandar para o resquest
        taskRequest.put("title", "Aprender Spring RestDocs");
        taskRequest.put("description", "Ler o guia oficial do Spring");

        /* com o mockMvc eu consigo falar para ele rodar um post pro tasks, então estou querendo criar uma task com o
        payload acima
         */
        // JSON de resposta em formato de string
        String responseJson = this.mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
        )
                /* Também consigo colocar o que eu estou esperando. Então eu quero que o status de response seja
                201
                 */
                .andExpect(status().isCreated())
                // geração de documento
                .andDo(print())
                .andDo(document("create-task",
                        requestFields(
                                fieldWithPath("title").description("Título da tarefa"),
                                fieldWithPath("description").description("Descrição detalhada")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único da tarefa"),
                                fieldWithPath("title").description("Título da tarefa"),
                                fieldWithPath("description").description("Descrição detalhada"),
                                fieldWithPath("status").description("Status da tarefa")

                        )
                ))
                .andReturn() // pega a resposta
                .getResponse()
                .getContentAsString(); // pega a resposta como string

        String generatedId = JsonPath.read(responseJson, "$.id"); // Recupera o ID que veio da minha string

        // Outra requisição um get para pegar informação daquela task
        this.mockMvc.perform(
                get("/tasks/{id}", generatedId)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk()) // espera que ele retorne um 200
                // Também espera que ele retorne o ID correto e o título correto
                .andExpect(jsonPath("$.id").value(generatedId))
                .andExpect(jsonPath("$.title").value("Aprender Spring RestDocs"))
                // Criando a documentação do get-task-by-id
                .andDo(document("get-task-by-id",
                        pathParameters(
                                parameterWithName("id").description("Identificador único da tarefa")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador único da tarefa"),
                                fieldWithPath("title").description("Título da tarefa"),
                                fieldWithPath("description").description("Descrição detalhada"),
                                fieldWithPath("status").description("Status da tarefa")
                        )
                        ));
    }

}