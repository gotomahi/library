package com.library.library.model;

public class Book {
    private String name;
    private Integer count;

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public void incrementCount() {
        count = (count == null) ? 1 : count + 1;
    }
}
