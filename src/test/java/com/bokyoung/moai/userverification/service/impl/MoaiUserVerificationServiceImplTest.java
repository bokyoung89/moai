package com.bokyoung.moai.userverification.service.impl;

import com.bokyoung.moai.userverification.repository.MoaiUserVerificationRepository;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationRouteProjection;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRouteResponseDto;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("기간별/채널별 본인인증 사용자 수 조회 테스트")
class MoaiUserVerificationServiceImplTest {

    @Mock
    private MoaiUserVerificationRepository moaiUserVerificationRepository;

    @InjectMocks
    private MoaiUserVerificationServiceImpl moaiUserVerificationService;

    @Mock
    private ModelMapper mapper;

    private Type listType;

    @BeforeEach
    public void setUp() {
        listType = new TypeToken<List<MoaiUserVerificationResponseDto>>() {
        }.getType();
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

    @Test
    @DisplayName("채널별 본인인증 사용자 수 조회 테스트")
    void getUserVerificationCountByRoute() {
        LocalDate startDate = LocalDate.of(2024, 7, 20);
        LocalDate endDate = LocalDate.of(2024, 7, 23);

        MoaiUserVerificationRequestDto requestDto = new MoaiUserVerificationRequestDto();
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        MoaiUserVerificationRouteProjection projection1 = new MoaiUserVerificationRouteProjection() {
            @Override
            public LocalDate getDay() {
                return LocalDate.of(2024, 7, 20);
            }

            @Override
            public String getRoute() {
                return "Route1";
            }

            @Override
            public Long getCount() {
                return 10L;
            }
        };

        MoaiUserVerificationRouteProjection projection2 = new MoaiUserVerificationRouteProjection() {
            @Override
            public LocalDate getDay() {
                return LocalDate.of(2024, 7, 20);
            }

            @Override
            public String getRoute() {
                return "Route2";
            }

            @Override
            public Long getCount() {
                return 5L;
            }
        };

        MoaiUserVerificationRouteProjection projection3 = new MoaiUserVerificationRouteProjection() {
            @Override
            public LocalDate getDay() {
                return LocalDate.of(2024, 7, 21);
            }

            @Override
            public String getRoute() {
                return "Route1";
            }

            @Override
            public Long getCount() {
                return 7L;
            }
        };

        List<MoaiUserVerificationRouteProjection> projectionList = Arrays.asList(projection1, projection2, projection3);

        Map<String, Long> routeCountMapDay1 = new HashMap<>();
        routeCountMapDay1.put("Route1", 10L);
        routeCountMapDay1.put("Route2", 5L);

        Map<String, Long> routeCountMapDay2 = new HashMap<>();
        routeCountMapDay2.put("Route1", 7L);

        MoaiUserVerificationRouteResponseDto responseDto1 = new MoaiUserVerificationRouteResponseDto(LocalDate.of(2024, 7, 20), routeCountMapDay1);
        MoaiUserVerificationRouteResponseDto responseDto2 = new MoaiUserVerificationRouteResponseDto(LocalDate.of(2024, 7, 21), routeCountMapDay2);

        List<MoaiUserVerificationRouteResponseDto> expectedResponse = Arrays.asList(responseDto1, responseDto2);

        when(moaiUserVerificationRepository.findUserVerificationCountByRouteInGroupByCreatedAtAndRoute(startDate, endDate))
                .thenReturn(projectionList);

        List<MoaiUserVerificationRouteResponseDto> result = moaiUserVerificationService.getUserVerificationCountByRoute(requestDto);

        assertEquals(expectedResponse.size(), result.size());

        for (int i = 0; i < expectedResponse.size(); i++) {
            MoaiUserVerificationRouteResponseDto expectedDto = expectedResponse.get(i);
            MoaiUserVerificationRouteResponseDto resultDto = result.get(i);

            assertEquals(expectedDto.getDay(), resultDto.getDay());
            assertEquals(expectedDto.getCount(), resultDto.getCount());
        }
    }
}