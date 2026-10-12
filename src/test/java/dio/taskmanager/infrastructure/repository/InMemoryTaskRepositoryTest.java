package dio.taskmanager.infrastructure.repository;

import dio.taskmanager.domain.*;

class InMemoryTaskRepositoryTest extends TaskRepositoryTest {
    @Override
    protected TaskRepository createRepository() {
        return new InMemoryTaskRepository();
    }


}