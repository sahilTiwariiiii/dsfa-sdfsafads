package com.example.samrat.modules.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request body to create a department")
public class DepartmentCreateRequest {

    @NotBlank(message = "Department name is required")
    @Schema(example = "Cardiology")
    private String name;

    @Schema(description = "Optional. If omitted, the system will auto-generate a unique code.", example = "CARD")
    private String code;

    private String description;
    private String headOfDepartment;
    private String location;
    private String contactNumber;
    private String email;
    private Integer totalBeds;
    private Integer availableBeds;
}

