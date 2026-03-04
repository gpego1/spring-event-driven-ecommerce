package com.ecommerce.domain.entity;

public enum Roles {
    CLIENT,
    ADMIN;

    public String getName() {
        return this.getName();

    }
    public String getSimpleName() {
        return this.getName().replace("ROLE_","");
    }
}
