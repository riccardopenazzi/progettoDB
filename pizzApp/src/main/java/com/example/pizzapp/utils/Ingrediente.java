package com.example.pizzapp.utils;

public class Ingrediente {
    private String name;
    private Double price;

    private int count;

    public Ingrediente(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Ingrediente(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
