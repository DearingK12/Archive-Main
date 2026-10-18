package com.virtualos.security;

public record User(String username, Role role) {
    public enum Role { ADMIN, USER }

    @Override
    public String toString() {
        return username + " (" + role.name().toLowerCase() + ")";
    }
}
