package com.bokyoung.moai.userverification.service.impl;

import com.bokyoung.moai.userverification.repository.MoaiUserVerificationRepository;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("기간별/채널별 본인인증 사용자 수 조회 테스트")
class MoaiUserVerificationServiceImplTest {

    @Mock
    private MoaiUserVerificationRepository moaiUserVerificationRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private MoaiUserVerificationServiceImpl moaiUserVerificationService;

    private Type listType;

    @BeforeEach
    public void setUp() {
        listType = new TypeToken<List<MoaiUserVerificationResponseDto>>() {}.getType();
    }

    @Test
    @DisplayName("기간별 본인인증 사용자 수 조회 테스트")
    void getUserVerificationCountByDate() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 23);

        MoaiUserVerificationRequestDto requestDto = new MoaiUserVerificationRequestDto();
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        MoaiUserVerificationProjection projection1 = new MoaiUserVerificationProjection() {
            @Override
            public LocalDate getDay() {
                return LocalDate.parse("2024-07-20");
            }

            @Override
            public Long getCount() {
                return 5L;
            }
        };

        MoaiUserVerificationProjection projection2 = new MoaiUserVerificationProjection() {
            @Override
            public LocalDate getDay() {
                return LocalDate.parse("2024-07-21");
            }

            @Override
            public Long getCount() {
                return 3L;
            }
        };

        List<MoaiUserVerificationProjection> projectionList = new ArrayList<>();
        projectionList.add(projection1);
        projectionList.add(projection2);

        List<MoaiUserVerificationResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(new MoaiUserVerificationResponseDto(LocalDate.parse("2024-07-20"), 5L));
        responseDtoList.add(new MoaiUserVerificationResponseDto(LocalDate.parse("2024-07-21"), 3L));

        when(moaiUserVerificationRepository.findUserVerificationCountByDateInGroupByCreatedAt(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(projectionList);

        when(mapper.map(any(List.class), any(Type.class))).thenAnswer(invocation -> {
            List<?> sourceList = invocation.getArgument(0);
            if (sourceList == projectionList) {
                return responseDtoList;
            }
            return new ArrayList<>();
        });

        // Act
        List<MoaiUserVerificationResponseDto> result = moaiUserVerificationService.getUserVerificationCountByPeriod(requestDto);

        // Assert
        assertEquals(LocalDate.parse("2024-07-20"), result.get(0).getDay());
        assertEquals(5L, result.get(0).getCount());
        assertEquals(LocalDate.parse("2024-07-21"), result.get(1).getDay());
        assertEquals(3L, result.get(1).getCount());

    }
}