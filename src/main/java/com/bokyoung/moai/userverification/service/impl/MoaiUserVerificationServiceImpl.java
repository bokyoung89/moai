package com.bokyoung.moai.userverification.service.impl;

import com.bokyoung.moai.userverification.repository.MoaiUserVerificationRepository;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationRouteProjection;
import com.bokyoung.moai.userverification.service.MoaiUserVerificationService;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRouteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoaiUserVerificationServiceImpl implements MoaiUserVerificationService {

    private final ModelMapper mapper;

    private final MoaiUserVerificationRepository moaiUserVerificationRepository;

    @Override
    public List<MoaiUserVerificationResponseDto> getUserVerificationCountByPeriod(MoaiUserVerificationRequestDto requestDto) {

        List<MoaiUserVerificationProjection> verificationList =
                moaiUserVerificationRepository.findUserVerificationCountByDateInGroupByCreatedAt(
                        requestDto.getStartDate(), requestDto.getEndDate());
        return mapper.map(verificationList, new TypeToken<List<MoaiUserVerificationResponseDto>>() {
        }.getType());
    }

    @Override
    public List<MoaiUserVerificationRouteResponseDto> getUserVerificationCountByRoute(MoaiUserVerificationRequestDto requestDto) {

        List<MoaiUserVerificationRouteProjection> verificationRouteList =
                moaiUserVerificationRepository.findUserVerificationCountByRouteInGroupByCreatedAtAndRoute(
                        requestDto.getStartDate(), requestDto.getEndDate());

        Map<String, MoaiUserVerificationRouteResponseDto> responseMap = new HashMap<>();

        for (MoaiUserVerificationRouteProjection projection : verificationRouteList) {
            LocalDate day = projection.getDay();
            String route = projection.getRoute();
            Long count = projection.getCount();

            MoaiUserVerificationRouteResponseDto responseDto =
                    responseMap.computeIfAbsent(String.valueOf(day), k -> new MoaiUserVerificationRouteResponseDto(day, new HashMap<>()));

            responseDto.getCount().put(route, count);
        }

        return responseMap.values().stream()
                .sorted(Comparator.comparing(MoaiUserVerificationRouteResponseDto::getDay))
                .collect(Collectors.toList());
    }
}
