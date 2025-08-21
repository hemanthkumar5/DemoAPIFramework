package com.hemanth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Support {

    @JsonProperty("url")
    private String url;
    
    @JsonProperty("text")
    private String text;

    // Default constructor
    public Support() {}

    // Constructor with all fields
    public Support(String url, String text) {
        this.url = url;
        this.text = text;
    }

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public Support setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getText() {
        return text;
    }

    public Support setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return "Support{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Support support = (Support) o;
        return java.util.Objects.equals(url, support.url) &&
                java.util.Objects.equals(text, support.text);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(url, text);
    }
}
