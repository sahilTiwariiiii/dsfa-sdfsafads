package com.example.samrat.modules.nurse.dto;

import lombok.Data;

@Data
public class NurseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String qualification;
    private String registrationNumber;
    private Long departmentId;
    private String shift;
    private boolean available;
}
