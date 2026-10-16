package dio.taskmanager.infrastructure.http;

import dio.taskmanager.application.*;
import dio.taskmanager.application.input.CreateTaskInput;
import dio.taskmanager.domain.TaskId;
import dio.taskmanager.infrastructure.http.request.CreateTaskRequest;
import dio.taskmanager.infrastructure.http.request.UpdateTaskRequest;
import dio.taskmanager.infrastructure.http.response.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController // com isso o SpringBoot já sabe como instanciar o controller e já defini ele para o servidor toncat
@RequestMapping("/tasks")
public class TaskController {
    /* controller é um dos padrões comuns quando a gente trabalha com servidores web. O controller vai ser basicamente
    um ponto de entrada para requisições HTTP de um recurso específico, no nosso caso do recurso task
     */

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final GetTaskByIdUseCase getTaskByIdUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase,
                          GetTaskByIdUseCase getTaskByIdUseCase, DeleteTaskUseCase deleteTaskUseCase,
                          UpdateTaskUseCase updateTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.getTaskByIdUseCase = getTaskByIdUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
    }

    @PostMapping // URL disponível para receber uma requisição post.
    @ResponseStatus(HttpStatus.CREATED)
    TaskResponse create(@RequestBody @Valid CreateTaskRequest request) {
        // a ideia é que a gente envie os dados da nossa aplicação da nossa task para a nossa api.
        var input = request.toInput();
        var output = createTaskUseCase.execute(input);
        return TaskResponse.from(output);
        /* nesse formato a gente manda uma requisição ela vai ser persistida e agente vai ter uma reposta com as
        informações que foram persistidas.
         */
        // primeiro fluxo inteiro de requisição para o nosso controller
    }

    @GetMapping
    List<TaskResponse> list() {
        return getTaskUseCase.execute().stream().map(TaskResponse::from).toList();
    }

    @GetMapping("/{id}")
    TaskResponse read(@PathVariable UUID id){
        var output = getTaskByIdUseCase.execute(new TaskId(id));
        return TaskResponse.from(output);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id){
        deleteTaskUseCase.execute(new TaskId(id));
    }

    @PatchMapping("/{id}")
    TaskResponse update(@PathVariable UUID id, @RequestBody UpdateTaskRequest request) {
        var input = request.toInput();
        var output = updateTaskUseCase.execute(new TaskId(id), input);
        return TaskResponse.from(output);
    }

    // todos os mapeamento de rotas implementados
}
