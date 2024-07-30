package com.bokyoung.moai.reward.domain;

import com.bokyoung.moai.common.entity.BaseUpdatedWithoutActivatedEntity;
import com.bokyoung.moai.reward.constant.ChallengeParticipateStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "challenge_participate",
        indexes = {
                @Index(name = "CHALLENGE_PARTICIPATE_IDX_01", columnList = "challengeId, status"),
                @Index(name = "CHALLENGE_PARTICIPATE_IDX_02", columnList = "did"),
        }
)
public class MoaiChallengeParticipate extends BaseUpdatedWithoutActivatedEntity {

    @Id
    @Comment("챌린지 참여 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Comment("챌린지 ID")
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long challengeId;

    @Comment("사용자 DID")
    @Column(nullable = false, length = 40)
    private String did;

    @Comment("사용자 닉네임")
    @Column(nullable = false, length = 30)
    private String nickName;

    @Comment("프로필 ID")
    @Column(nullable = false)
    private long profileId;

    @Comment("참여 포인트")
    @Column(nullable = false)
    private int applyPoint;

    @Comment("보상 포인트")
    private Integer rewardPoint;

    @Comment("성공 여부")
    private Boolean success;

    @Comment("참여 상태")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeParticipateStatus status;
}
