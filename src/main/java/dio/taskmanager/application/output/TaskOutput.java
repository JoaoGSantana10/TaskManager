package dio.taskmanager.application.output;

import dio.taskmanager.domain.Task;

import java.util.Optional;

public record TaskOutput(String id, String tittle, Optional<String> description, String status) {

    public static TaskOutput from(Task task) {
        return new TaskOutput(task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name()
        );
    }
}
