package com.bokyoung.moai.userverification.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMoaiUserVerification is a Querydsl query type for MoaiUserVerification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoaiUserVerification extends EntityPathBase<MoaiUserVerification> {

    private static final long serialVersionUID = 1972951631L;

    public static final QMoaiUserVerification moaiUserVerification = new QMoaiUserVerification("moaiUserVerification");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath did = createString("did");

    public final StringPath encryptedMaterial = createString("encryptedMaterial");

    public final StringPath hashedMaterial = createString("hashedMaterial");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath salt = createString("salt");

    public QMoaiUserVerification(String variable) {
        super(MoaiUserVerification.class, forVariable(variable));
    }

    public QMoaiUserVerification(Path<? extends MoaiUserVerification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMoaiUserVerification(PathMetadata metadata) {
        super(MoaiUserVerification.class, metadata);
    }

}

