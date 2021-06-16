package de.telran.businesstracker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {

    public Long id;

    public String name;

    public Integer hours;

    public Double cost;

    public Long taskId;
}
