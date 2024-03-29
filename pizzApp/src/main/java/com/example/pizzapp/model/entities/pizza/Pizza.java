package com.example.pizzapp.model.entities.pizza;

import java.util.List;

public class Pizza {
    private String name;
    private Double price;
    private Boolean present;
    private List<String> ingredienti;

    private int count;

    public Pizza(final String name, final Double price, final List<String> ingredienti){
        this.name = name;
        this.price = price;
        this.ingredienti = ingredienti;
    }

    public Pizza(final String name, final List<String> ingredienti) {
        this.name = name;
        this.ingredienti = ingredienti;
    }

    public Pizza(final String name, final Double price, final Boolean present, final List<String> ingredienti) {
        this.name = name;
        this.price = price;
        this.present = present;
        this.ingredienti = ingredienti;
    }

    public Pizza(final String name, final int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getPresent() {
        return present;
    }

    public List<String> getIngredienti() {
        return ingredienti;
    }

    public int getCount() {
        return count;
    }
}
