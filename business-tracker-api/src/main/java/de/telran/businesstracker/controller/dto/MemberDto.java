package de.telran.businesstracker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    public Long id;

    public String position;

    public Long projectId;

    public Long userId;
}
