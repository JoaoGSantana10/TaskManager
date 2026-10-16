package dio.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskmanagerApplication {
    // Servidor toncat na porta 8080
    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }

}
