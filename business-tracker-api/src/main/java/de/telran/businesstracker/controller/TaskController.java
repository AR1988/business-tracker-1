package de.telran.businesstracker.controller;

import de.telran.businesstracker.model.Task;
import de.telran.businesstracker.controller.dto.TaskDto;
import de.telran.businesstracker.mapper.TaskMapper;
import de.telran.businesstracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("")
    public ResponseEntity<TaskDto> createTask(@RequestBody @Valid TaskDto taskDto) throws URISyntaxException {
        Task task = taskService.add(taskDto.name, taskDto.finished, taskDto.milestoneId, taskDto.memberId);
        taskDto.id = task.getId();
        return ResponseEntity
                .created(new URI("/api/tasks/" + task.getId()))
                .body(taskDto);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTask(@RequestBody @Valid TaskDto taskDto) throws HttpClientErrorException.BadRequest {
        taskService.edit(taskDto.id, taskDto.name, taskDto.finished);
    }

    @GetMapping("")
    public List<TaskDto> getAllTasks() {
        return taskService.getAll()
                .stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.removeById(id);
    }
}
