package dio.taskmanager.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Repository focado em armazenar em mémoria
public interface TaskRepository {

    Task save(Task task);
    List<Task> findAll();
    Optional<Task> findById(TaskId id);
    void delete(TaskId id);

}
