package com.airline.service;

import com.airline.entity.CrewMember;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.CrewMemberRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewRosterService {

    private final CrewMemberRepository crewRepository;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class CrewResponse {
        private Long id;
        private String employeeId;
        private String fullName;
        private String role;
        private String licenseNumber;
        private Integer flightHours;
        private Boolean isActive;
    }

    @Transactional(readOnly = true)
    public List<CrewResponse> getAllCrew() {
        return crewRepository.findAll().stream().map(c -> CrewResponse.builder()
            .id(c.getId())
            .employeeId(c.getEmployeeId())
            .fullName(c.getFirstName() + " " + c.getLastName())
            .role(c.getRole())
            .licenseNumber(c.getLicenseNumber())
            .flightHours(c.getFlightHoursAccumulated())
            .isActive(c.getIsActive())
            .build()).collect(Collectors.toList());
    }

    @Transactional
    public CrewResponse addCrewMember(String empId, String firstName, String lastName, String role, String license) {
        CrewMember c = CrewMember.builder()
            .employeeId(empId)
            .firstName(firstName)
            .lastName(lastName)
            .role(role)
            .licenseNumber(license)
            .flightHoursAccumulated(0)
            .isActive(true)
            .build();
        CrewMember saved = crewRepository.save(c);
        return CrewResponse.builder()
            .id(saved.getId())
            .employeeId(saved.getEmployeeId())
            .fullName(saved.getFirstName() + " " + saved.getLastName())
            .role(saved.getRole())
            .licenseNumber(saved.getLicenseNumber())
            .flightHours(saved.getFlightHoursAccumulated())
            .isActive(saved.getIsActive())
            .build();
    }
}
