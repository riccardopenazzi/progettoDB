package com.example.pizzapp.utils;

import java.time.LocalTime;

public class Orario {
    private LocalTime ora;

    public Orario(LocalTime ora) {
        this.ora = ora;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }
}
