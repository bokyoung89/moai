package com.bokyoung.moai.newmember.controller;

import com.bokyoung.moai.newmember.controller.response.NewMemberResponse;
import com.bokyoung.moai.newmember.controller.response.NewMemberRouteResponse;
import com.bokyoung.moai.newmember.service.NewMemberService;
import com.bokyoung.moai.newmember.service.dto.NewMemberRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "NewMember", description = "신규 사용자 수 조회 요청을 처리합니다.")
public class NewMemberController {

    private final ModelMapper mapper;
    private final NewMemberService newMemberService;

    @GetMapping("/moai/new-member/count-all")
    @Operation(summary = "전체 신규 사용자 수 조회",
        description = "신규 가입한 전체 사용자 수 요청을 조회합니다.",
        security = @SecurityRequirement(name = "Authorization")
    )
//    @PreAuthorize("hasAnyRole('users')")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewMemberResponse.class)))
    public List<NewMemberResponse> getNewMemberCountByAll(
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        NewMemberRequestDto requestDto = NewMemberRequestDto.builder()
            .startDate(startDate).endDate(endDate).build();

        return mapper.map(newMemberService.getNewMemberCountByAll(requestDto), new TypeToken<List<NewMemberResponse>>(){}.getType());
    }

    @GetMapping("/moai/new-member/count-by-route")
    @Operation(summary = "채널별 신규 사용자 수 조회",
            description = "신규 가입한 채널별 사용자 수 요청을 조회합니다.",
            security = @SecurityRequirement(name = "Authorization")
    )
//    @PreAuthorize("hasAnyRole('users')")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NewMemberRouteResponse.class)))
    public List<NewMemberRouteResponse> getNewMemberCountByRoute(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "route", required = false) String route) {

        NewMemberRequestDto requestDto = NewMemberRequestDto.builder()
                .startDate(startDate).endDate(endDate).route(route).build();

        return mapper.map(newMemberService.getNewMemberCountByRoute(requestDto), new TypeToken<List<NewMemberRouteResponse>>(){}.getType());
    }
}
