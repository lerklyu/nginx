package ru.netology.amlicode.mapper;

import org.springframework.stereotype.Component;
import ru.netology.amlicode.dto.TaskRequest;
import ru.netology.amlicode.dto.TaskResponse;
import ru.netology.amlicode.model.Task;

@Component
public class TaskMapper {

    public Task toEntity(TaskRequest request) {
        return new Task(
                null,
                request.getTitle(),
                request.getDescription(),
                request.isCompleted()
        );
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }
}
