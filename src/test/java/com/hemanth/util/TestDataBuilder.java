package com.hemanth.util;

import com.hemanth.models.Support;
import com.hemanth.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for building test data objects
 */
public class TestDataBuilder {

    private static final Random random = new Random();

    /**
     * Build a User object with random data
     */
    public static User buildRandomUser() {
        return new User()
                .setName("Test User " + random.nextInt(1000))
                .setJob("Test Job " + random.nextInt(1000))
                .setEmail("test" + random.nextInt(1000) + "@example.com")
                .setFirstName("FirstName" + random.nextInt(1000))
                .setLastName("LastName" + random.nextInt(1000))
                .setAvatar("https://example.com/avatar" + random.nextInt(1000) + ".jpg");
    }

    /**
     * Build a User object with specific data
     */
    public static User buildUser(String name, String job) {
        return new User()
                .setName(name)
                .setJob(job)
                .setEmail(name.toLowerCase().replace(" ", ".") + "@example.com")
                .setFirstName(name.split(" ")[0])
                .setLastName(name.split(" ").length > 1 ? name.split(" ")[1] : "");
    }

    /**
     * Build a User object with all fields
     */
    public static User buildFullUser(Integer id, String name, String job, String email, 
                                   String firstName, String lastName, String avatar) {
        return new User(id, name, job, email, firstName, lastName, avatar);
    }

    /**
     * Build a Support object
     */
    public static Support buildSupport(String url, String text) {
        return new Support(url, text);
    }

    /**
     * Build a default Support object
     */
    public static Support buildDefaultSupport() {
        return new Support("https://reqres.in/#support-heading", 
                          "To keep ReqRes free, contributions towards server costs are appreciated!");
    }

    /**
     * Build a list of random users
     */
    public static List<User> buildRandomUserList(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(buildRandomUser());
        }
        return users;
    }

    /**
     * Build a list of users with specific data
     */
    public static List<User> buildUserList(String[] names, String[] jobs) {
        List<User> users = new ArrayList<>();
        int maxLength = Math.min(names.length, jobs.length);
        
        for (int i = 0; i < maxLength; i++) {
            users.add(buildUser(names[i], jobs[i]));
        }
        return users;
    }

    /**
     * Build a User object from CSV data
     */
    public static User buildUserFromCsvData(String name, String job) {
        return buildUser(name, job);
    }

    /**
     * Build a User object with minimal required fields
     */
    public static User buildMinimalUser(String name, String job) {
        return new User(name, job);
    }

    /**
     * Build a User object for update operations
     */
    public static User buildUserForUpdate(Integer id, String name, String job) {
        return new User()
                .setId(id)
                .setName(name)
                .setJob(job);
    }

    /**
     * Get a random name from predefined list
     */
    public static String getRandomName() {
        String[] names = {"John Doe", "Jane Smith", "Bob Johnson", "Alice Brown", 
                         "Charlie Wilson", "Diana Davis", "Edward Miller", "Fiona Garcia"};
        return names[random.nextInt(names.length)];
    }

    /**
     * Get a random job from predefined list
     */
    public static String getRandomJob() {
        String[] jobs = {"Software Engineer", "QA Engineer", "DevOps Engineer", "Product Manager",
                        "Data Analyst", "UX Designer", "Project Manager", "Business Analyst"};
        return jobs[random.nextInt(jobs.length)];
    }

    /**
     * Build a User object with random realistic data
     */
    public static User buildRealisticUser() {
        return buildUser(getRandomName(), getRandomJob());
    }
}
