package com.codecrafters.taskhubcore.controller.jobs.mapper;

import com.codecrafters.taskhubcore.controller.jobs.dto.AddressDTO;
import com.codecrafters.taskhubcore.controller.jobs.dto.JobDTO;
import com.codecrafters.taskhubcore.controller.usuarios.dto.UserDTO;
import com.codecrafters.taskhubcore.model.entities.AddressEntity;
import com.codecrafters.taskhubcore.model.entities.JobEntity;
import com.codecrafters.taskhubcore.model.entities.UserEntity;
import com.codecrafters.taskhubcore.model.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobWebMapper {
    private final UserRepository userRepository;

    public JobEntity toEntity(JobDTO jobDTO) {
        AddressEntity addressEntity = AddressEntity.builder()
                .state(jobDTO.address().state())
                .city(jobDTO.address().city())
                .neighborhood(jobDTO.address().neighborhood())
                .complement(jobDTO.address().complement())
                .build();


        return JobEntity.builder()
                .title(jobDTO.title())
                .moment(jobDTO.moment())
                .details(jobDTO.details())
                .imageUrl(jobDTO.imageUrl())
                .payment(jobDTO.payment())
                .available(jobDTO.available())
                .address(addressEntity)
                .crafterId(jobDTO.crafter().id())
                .build();
    }

    public List<JobDTO> toListDTO(List<JobEntity> jobEntity) {
        List<JobDTO> jobsDTO = new ArrayList<>();
        jobEntity.forEach(job -> jobsDTO.add(toDTO(job)));
        return jobsDTO;
    }


    public JobDTO toDTO(JobEntity jobEntity) {
        AddressDTO addressDTO = AddressDTO.builder()
                .state(jobEntity.getAddress().getState())
                .city(jobEntity.getAddress().getCity())
                .neighborhood(jobEntity.getAddress().getNeighborhood())
                .complement(jobEntity.getAddress().getComplement())
                .build();


        UserEntity crafter = userRepository.findById(jobEntity.getCrafterId()).orElseThrow();

        return JobDTO.builder()
                .id(jobEntity.getId())
                .title(jobEntity.getTitle())
                .moment(jobEntity.getMoment())
                .details(jobEntity.getDetails())
                .imageUrl(jobEntity.getImageUrl())
                .payment(jobEntity.getPayment())
                .available(jobEntity.getAvailable())
                .address(addressDTO)
                .crafter(UserDTO.builder()
                        .id(crafter.getId())
                        .name(crafter.getName())
                        .phone(crafter.getPhone())
                        .email(crafter.getEmail())
                        .build())

                .subscribers(jobEntity.getSubscribersId() == null ? new HashSet<>() : jobEntity.getSubscribersId().stream()
                        .map(id -> {
                            UserEntity userEntity = userRepository.findById(id).orElseThrow();
                            return UserDTO.builder()
                                    .id(userEntity.getId())
                                    .name(userEntity.getName())
                                    .phone(userEntity.getPhone())
                                    .email(userEntity.getEmail())
                                    .build();
                        }).collect(Collectors.toSet()))
                .build();
    }
}