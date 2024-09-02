package com.bokyoung.moai.newmember.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.bokyoung.moai.newmember.constant.MemberConstant;
import com.bokyoung.moai.newmember.repository.MemberRepository;
import com.bokyoung.moai.newmember.repository.projection.NewMemberProjection;
import com.bokyoung.moai.newmember.service.NewMemberService;
import com.bokyoung.moai.newmember.service.dto.AgeItemDto;
import com.bokyoung.moai.newmember.service.dto.GenderItemDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberRequestDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewMemberServiceImpl implements NewMemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<NewMemberResponseDto> getNewMemberCountByAll(NewMemberRequestDto requestDto) {

        List<NewMemberProjection> newMemberProjectionList = memberRepository.findNewMemberCountByCreatedAt(
            requestDto.getStartDate(), requestDto.getEndDate());

        // 조회된 데이터를 날짜별로 그룹화
        Map<LocalDate, List<NewMemberProjection>> groupedByDate = newMemberProjectionList.stream()
            .collect(Collectors.groupingBy(NewMemberProjection::getDate));

        List<NewMemberResponseDto> responseList = new ArrayList<>();

        // 날짜별로 데이터 처리
        for(Map.Entry<LocalDate, List<NewMemberProjection>> entry : groupedByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<NewMemberProjection> projections = entry.getValue();

            //성별에 따른 카운트 계산과 리스트 생성
            List<GenderItemDto> genderItemList = createGenderItemList(projections);

            //연령대에 따른 카운트 계산과 리스트 생성
            List<AgeItemDto> ageItemList = createAgeItemList(projections);

            //전체 카운트 계산
            long totalcount = projections.size();

            NewMemberResponseDto responseDto = NewMemberResponseDto.builder()
                .date(date)
                .totalCount(totalcount)
                .genderCount(genderItemList)
                .ageCount(ageItemList)
                .build();

            responseList.add(responseDto);
        }

        //날짜 기준으로 오름차순으로 정렬하여 결과 리스트 반환
        return responseList.stream().sorted(Comparator.comparing(NewMemberResponseDto::getDate))
            .collect(Collectors.toList());
    }

    private static List<AgeItemDto> createAgeItemList(List<NewMemberProjection> projections) {
        //연령에 따른 카운트 계산
        Map<String, Long> ageCountMap = projections.stream()
            .collect(Collectors.groupingBy(
                p -> p.getAgeRange()!= null ? p.getAgeRange() : "Non",
                Collectors.counting()
            ));

        //연령대별 데이터 생성
        return MemberConstant.AGE_RANGES.stream()
            .map(age -> AgeItemDto.builder()
                .age(age)
                .count(ageCountMap.getOrDefault(age, 0L))
                .build())
            .toList();
    }

    private static List<GenderItemDto> createGenderItemList(List<NewMemberProjection> projections) {
        //성별에 따른 카운트 계산
        Map<String, Long> genderCountMap = projections.stream()
            .collect(Collectors.groupingBy(
                p -> p.getGender()!= null ? p.getGender() : "Non",
                Collectors.counting()
            ));

        //성별 데이터 생성
        return MemberConstant.GENDERS.stream()
            .map(gender -> GenderItemDto.builder()
                .gender(gender)
                .count(genderCountMap.getOrDefault(gender, 0L))
                .build())
            .toList();
    }


}
