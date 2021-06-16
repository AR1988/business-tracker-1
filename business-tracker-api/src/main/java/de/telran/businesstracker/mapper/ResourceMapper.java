package de.telran.businesstracker.mapper;

import de.telran.businesstracker.model.Resource;
import de.telran.businesstracker.controller.dto.ResourceDto;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceDto toDto(Resource resource) {
        return ResourceDto.builder()
                .id(resource.getId())
                .name(resource.getName())
                .hours(resource.getHours())
                .cost(resource.getCost())
                .taskId(resource.getTask().getId())
                .build();
    }
}
