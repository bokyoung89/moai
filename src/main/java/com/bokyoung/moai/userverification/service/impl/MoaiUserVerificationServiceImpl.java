package com.bokyoung.moai.userverification.service.impl;

import com.bokyoung.moai.userverification.repository.MoaiUserVerificationRepository;
import com.bokyoung.moai.userverification.repository.projection.MoaiUserVerificationProjection;
import com.bokyoung.moai.userverification.service.MoaiUserVerificationService;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationRequestDto;
import com.bokyoung.moai.userverification.service.dto.MoaiUserVerificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoaiUserVerificationServiceImpl implements MoaiUserVerificationService {

    private final ModelMapper mapper;

    private final MoaiUserVerificationRepository moaiUserVerificationRepository;

    @Override
    public List<MoaiUserVerificationResponseDto> getUserVerificationCountByPeriod(MoaiUserVerificationRequestDto requestDto) {

        List<MoaiUserVerificationProjection> verificationList = moaiUserVerificationRepository.findUserVerificationCountByDateInGroupByCreatedAt(requestDto.getStartDate(), requestDto.getEndDate());
        return mapper.map(verificationList, new TypeToken<List<MoaiUserVerificationResponseDto>>() {
        }.getType());
    }
}
