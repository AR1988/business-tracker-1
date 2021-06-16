package de.telran.businesstracker.controller;

import de.telran.businesstracker.data.Milestone;
import de.telran.businesstracker.data.Task;
import de.telran.businesstracker.dto.TaskDto;
import de.telran.businesstracker.mapper.TaskMapper;
import de.telran.businesstracker.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
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

    @GetMapping("/by")
    public List<TaskDto> getKpiByParam(@RequestParam Map<String, String> params) {
        List<Task> tasks = taskService.getAll();
        String active = params.get("active");
        String roadMapId = params.get("roadMapId");
        String taskId = params.get("taskId");
        if (active != null) {
            boolean isActive = Boolean.parseBoolean(active);
            return tasks.stream().filter(task -> task.getActive() == isActive).map(taskMapper::toDto).collect(Collectors.toList());
        } else if (roadMapId != null) {
            long id = Long.parseLong(roadMapId);
            return tasks.stream().filter(task -> task.getMilestone().getRoadmap().getId() == id).map(taskMapper::toDto).collect(Collectors.toList());
        } else if (taskId != null) {
            long id = Long.parseLong(taskId);
            return tasks.stream().filter(task -> task.getId() == id).map(taskMapper::toDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.removeById(id);
    }
}
