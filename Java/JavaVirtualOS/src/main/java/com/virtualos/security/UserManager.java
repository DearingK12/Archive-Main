package com.virtualos.security;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class UserManager {
    private final Map<String, User> users = new LinkedHashMap<>();
    private User current;

    public UserManager() {
        users.put("admin", new User("admin", User.Role.ADMIN));
        users.put("guest", new User("guest", User.Role.USER));
        current = users.get("admin");
    }

    public User currentUser() { return current; }
    public Collection<User> listUsers() { return users.values(); }

    public boolean login(String username) {
        User user = users.get(username);
        if (user == null) return false;
        current = user;
        return true;
    }
}
