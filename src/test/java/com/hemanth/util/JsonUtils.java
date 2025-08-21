package com.hemanth.util;

import io.restassured.path.json.JsonPath;

public class JsonUtils {

    public static JsonPath jp(String responseBody){
        return new JsonPath(responseBody);
    }
}
