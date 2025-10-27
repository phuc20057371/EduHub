package com.example.eduhubvn.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    ORGANIZATION_READ("organization:read"),
    ORGANIZATION_CREATE("organization:create"),
    ORGANIZATION_UPDATE("organization:update"),
    ORGANIZATION_DELETE("organization:delete"),
    ORGANIZATION_APPROVE("organization:approve"),

    LECTURER_READ("lecturer:read"),
    LECTURER_CREATE("lecturer:create"),
    LECTURER_UPDATE("lecturer:update"),
    LECTURER_DELETE("lecturer:delete"),
    LECTURER_APPROVE("lecturer:approve"),

    SCHOOL_READ("school:read"),
    SCHOOL_CREATE("school:create"),
    SCHOOL_UPDATE("school:update"),
    SCHOOL_DELETE("school:delete"),
    SCHOOL_APPROVE("school:approve"),

    COURSE_READ("course:read"),
    COURSE_CREATE("course:create"),
    COURSE_UPDATE("course:update"),
    COURSE_DELETE("course:delete"),
    // COURSE_APPROVE("course:approve");

    PROGRAM_READ("program:read"),
    PROGRAM_CREATE("program:create"),
    PROGRAM_UPDATE("program:update"),
    PROGRAM_ARCHIVE("program:archive");

    private final String permission;
}
