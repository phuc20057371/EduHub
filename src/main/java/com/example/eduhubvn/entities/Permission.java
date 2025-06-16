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

    LECTURER_READ("lecturer:read"),
    LECTURER_CREATE("lecturer:create"),
    LECTURER_UPDATE("lecturer:update"),
    LECTURER_DELETE("lecturer:delete");

    private final String permission;
}
