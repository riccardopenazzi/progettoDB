package com.example.pizzapp.utils;

public class Employee {
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String ruolo;

    private int codice;

    public Employee(String nome, String cognome, String email, String telefono, String ruolo, int codice) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.ruolo = ruolo;
        this.codice = codice;
    }

    public Employee(String nome, String cognome, String email, int codice, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.codice = codice;
        this.ruolo = ruolo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }
}
