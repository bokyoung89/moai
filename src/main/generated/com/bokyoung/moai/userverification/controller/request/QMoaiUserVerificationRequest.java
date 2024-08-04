package com.bokyoung.moai.userverification.controller.request;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMoaiUserVerificationRequest is a Querydsl query type for MoaiUserVerificationRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoaiUserVerificationRequest extends EntityPathBase<MoaiUserVerificationRequest> {

    private static final long serialVersionUID = 2098874041L;

    public static final QMoaiUserVerificationRequest moaiUserVerificationRequest = new QMoaiUserVerificationRequest("moaiUserVerificationRequest");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath did = createString("did");

    public final StringPath encryptedMaterial = createString("encryptedMaterial");

    public final StringPath hashedMaterial = createString("hashedMaterial");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath salt = createString("salt");

    public QMoaiUserVerificationRequest(String variable) {
        super(MoaiUserVerificationRequest.class, forVariable(variable));
    }

    public QMoaiUserVerificationRequest(Path<? extends MoaiUserVerificationRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMoaiUserVerificationRequest(PathMetadata metadata) {
        super(MoaiUserVerificationRequest.class, metadata);
    }

}

