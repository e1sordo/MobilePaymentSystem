package com.epam.lab.mobilepaymentsystem.model;

public enum Role {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUBSCRIBER("ROLE_SUBSCRIBER"),
    ROLE_USER("ROLE_USER"),
    ROLE_LOCKED("ROLE_LOCKED"),
    ROLE_DELETED("ROLE_DELETED");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
