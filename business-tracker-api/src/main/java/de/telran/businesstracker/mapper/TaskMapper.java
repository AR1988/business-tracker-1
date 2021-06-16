package de.telran.businesstracker.mapper;

import de.telran.businesstracker.model.Task;
import de.telran.businesstracker.controller.dto.TaskDto;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .finished(task.getFinished())
                .milestoneId(task.getMilestone().getId())
                .memberId(task.getResponsibleMember().getId())
                .build();
    }
}
