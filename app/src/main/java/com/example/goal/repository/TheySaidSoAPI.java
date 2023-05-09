package com.example.goal.repository;

public class TheySaidSoAPI {
    public String quote;


    public TheySaidSoAPI() {
    }

    public TheySaidSoAPI(String quote) {
        this.quote = quote;
    }
    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }
}
