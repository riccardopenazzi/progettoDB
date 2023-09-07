package com.example.pizzapp.model.entities.review;

public class Review {
    private int codRecensione;
    private int codUtente;
    private int stelle;
    private String commento;

    public Review(int codRecensione, int codUtente, int stelle, String commento) {
        this.codRecensione = codRecensione;
        this.codUtente = codUtente;
        this.stelle = stelle;
        this.commento = commento;
    }

    public int getCodRecensione() {
        return codRecensione;
    }

    public void setCodRecensione(int codRecensione) {
        this.codRecensione = codRecensione;
    }

    public int getCodUtente() {
        return codUtente;
    }

    public void setCodUtente(int codUtente) {
        this.codUtente = codUtente;
    }

    public int getStelle() {
        return stelle;
    }

    public void setStelle(int stelle) {
        this.stelle = stelle;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }
}
