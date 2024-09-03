package com.bokyoung.moai.newmember.service.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.bokyoung.moai.newmember.repository.MemberRepository;
import com.bokyoung.moai.newmember.service.NewMemberService;
import com.bokyoung.moai.newmember.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewMemberServiceImpl implements NewMemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<NewMemberResponseDto> getNewMemberCountByAll(NewMemberRequestDto requestDto) {
        //조회 시작일과 종료일 사이 날짜와 카운트 값 초기화
        Map<LocalDate, Long> responseMap = initializeDateMap(requestDto.getStartDate(), requestDto.getEndDate());

        memberRepository.findNewMemberCountByCreatedAt(requestDto.getStartDate(), requestDto.getEndDate())
                .forEach(projection -> responseMap.put(projection.getDate(), projection.getCount()));

        return responseMap.entrySet().stream()
                .map(entry -> new NewMemberResponseDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(NewMemberResponseDto::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<NewMemberRouteResponseDto> getNewMemberCountByRoute(NewMemberRequestDto requestDto) {
        //조회 시작일과 종료일 사이 날짜와 맵 초기화
        Map<LocalDate, Map<String, Long>> responseMap = initializeRouteDateMap(requestDto.getStartDate(), requestDto.getEndDate());

        memberRepository.findNewMemberCountByCreatedAtAndRoute(requestDto.getStartDate(), requestDto.getEndDate())
                .forEach(projection -> responseMap
                        .computeIfAbsent(projection.getDate(), k -> new HashMap<>())
                        .put(projection.getRoute(), projection.getCount()));

        //각 날짜에 채널과 카운트 값 초기화
        fillMissingRoutes(responseMap);

        return responseMap.entrySet().stream()
                .map(entry -> new NewMemberRouteResponseDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(NewMemberRouteResponseDto::getDate))
                .collect(Collectors.toList());
    }

    private Map<LocalDate, Long> initializeDateMap(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> map = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            map.put(date, 0L);
        }
        return map;
    }

    private Map<LocalDate, Map<String, Long>> initializeRouteDateMap(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Map<String, Long>> map = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            map.put(date, new HashMap<>());
        }
        return map;
    }

    private void fillMissingRoutes(Map<LocalDate, Map<String, Long>> responseMap) {
        Set<String> allRoutes = responseMap.values().stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        responseMap.values().forEach(routeMap -> {
            allRoutes.forEach(route -> routeMap.putIfAbsent(route, 0L));
        });
    }
}