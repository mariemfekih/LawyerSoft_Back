package com.example.gestion_user.entities.enums;


import static com.example.gestion_user.entities.Authority.ADMIN_AUTHORITIES;
import static com.example.gestion_user.entities.Authority.MANAGER_AUTHORITIES;

public enum Role {
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
