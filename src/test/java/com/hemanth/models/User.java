package com.hemanth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("job")
    private String job;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("first_name")
    private String firstName;
    
    @JsonProperty("last_name")
    private String lastName;
    
    @JsonProperty("avatar")
    private String avatar;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;

    // Default constructor
    public User() {}

    // Constructor with required fields
    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // Constructor with all fields
    public User(Integer id, String name, String job, String email, String firstName, String lastName, String avatar) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getJob() {
        return job;
    }

    public User setJob(String job) {
        this.job = job;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public User setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return java.util.Objects.equals(id, user.id) &&
                java.util.Objects.equals(name, user.name) &&
                java.util.Objects.equals(job, user.job) &&
                java.util.Objects.equals(email, user.email) &&
                java.util.Objects.equals(firstName, user.firstName) &&
                java.util.Objects.equals(lastName, user.lastName) &&
                java.util.Objects.equals(avatar, user.avatar);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, job, email, firstName, lastName, avatar);
    }
}
