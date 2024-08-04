package com.bokyoung.moai.userverification.domain;

import com.bokyoung.moai.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "", uniqueConstraints = {
    @UniqueConstraint(name = "USER_INFLUX_LOG_UDX_01", columnNames = {"did", "route"})
})

public class MoaiUserInfluxLog extends BaseEntity {

    @Id
    @Comment("로깅 일련번호")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Comment("분산 신원 확인 아이디")
    @Column(nullable = false, length = 40)
    private String did;

    @Comment("유입 경로")
    @Column(nullable = false)
    private String route;
}
