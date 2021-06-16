package de.telran.businesstracker.controller.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDataDto {

    public String email;
    public String formId;
    //ask form page_1
    public String lastName;
    public String linkedInUrl;
    public String phoneNumber;
    public String firstName;
    //ask form page_2
    public List<String> startupPos;
    public List<String> interest;
    public List<String> businessGoals;
    public int engLvl;
    public boolean isSubmitted;

    //TODO video
    public String videoUrl1;
    public String videoUrl2;

}
