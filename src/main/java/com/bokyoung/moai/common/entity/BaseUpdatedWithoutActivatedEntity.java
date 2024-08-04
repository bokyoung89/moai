package com.bokyoung.moai.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class BaseUpdatedWithoutActivatedEntity extends BaseEntity {

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "DATETIME(6)")
    @Comment("변경일자")
    private LocalDateTime updatedAt;
}
