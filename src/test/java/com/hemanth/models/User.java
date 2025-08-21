package com.hemanth.models;

public class User {

    private String name;
    private String job;

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setJob(String job) {
        this.job = job;
        return this;
    }

}
