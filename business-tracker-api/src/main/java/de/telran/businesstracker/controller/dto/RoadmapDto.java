package de.telran.businesstracker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapDto {

    public Long id;

    public String name;

    public LocalDate startDate;

    public Long projectId;

}
