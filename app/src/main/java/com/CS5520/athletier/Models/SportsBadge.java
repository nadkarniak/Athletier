package com.CS5520.athletier.Models;

public class SportsBadge {
    private String name;
    private Sport sport;
    private String description;
    private int resId;

    public SportsBadge(String name, Sport sport, String description, int resId) {
        this.name = name;
        this.sport = sport;
        this.description = description;
        this.resId = resId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Sport getSport() {
        return this.sport;
    }

    public int getResId() {
        return this.resId;
    }
}
