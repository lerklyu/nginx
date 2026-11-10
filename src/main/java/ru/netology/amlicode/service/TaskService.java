
package ru.netology.amlicode.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.amlicode.dto.TaskRequest;
import ru.netology.amlicode.dto.TaskResponse;
import ru.netology.amlicode.exception.ResourceNotFoundException;
import ru.netology.amlicode.mapper.TaskMapper;
import ru.netology.amlicode.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private final TaskMapper taskMapper;

    // Создание задачи
    public TaskResponse createTask(TaskRequest request) {
        Long id = idGenerator.incrementAndGet();

        Task task = taskMapper.toEntity(request);
        task.setId(id);

        tasks.put(id, task);

        return taskMapper.toResponse(task);
    }

    // Получение всех задач
    public List<TaskResponse> getAllTasks() {
        return tasks.values()
                .stream()
                .sorted(Comparator.comparing(Task::getId))
                .map(taskMapper::toResponse)
                .toList();
    }

    // Получение задачи по ID
    public TaskResponse getTaskById(Long id) {
        Task task = findTaskById(id);
        return taskMapper.toResponse(task);
    }

    // Обновление задачи
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task updatedTask = tasks.compute(id, (key, existingTask) -> {
            if (existingTask == null) {
                throw new ResourceNotFoundException(
                        "Задача с ID " + id + " не найдена"
                );
            }

            return new Task(
                    id,
                    request.getTitle(),
                    request.getDescription(),
                    request.isCompleted()
            );
        });

        return taskMapper.toResponse(updatedTask);
    }

    // Удаление задачи
    public void deleteTask(Long id) {
        Task removedTask = tasks.remove(id);

        if (removedTask == null) {
            throw new ResourceNotFoundException(
                    "Задача с ID " + id + " не найдена"
            );
        }
    }

    private Task findTaskById(Long id) {
        Task task = tasks.get(id);

        if (task == null) {
            throw new ResourceNotFoundException(
                    "Задача с ID " + id + " не найдена"
            );
        }

        return task;
    }
}
