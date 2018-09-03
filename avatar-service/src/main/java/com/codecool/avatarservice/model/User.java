package com.codecool.avatarservice.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static List<User> users = new ArrayList<User>();

    private int id;
    private String uri;

    public User(int id) {
        setId(id);
    }

    public void setId(int id) {
        this.id = id;
        this.uri = String.format("https://api.adorable.io/avatars/240/%s.png", id);
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public static User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        User newUser = new User(id);
        users.add(newUser);
        return newUser;
    }
}
