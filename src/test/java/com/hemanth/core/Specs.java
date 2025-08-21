package com.hemanth.core;

import com.hemanth.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class Specs {

    //    Private constructor to prevent creating instances of this class.
    private Specs() {
    }

    public static RequestSpecification request() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(Config.baseUri())
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .log(LogDetail.METHOD)
                .addHeader("Accept", "application/json");

        if (Config.apiKeyEnabled()) {
            builder.addHeader(Config.apiKeyHeader(), Config.apiKeyValue());
        }
        return builder.build();
    }

    // generic success response spec → reusable when multiple 2xx codes are acceptable (like 200 or 201
    public static ResponseSpecification success() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(equalTo(200), equalTo(201), equalTo(204)))
                .expectContentType(ContentType.JSON)
                .build();
    }


    public static ResponseSpecification ok() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification created() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification noContent() {
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .build();
    }

}