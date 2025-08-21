package com.hemanth.core;

import com.hemanth.models.User;
import com.hemanth.util.TestDataBuilder;
import com.hemanth.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Builder Pattern implementation for creating dynamic payloads
 */
public class PayloadBuilder {
    
    private final Map<String, Object> payload;
    private final Random random;
    
    public PayloadBuilder() {
        this.payload = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * Build User payload with Builder pattern
     */
    public static class UserPayloadBuilder {
        private final Map<String, Object> userPayload;
        private final Random random;
        
        public UserPayloadBuilder() {
            this.userPayload = new HashMap<>();
            this.random = new Random();
        }
        
        public UserPayloadBuilder withName(String name) {
            userPayload.put("name", name);
            return this;
        }
        
        public UserPayloadBuilder withJob(String job) {
            userPayload.put("job", job);
            return this;
        }
        
        public UserPayloadBuilder withEmail(String email) {
            userPayload.put("email", email);
            return this;
        }
        
        public UserPayloadBuilder withFirstName(String firstName) {
            userPayload.put("first_name", firstName);
            return this;
        }
        
        public UserPayloadBuilder withLastName(String lastName) {
            userPayload.put("last_name", lastName);
            return this;
        }
        
        public UserPayloadBuilder withAvatar(String avatar) {
            userPayload.put("avatar", avatar);
            return this;
        }
        
        public UserPayloadBuilder withRandomName() {
            String[] names = {"John Doe", "Jane Smith", "Bob Johnson", "Alice Brown", "Charlie Wilson"};
            userPayload.put("name", names[random.nextInt(names.length)]);
            return this;
        }
        
        public UserPayloadBuilder withRandomJob() {
            String[] jobs = {"Software Engineer", "QA Engineer", "DevOps Engineer", "Product Manager", "Data Analyst"};
            userPayload.put("job", jobs[random.nextInt(jobs.length)]);
            return this;
        }
        
        public UserPayloadBuilder withRandomEmail() {
            String[] domains = {"example.com", "test.com", "demo.com", "sample.com"};
            String name = "user" + random.nextInt(1000);
            String domain = domains[random.nextInt(domains.length)];
            userPayload.put("email", name + "@" + domain);
            return this;
        }
        
        public UserPayloadBuilder withRandomFirstName() {
            String[] firstNames = {"John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Edward", "Fiona"};
            userPayload.put("first_name", firstNames[random.nextInt(firstNames.length)]);
            return this;
        }
        
        public UserPayloadBuilder withRandomLastName() {
            String[] lastNames = {"Doe", "Smith", "Johnson", "Brown", "Wilson", "Davis", "Miller", "Garcia"};
            userPayload.put("last_name", lastNames[random.nextInt(lastNames.length)]);
            return this;
        }
        
        public UserPayloadBuilder withRandomAvatar() {
            userPayload.put("avatar", "https://example.com/avatar" + random.nextInt(1000) + ".jpg");
            return this;
        }
        
        public UserPayloadBuilder withRandomData() {
            return withRandomName()
                    .withRandomJob()
                    .withRandomEmail()
                    .withRandomFirstName()
                    .withRandomLastName()
                    .withRandomAvatar();
        }
        
        public UserPayloadBuilder withMinimalData() {
            return withRandomName().withRandomJob();
        }
        
        public Map<String, Object> build() {
            return new HashMap<>(userPayload);
        }
        
        public String buildAsJson() {
            return JsonUtils.toJson(userPayload);
        }
    }
    
    /**
     * Build Login payload
     */
    public static class LoginPayloadBuilder {
        private final Map<String, Object> loginPayload;
        private final Random random;
        
        public LoginPayloadBuilder() {
            this.loginPayload = new HashMap<>();
            this.random = new Random();
        }
        
        public LoginPayloadBuilder withEmail(String email) {
            loginPayload.put("email", email);
            return this;
        }
        
        public LoginPayloadBuilder withPassword(String password) {
            loginPayload.put("password", password);
            return this;
        }
        
        public LoginPayloadBuilder withRandomEmail() {
            String[] domains = {"example.com", "test.com", "demo.com"};
            String name = "user" + random.nextInt(1000);
            String domain = domains[random.nextInt(domains.length)];
            loginPayload.put("email", name + "@" + domain);
            return this;
        }
        
        public LoginPayloadBuilder withRandomPassword() {
            String password = "password" + random.nextInt(1000);
            loginPayload.put("password", password);
            return this;
        }
        
        public LoginPayloadBuilder withRandomData() {
            return withRandomEmail().withRandomPassword();
        }
        
        public Map<String, Object> build() {
            return new HashMap<>(loginPayload);
        }
        
        public String buildAsJson() {
            return JsonUtils.toJson(loginPayload);
        }
    }
    
    /**
     * Build Product payload
     */
    public static class ProductPayloadBuilder {
        private final Map<String, Object> productPayload;
        private final Random random;
        
        public ProductPayloadBuilder() {
            this.productPayload = new HashMap<>();
            this.random = new Random();
        }
        
        public ProductPayloadBuilder withName(String name) {
            productPayload.put("name", name);
            return this;
        }
        
        public ProductPayloadBuilder withDescription(String description) {
            productPayload.put("description", description);
            return this;
        }
        
        public ProductPayloadBuilder withPrice(double price) {
            productPayload.put("price", price);
            return this;
        }
        
        public ProductPayloadBuilder withCategory(String category) {
            productPayload.put("category", category);
            return this;
        }
        
        public ProductPayloadBuilder withStock(int stock) {
            productPayload.put("stock", stock);
            return this;
        }
        
        public ProductPayloadBuilder withRandomName() {
            String[] names = {"Laptop", "Smartphone", "Tablet", "Headphones", "Monitor", "Keyboard", "Mouse"};
            productPayload.put("name", names[random.nextInt(names.length)] + " " + random.nextInt(1000));
            return this;
        }
        
        public ProductPayloadBuilder withRandomDescription() {
            String[] descriptions = {"High quality product", "Best in class", "Premium features", "Reliable performance"};
            productPayload.put("description", descriptions[random.nextInt(descriptions.length)]);
            return this;
        }
        
        public ProductPayloadBuilder withRandomPrice() {
            double price = 10.0 + random.nextDouble() * 990.0; // 10.0 to 1000.0
            productPayload.put("price", Math.round(price * 100.0) / 100.0);
            return this;
        }
        
        public ProductPayloadBuilder withRandomCategory() {
            String[] categories = {"Electronics", "Computers", "Mobile", "Accessories", "Gaming"};
            productPayload.put("category", categories[random.nextInt(categories.length)]);
            return this;
        }
        
        public ProductPayloadBuilder withRandomStock() {
            productPayload.put("stock", random.nextInt(1000));
            return this;
        }
        
        public ProductPayloadBuilder withRandomData() {
            return withRandomName()
                    .withRandomDescription()
                    .withRandomPrice()
                    .withRandomCategory()
                    .withRandomStock();
        }
        
        public Map<String, Object> build() {
            return new HashMap<>(productPayload);
        }
        
        public String buildAsJson() {
            return JsonUtils.toJson(productPayload);
        }
    }
    
    /**
     * Build Order payload
     */
    public static class OrderPayloadBuilder {
        private final Map<String, Object> orderPayload;
        private final Random random;
        
        public OrderPayloadBuilder() {
            this.orderPayload = new HashMap<>();
            this.random = new Random();
        }
        
        public OrderPayloadBuilder withUserId(int userId) {
            orderPayload.put("user_id", userId);
            return this;
        }
        
        public OrderPayloadBuilder withProductIds(java.util.List<Integer> productIds) {
            orderPayload.put("product_ids", productIds);
            return this;
        }
        
        public OrderPayloadBuilder withQuantity(int quantity) {
            orderPayload.put("quantity", quantity);
            return this;
        }
        
        public OrderPayloadBuilder withShippingAddress(String address) {
            orderPayload.put("shipping_address", address);
            return this;
        }
        
        public OrderPayloadBuilder withRandomUserId() {
            orderPayload.put("user_id", random.nextInt(1000) + 1);
            return this;
        }
        
        public OrderPayloadBuilder withRandomProductIds() {
            java.util.List<Integer> productIds = java.util.List.of(
                random.nextInt(100) + 1,
                random.nextInt(100) + 1
            );
            orderPayload.put("product_ids", productIds);
            return this;
        }
        
        public OrderPayloadBuilder withRandomQuantity() {
            orderPayload.put("quantity", random.nextInt(10) + 1);
            return this;
        }
        
        public OrderPayloadBuilder withRandomShippingAddress() {
            String[] streets = {"Main St", "Oak Ave", "Pine Rd", "Elm St", "Maple Dr"};
            String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
            String street = streets[random.nextInt(streets.length)];
            String city = cities[random.nextInt(cities.length)];
            int number = random.nextInt(9999) + 1;
            orderPayload.put("shipping_address", number + " " + street + ", " + city);
            return this;
        }
        
        public OrderPayloadBuilder withRandomData() {
            return withRandomUserId()
                    .withRandomProductIds()
                    .withRandomQuantity()
                    .withRandomShippingAddress();
        }
        
        public Map<String, Object> build() {
            return new HashMap<>(orderPayload);
        }
        
        public String buildAsJson() {
            return JsonUtils.toJson(orderPayload);
        }
    }
    
    /**
     * Build custom payload with key-value pairs
     */
    public PayloadBuilder addField(String key, Object value) {
        payload.put(key, value);
        return this;
    }
    
    public PayloadBuilder addFields(Map<String, Object> fields) {
        payload.putAll(fields);
        return this;
    }
    
    public PayloadBuilder removeField(String key) {
        payload.remove(key);
        return this;
    }
    
    public PayloadBuilder clear() {
        payload.clear();
        return this;
    }
    
    public Map<String, Object> build() {
        return new HashMap<>(payload);
    }
    
    public String buildAsJson() {
        return JsonUtils.toJson(payload);
    }
    
    public Object getField(String key) {
        return payload.get(key);
    }
    
    public boolean hasField(String key) {
        return payload.containsKey(key);
    }
    
    public int size() {
        return payload.size();
    }
    
    public boolean isEmpty() {
        return payload.isEmpty();
    }
    
    /**
     * Static factory methods
     */
    public static UserPayloadBuilder user() {
        return new UserPayloadBuilder();
    }
    
    public static LoginPayloadBuilder login() {
        return new LoginPayloadBuilder();
    }
    
    public static ProductPayloadBuilder product() {
        return new ProductPayloadBuilder();
    }
    
    public static OrderPayloadBuilder order() {
        return new OrderPayloadBuilder();
    }
    
    public static PayloadBuilder custom() {
        return new PayloadBuilder();
    }
}
