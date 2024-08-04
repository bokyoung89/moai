package com.bokyoung.moai.staff.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMoaiStaff is a Querydsl query type for MoaiStaff
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMoaiStaff extends EntityPathBase<MoaiStaff> {

    private static final long serialVersionUID = 1824295127L;

    public static final QMoaiStaff moaiStaff = new QMoaiStaff("moaiStaff");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final EnumPath<com.bokyoung.moai.staff.constant.MoaiStaffRoleType> role = createEnum("role", com.bokyoung.moai.staff.constant.MoaiStaffRoleType.class);

    public final StringPath userId = createString("userId");

    public QMoaiStaff(String variable) {
        super(MoaiStaff.class, forVariable(variable));
    }

    public QMoaiStaff(Path<? extends MoaiStaff> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMoaiStaff(PathMetadata metadata) {
        super(MoaiStaff.class, metadata);
    }

}

