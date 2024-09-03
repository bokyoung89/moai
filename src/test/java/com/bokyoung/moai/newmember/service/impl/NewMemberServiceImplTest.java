package com.bokyoung.moai.newmember.service.impl;

import com.bokyoung.moai.newmember.repository.MemberRepository;
import com.bokyoung.moai.newmember.repository.projection.NewMemberProjection;
import com.bokyoung.moai.newmember.repository.projection.NewMemberRouteProjection;
import com.bokyoung.moai.newmember.service.dto.NewMemberRequestDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberResponseDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberRouteResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("기간별/채널별 신규 사용자 수 조회 테스트")
class NewMemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private NewMemberServiceImpl newMemberService;

    @Test
    @DisplayName("기간별 신규 사용자 수 조회 테스트")
    void getNewMemberCountByAll() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 23);

        NewMemberRequestDto requestDto = NewMemberRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<NewMemberProjection> projectionList = Arrays.asList(
                new NewMemberProjection() {
                    @Override
                    public LocalDate getDate() { return LocalDate.of(2024, 7, 20); }
                    @Override
                    public Long getCount() { return 5L; }
                },
                new NewMemberProjection() {
                    @Override
                    public LocalDate getDate() { return LocalDate.of(2024, 7, 21); }
                    @Override
                    public Long getCount() { return 3L; }
                }
        );

        List<NewMemberResponseDto> responseDtoList = Arrays.asList(
                new NewMemberResponseDto(LocalDate.of(2024, 7, 20), 5L),
                new NewMemberResponseDto(LocalDate.of(2024, 7, 21), 3L)
        );

        when(memberRepository.findNewMemberCountByCreatedAt(startDate, endDate)).thenReturn(projectionList);

        List<NewMemberResponseDto> result = newMemberService.getNewMemberCountByAll(requestDto);

        assertEquals(4, result.size());

        // 날짜 2024-07-20 검증
        NewMemberResponseDto responseDto = result.stream()
                .filter(dto -> dto.getDate().equals(LocalDate.of(2024, 7, 20)))
                .findFirst()
                .orElseThrow();

        assertEquals(5, responseDto.getCount());
    }

    @Test
    @DisplayName("채널별 신규 사용자 수 조회 테스트")
    void getNewMemberCountByRoute() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 23);

        NewMemberRequestDto requestDto = NewMemberRequestDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<NewMemberRouteProjection> projectionList = Arrays.asList(
                new NewMemberRouteProjection() {
                    @Override
                    public LocalDate getDate() { return LocalDate.of(2024, 7, 20); }
                    @Override
                    public String getRoute() { return "Route1"; }
                    @Override
                    public Long getCount() { return 5L; }
                },
                new NewMemberRouteProjection() {
                    @Override
                    public LocalDate getDate() { return LocalDate.of(2024, 7, 20); }
                    @Override
                    public String getRoute() { return "Route2"; }
                    @Override
                    public Long getCount() { return 5L; }
                },
                new NewMemberRouteProjection() {
                    @Override
                    public LocalDate getDate() { return LocalDate.of(2024, 7, 21); }
                    @Override
                    public String getRoute() { return "Route1"; }
                    @Override
                    public Long getCount() { return 7L; }
                }
        );

        when(memberRepository.findNewMemberCountByCreatedAtAndRoute(startDate, endDate)).thenReturn(projectionList);

        List<NewMemberRouteResponseDto> result = newMemberService.getNewMemberCountByRoute(requestDto);

        assertEquals(4, result.size());

        // 날짜 2024-07-20 검증
        NewMemberRouteResponseDto responseDto = result.stream()
                .filter(dto -> dto.getDate().equals(LocalDate.of(2024, 7, 20)))
                .findFirst()
                .orElseThrow();

        assertEquals(10, responseDto.getCount().values().stream().mapToLong(Long::longValue).sum());
    }
}