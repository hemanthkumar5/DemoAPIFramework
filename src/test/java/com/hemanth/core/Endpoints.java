package com.hemanth.core;

public class Endpoints {
    private Endpoints() {
    }

    public static String users() {
        return "/users";
    }

    public static String userById(int id) {
        return "/users/" + id;
    }
}
