package com.bokyoung.moai.newmember.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member_persona")
public class MemberPersona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("분산 신원 확인 아이디")
    @Column(nullable = false, length = 255)
    private String did;

    @Column(length = 1)
    @Comment("성별")
    private String gender;

    @Column(columnDefinition = "TINYINT UNSIGNED")
    @Comment("연령대")
    private Integer ageRange;

    @Column(length = 10)
    @Comment("통신사")
    private String mobileProvider;

    @Column(length = 100)
    @Comment("거주지")
    private String locality;
}