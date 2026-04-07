package com.example.samrat.modules.doctor.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.doctor.dto.DoctorDTO;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public List<DoctorDTO> getAvailableDoctors() {
        return doctorRepository.findAvailableDoctorsByBranch(
                        TenantContext.getHospitalId(), TenantContext.getBranchId())
                .stream()
                .map(doctor -> {
                    DoctorDTO dto = modelMapper.map(doctor, DoctorDTO.class);
                    dto.setFullName(doctor.getUser().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findByHospitalIdAndSpecialization(
                        TenantContext.getHospitalId(), specialization)
                .stream()
                .map(doctor -> {
                    DoctorDTO dto = modelMapper.map(doctor, DoctorDTO.class);
                    dto.setFullName(doctor.getUser().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public DoctorDTO registerDoctor(Doctor doctor) {
        doctor.setHospitalId(TenantContext.getHospitalId());
        doctor.setBranchId(TenantContext.getBranchId());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return modelMapper.map(savedDoctor, DoctorDTO.class);
    }
}
