package dio.taskmanager.infrastructure.http.request;

import dio.taskmanager.application.input.UpdateTaskInput;
import dio.taskmanager.domain.TaskStatus;

import java.util.Optional;

public record UpdateTaskRequest(
        Optional<String> title,
        Optional<String> descrition,
        Optional<String> status
) {
    public UpdateTaskInput toInput(){
        return new UpdateTaskInput(title, descrition, status.map(TaskStatus::valueOf));
    }
}
