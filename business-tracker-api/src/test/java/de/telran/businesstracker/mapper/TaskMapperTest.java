package de.telran.businesstracker.mapper;

import de.telran.businesstracker.controller.dto.TaskDto;
import de.telran.businesstracker.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)

public class TaskMapperTest {

    @InjectMocks
    TaskMapper taskMapper;

    private Task task;

    @BeforeEach
    public void beforeEachTest() {
        User user = new User();
        Project project = new Project();
        Roadmap roadmap = new Roadmap();
        Member member = new Member(1L, "Boss", project, user);
        Milestone milestone = new Milestone(3L, "Milestone", LocalDate.now(), LocalDate.now().plusDays(10), roadmap);
        task = new Task(2L, "Task", false, false, "Document", milestone, member);
    }

    @Test
    public void mapTaskToTaskDto() {
        TaskDto taskDto = taskMapper.toDto(task);

        assertEquals(task.getId(), taskDto.id);
        assertEquals(task.getDelivery(), taskDto.delivery);
        assertEquals(task.isActive(), taskDto.acitve);
        assertEquals(task.isFinished(), taskDto.finished);
        assertEquals(task.getResponsibleMember().getId(), taskDto.memberId);
        assertEquals(task.getMilestone().getId(), taskDto.milestoneId);
        assertEquals(task.getName(), taskDto.name);
    }
}
