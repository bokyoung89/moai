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
import org.springframework.cglib.core.Local;
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
    public List<MoaiUserVerificationResponseDto> getUserVerificationCountByAll(MoaiUserVerificationRequestDto requestDto) {

        List<MoaiUserVerificationProjection> verificationList =
                moaiUserVerificationRepository.findUserVerificationCountByDateInGroupByCreatedAt(
                        requestDto.getStartDate(), requestDto.getEndDate());

        Map<String, MoaiUserVerificationResponseDto> responseMap = new HashMap<>();

        //날짜값 초기 셋팅
        initialDateSettingsForAllCount(requestDto.getStartDate(), requestDto.getEndDate(), responseMap);

        for (MoaiUserVerificationProjection projection : verificationList) {
            LocalDate date = projection.getDate();
            Long count = projection.getCount();

            MoaiUserVerificationResponseDto responseDto = responseMap.get(date.toString());
            responseDto.setCount(count);
        }

        // 데이터 없는 날짜에 카운트 값 0으로 셋팅
        initialCountSettingsForAllCount(responseMap);

        return responseMap.values().stream()
                .sorted(Comparator.comparing(MoaiUserVerificationResponseDto::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<MoaiUserVerificationRouteResponseDto> getUserVerificationCountByRoute(MoaiUserVerificationRequestDto requestDto) {

        List<MoaiUserVerificationRouteProjection> verificationRouteList =
                moaiUserVerificationRepository.findUserVerificationCountByRouteInGroupByCreatedAtAndRoute(
                        requestDto.getStartDate(), requestDto.getEndDate());

        Map<String, MoaiUserVerificationRouteResponseDto> responseMap = new HashMap<>();
        Set<String> allRoutes = new HashSet<>();

        //날짜값 초기 셋팅
        initialDateSettingsForRouteCount(requestDto.getStartDate(), requestDto.getEndDate(), responseMap);

        for (MoaiUserVerificationRouteProjection projection : verificationRouteList) {
            LocalDate date = projection.getDate();
            String route = projection.getRoute();
            Long count = projection.getCount();

            MoaiUserVerificationRouteResponseDto responseDto = responseMap.get(date.toString());
            responseDto.getCount().put(route, count);
            allRoutes.add(route);
        }

        // 데이터 없는 날짜에 카운트 0으로 셋팅
        initialRouteSettingsForRouteCount(responseMap, allRoutes);

        return responseMap.values().stream()
                .sorted(Comparator.comparing(MoaiUserVerificationRouteResponseDto::getDate))
                .collect(Collectors.toList());
    }

    private static void initialDateSettingsForAllCount(LocalDate startDate, LocalDate endDate, Map<String, MoaiUserVerificationResponseDto> responseMap) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            responseMap.put(String.valueOf(date), new MoaiUserVerificationResponseDto(date, 0L));
        }
    }

    private static void initialCountSettingsForAllCount(Map<String, MoaiUserVerificationResponseDto> responseMap) {
        for (MoaiUserVerificationResponseDto responseDto : responseMap.values()) {
            if (responseDto.getCount() == null) {
                responseDto.setCount(0L);
            }
        }
    }

    private static void initialDateSettingsForRouteCount(LocalDate startDate, LocalDate endDate, Map<String, MoaiUserVerificationRouteResponseDto> responseMap) {
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            responseMap.put(String.valueOf(date), new MoaiUserVerificationRouteResponseDto(date, new HashMap<>()));
        }
    }

    private static void initialRouteSettingsForRouteCount(Map<String, MoaiUserVerificationRouteResponseDto> responseMap, Set<String> allRoutes) {
        for (MoaiUserVerificationRouteResponseDto responseDto : responseMap.values()) {
            Map<String, Long> counts = responseDto.getCount();
            for (String route : allRoutes) {
                counts.putIfAbsent(route, 0L);
            }
        }
    }
}