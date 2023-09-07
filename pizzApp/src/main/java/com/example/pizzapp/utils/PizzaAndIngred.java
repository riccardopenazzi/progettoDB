package com.example.pizzapp.utils;

import java.util.List;

public class PizzaAndIngred {
    private String pizzaName;
    private List<String> ingredients;
    private Double price;

    public PizzaAndIngred(String pizzaName, List<String> ingredients, Double price) {
        this.pizzaName = pizzaName;
        this.ingredients = ingredients;
        this.price = price;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = this.price + price;
    }
}
