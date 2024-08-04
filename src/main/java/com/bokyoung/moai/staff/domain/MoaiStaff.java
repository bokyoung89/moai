package com.bokyoung.moai.staff.domain;

import com.bokyoung.moai.staff.constant.MoaiStaffRoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@SuperBuilder
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoaiStaff {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Schema(description = "사용자 ID", example = "test.123")
        @NotNull
        @Column(name = "userId", length = 20, nullable = false)
        private String userId;

        @Schema(description = "비밀번호", example = "{bcrypt}$2a$10$nSRdRx5IYvmpkrNHd1ZUP.4iNPpyxftmjaPUOgiKnZV2z3IYfl6qy")
        @NotNull
        @Column(name = "password", length = 100, nullable = false)
        private String password;

        @Schema(description = "사용자 권한", example = "CXO")
        @Column(name = "role", length = 30)
        @Enumerated(EnumType.STRING)
        private MoaiStaffRoleType role;

}
