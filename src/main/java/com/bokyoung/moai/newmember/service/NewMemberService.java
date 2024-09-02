package com.bokyoung.moai.newmember.service;

import com.bokyoung.moai.newmember.service.dto.NewMemberRequestDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberResponseDto;
import com.bokyoung.moai.newmember.service.dto.NewMemberRouteResponseDto;

import java.util.List;

public interface NewMemberService {

    List<NewMemberResponseDto> getNewMemberCountByAll(NewMemberRequestDto requestDto);

    List<NewMemberRouteResponseDto> getNewMemberCountByRoute(NewMemberRequestDto requestDto);

}
