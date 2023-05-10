package com.example.goal.repository;

import java.util.List;

public class NinjasQuote {
    public List<TheySaidSoAPI> items;

    public NinjasQuote(List<TheySaidSoAPI> items) {
        this.items = items;
    }

    public NinjasQuote() {
    }

    public List<TheySaidSoAPI> getItems() {
        return items;
    }

    public void setItems(List<TheySaidSoAPI> items) {
        this.items = items;
    }


}
