package de.telran.businesstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiDtoToAdd {
    public long mileStoneId;
    public String name;
}
