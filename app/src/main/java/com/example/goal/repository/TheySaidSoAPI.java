package com.example.goal.repository;

import com.google.gson.annotations.SerializedName;

public class TheySaidSoAPI {

    @SerializedName("quote")
    public String quote;

    @SerializedName("author")
    private String author;


    @SerializedName("category")
    private String category;

    public TheySaidSoAPI(String quote, String author, String category) {
        this.quote = quote;
        this.author = author;
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TheySaidSoAPI() {
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }
}
