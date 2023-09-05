package com.example.pizzapp.utils;

public class Address {
    private String via;
    private int numero;
    private String comune;

    public Address(String via, int numero, String comune) {
        this.via = via;
        this.numero = numero;
        this.comune = comune;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    @Override
    public String toString() {
        return this.via + " " + this.numero + " " + this.comune;
    }
}
