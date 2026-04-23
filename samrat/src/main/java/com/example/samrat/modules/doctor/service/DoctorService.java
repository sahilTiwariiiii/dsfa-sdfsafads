package com.example.samrat.modules.doctor.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.admin.repository.UserRepository;
import com.example.samrat.modules.doctor.dto.DoctorDTO;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findByHospitalId(TenantContext.getHospitalId(), pageable)
                .map(this::convertToDTO);
    }

    public DoctorDTO getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public List<DoctorDTO> getAvailableDoctors() {
        return doctorRepository.findAvailableDoctorsByBranch(
                        TenantContext.getHospitalId(), TenantContext.getBranchId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findByHospitalIdAndSpecialization(
                        TenantContext.getHospitalId(), specialization)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<DoctorDTO> searchDoctors(Long departmentId, String query, Pageable pageable) {
        return doctorRepository.searchDoctors(TenantContext.getHospitalId(), departmentId, query, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public DoctorDTO registerDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctor.setHospitalId(TenantContext.getHospitalId());
        doctor.setBranchId(TenantContext.getBranchId());

        if (doctorDTO.getUserId() != null) {
            doctor.setUser(userRepository.findById(doctorDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        if (doctorDTO.getDepartmentId() != null) {
            doctor.setDepartment(departmentRepository.findById(doctorDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found")));
        }

        Doctor savedDoctor = doctorRepository.save(doctor);
        return convertToDTO(savedDoctor);
    }

    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        modelMapper.map(doctorDTO, existingDoctor);
        existingDoctor.setId(id);

        if (doctorDTO.getDepartmentId() != null) {
            existingDoctor.setDepartment(departmentRepository.findById(doctorDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found")));
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return convertToDTO(updatedDoctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = modelMapper.map(doctor, DoctorDTO.class);
        if (doctor.getUser() != null) {
            dto.setFullName(doctor.getUser().getFullName());
            dto.setEmail(doctor.getUser().getEmail());
            dto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        }
        if (doctor.getDepartment() != null) {
            dto.setDepartmentId(doctor.getDepartment().getId());
            dto.setDepartmentName(doctor.getDepartment().getName());
        }
        return dto;
    }
}
