package com.ecommerce.domain.entity;

public enum Roles {
    CLIENT,
    ADMIN;


    public String getSimpleName() {
        return this.name().replace("ROLE_","");
    }
}
