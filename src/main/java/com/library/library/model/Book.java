package com.library.library.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String name;
    private Integer count;
    private List<String> users = new ArrayList<>();

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void incrementCount() {
        count = (count == null) ? 1 : count + 1;
    }

    public void decrementCount() {
        count = count - 1;
    }

    public void addUser(String user) {
        users.add(user);
    }

    public boolean isAlreadyTaken(String user) {
        return users.contains(user);
    }

    public void removeUser(String user) {
        users.remove(user);
    }
}
