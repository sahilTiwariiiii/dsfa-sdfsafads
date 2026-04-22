package com.example.samrat.modules.nurse.service;

import com.example.samrat.modules.nurse.dto.NurseDTO;
import com.example.samrat.modules.nurse.entity.Nurse;
import com.example.samrat.modules.nurse.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NurseService {

    private final NurseRepository nurseRepository;
    private final ModelMapper modelMapper;

    public List<NurseDTO> getAvailableNurses() {
        return nurseRepository.findByAvailableTrue().stream()
                .map(nurse -> {
                    NurseDTO dto = modelMapper.map(nurse, NurseDTO.class);
                    dto.setFullName(nurse.getUser().getFullName());
                    dto.setEmail(nurse.getUser().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<NurseDTO> getNursesByDepartment(Long departmentId) {
        return nurseRepository.findByDepartmentId(departmentId).stream()
                .map(nurse -> {
                    NurseDTO dto = modelMapper.map(nurse, NurseDTO.class);
                    dto.setFullName(nurse.getUser().getFullName());
                    dto.setEmail(nurse.getUser().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public NurseDTO getNurseById(Long id) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nurse not found"));
        NurseDTO dto = modelMapper.map(nurse, NurseDTO.class);
        dto.setFullName(nurse.getUser().getFullName());
        dto.setEmail(nurse.getUser().getEmail());
        return dto;
    }
}
