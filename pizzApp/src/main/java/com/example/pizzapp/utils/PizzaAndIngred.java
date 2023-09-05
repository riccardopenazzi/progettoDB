package com.example.pizzapp.utils;

import java.util.List;

public class PizzaAndIngred {
    private String pizzaName;
    private List<String> ingredients;

    public PizzaAndIngred(String pizzaName, List<String> ingredients) {
        this.pizzaName = pizzaName;
        this.ingredients = ingredients;
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
}
