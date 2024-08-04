package com.bokyoung.moai.userverification.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMoaiUserInfluxLog is a Querydsl query type for MoaiUserInfluxLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoaiUserInfluxLog extends EntityPathBase<MoaiUserInfluxLog> {

    private static final long serialVersionUID = -635122654L;

    public static final QMoaiUserInfluxLog moaiUserInfluxLog = new QMoaiUserInfluxLog("moaiUserInfluxLog");

    public final com.bokyoung.moai.common.entity.QBaseEntity _super = new com.bokyoung.moai.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath did = createString("did");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath route = createString("route");

    public QMoaiUserInfluxLog(String variable) {
        super(MoaiUserInfluxLog.class, forVariable(variable));
    }

    public QMoaiUserInfluxLog(Path<? extends MoaiUserInfluxLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMoaiUserInfluxLog(PathMetadata metadata) {
        super(MoaiUserInfluxLog.class, metadata);
    }

}

