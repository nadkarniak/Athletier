package com.CS5520.athletier.Models;

public class SportsBadge {
    private int id;
    private String name;
    private Sport sport;
    private String description;
    private String iconName;

    public SportsBadge(String name, Sport sport, String description, String iconName) {
        this.name = name;
        this.sport = sport;
        this.description = description;
        this.iconName = iconName;
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

    public String getIconName() {
        return this.iconName;
    }
}
