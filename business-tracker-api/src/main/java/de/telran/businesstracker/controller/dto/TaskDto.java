package de.telran.businesstracker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    public Long id;

    public String name;

    public Boolean finished;

    public Long milestoneId;

    public Long memberId;
}
