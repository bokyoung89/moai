package com.bokyoung.moai.userverification.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@Entity
@Table(indexes = {
    @Index(name = "USER_VERIFICATION_IDX_01", columnList = "salt")
})
@NoArgsConstructor
public class MoaiUserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(unique = true, length = 70)
    private String did;

    @Column(length = 10)
    private String salt;

    @Column(unique = true, length = 64)
    private String hashedMaterial;

    @Column(unique = true)
    private String encryptedMaterial;

    @CreatedDate
    private LocalDateTime createdAt;
}
