package com.example.pizzapp.model.entities.user;

public class User {
    private static int codUtente;
    private static String nome;
    private static String cognome;
    private static String email;
    private static int telefono;
    private static String tipo;

    public User(final int codUtente, final String nome, final String cognome, final String email, final int telefono, final String tipo) {
        User.codUtente = codUtente;
        User.nome = nome;
        User.cognome = cognome;
        User.email = email;
        User.telefono = telefono;
        User.tipo = tipo;
    }

    public int getCodUtente() {
        return codUtente;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefono() {
        return telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public static void setCodUtente(int codUtente) {
        User.codUtente = codUtente;
    }

    public void setNome(String nome) {
        User.nome = nome;
    }

    public void setCognome(String cognome) {
        User.cognome = cognome;
    }

    public void setEmail(String email) {
        User.email = email;
    }

    public void setTelefono(int telefono) {
        User.telefono = telefono;
    }

    public void setTipo(String tipo) {
        User.tipo = tipo;
    }
}
